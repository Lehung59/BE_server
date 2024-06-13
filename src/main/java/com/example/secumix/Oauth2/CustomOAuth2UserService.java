package com.example.secumix.Oauth2;

import com.example.secumix.entities.*;
import com.example.secumix.repository.TokenRepository;
import com.example.secumix.repository.UserRepository;
import com.example.secumix.services.impl.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService  {
	@Value("${application.security.jwt.secret-key}")
	private String secretKey;
	@Value("${application.security.jwt.expiration}")
	private long jwtExpiration;
	@Value("${application.security.jwt.refresh-token.expiration}")
	private long refreshExpiration;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository repository;
	@Autowired
	private TokenRepository tokenRepository;

	public CustomOAuth2UserService() {
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		String clientName = userRequest.getClientRegistration().getClientName();
		OAuth2User user =  super.loadUser(userRequest);

		Optional<User> OUser = userService.FindByEmail(user.getName());
		String accessToken;
		if (OUser.isEmpty()){
			User newUser = new User();
			newUser.setEmail(user.getName());
			newUser.setRole(Role.USER);
			newUser.setAuthType(AuthenticationType.valueOf(clientName.toUpperCase()));
			repository.save(newUser);
			accessToken = generateToken(newUser);
			saveUserToken(newUser,accessToken);
		}else {
			revokeAllUserTokens(OUser.get());
			accessToken = generateToken(OUser.get());
			saveUserToken(OUser.get(),accessToken);
		}
		return new CustomOAuth2User(user, clientName,accessToken);
	}
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	/* Phương thức chung để rút trích các thông tin từ claim của token.*/
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	/* Tạo một token dựa trên thông tin người dùng được cung cấp.*/
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}
	/* Tạo một token với các claims mở rộng (extraClaims) và thông tin người dùng.*/
	public String generateToken(
			Map<String, Object> extraClaims,
			UserDetails userDetails
	) {
		return buildToken(extraClaims, userDetails, jwtExpiration);
	}
	/* Tạo một refresh token dựa trên thông tin người dùng.*/
	public String generateRefreshToken(
			UserDetails userDetails
	) {
		return buildToken(new HashMap<>(), userDetails, refreshExpiration);
	}
	/*  Phương thức xây dựng token dựa trên các tham số được cung cấp.*/
	private String buildToken(
			Map<String, Object> extraClaims,
			UserDetails userDetails,
			long expiration
	) {
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	/* Kiểm tra xem token có hợp lệ không, dựa trên so sánh tên người dùng và kiểm tra hết hạn.*/
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}
	/* Kiểm tra xem token có hết hạn không.*/
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	/* Rút trích thông tin thời gian hết hạn từ token.*/
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	/*  Rút trích tất cả các claims từ token.*/
	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	/*  Lấy key sử dụng để ký và giải mã token từ secretKey được cung cấp.*/
	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	public void saveUserToken(User user, String jwtToken) {
		var token = Token.builder()
				.user(user)
				.token(jwtToken)
				.tokenType(TokenType.BEARER)
				.expired(false)
				.revoked(false)
				.build();
		tokenRepository.save(token);
	}

	public void revokeAllUserTokens(User user) {
		var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
		if (validUserTokens.isEmpty())
			return;
		validUserTokens.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		// Cân nhắc xóa đi hoặc dùng để tính thời gian không hoạt động
		tokenRepository.saveAll(validUserTokens);
	}
}