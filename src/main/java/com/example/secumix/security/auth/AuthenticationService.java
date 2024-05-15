package com.example.secumix.security.auth;



import com.example.secumix.security.Utils.EmailMix;
import com.example.secumix.security.Utils.UserUtils;
import com.example.secumix.security.config.JwtService;
import com.example.secumix.security.notify.Notify;
import com.example.secumix.security.notify.NotifyRepository;
import com.example.secumix.security.store.model.entities.Cart;
import com.example.secumix.security.store.repository.CartRepo;
import com.example.secumix.security.token.Token;
import com.example.secumix.security.token.TokenRepository;
import com.example.secumix.security.token.TokenType;
import com.example.secumix.security.user.AuthenticationType;
import com.example.secumix.security.user.Role;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserRepository;
import com.example.secumix.security.userprofile.ProfileDetail;
import com.example.secumix.security.userprofile.ProfileDetailRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final ProfileDetailRepository profileDetailRepository;
  @Autowired
  private CartRepo cartRepo;

  private final NotifyRepository notifyRepository;
  public  Optional<Token> getVerificationToken(String token) {
    Optional<Token> result= tokenRepository.findByToken(token);
    return result;
  }


  public AuthenticationResponse registerUser(@Valid RegisterRequest request) {

    var user = User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .enabled(false)
            .authType(AuthenticationType.DATABASE)
            .build();
    var savedUser = repository.save(user);

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    var profileDetail= ProfileDetail.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .createdAt(UserUtils.getCurrentDay())
            .user(user)
            .build();
    profileDetailRepository.save(profileDetail);
    var notify= Notify.builder()
            .description("\n" +
                    "Your account has been initialized. Welcome to our service!")
            .notiStatus(false)   //Chưa xem
            .deletedNoti(false)  //Chưa xóa
            .user(savedUser)
            .build();
    notifyRepository.save(notify);
    Cart cart = Cart.builder()
            .user(user)
            .build();
    cartRepo.save(cart);
    String recipientAddress = request.getEmail();
    String subject = "Xác nhận tài khoản";
    String confirmationUrl
            =   "http://localhost:9000/api/v1/auth/registrationConfirm.html?token=" + jwtToken;
    String message = "Tài khoản được khởi tạo từ DuyThuong. Tên tài khoản email : " +  recipientAddress + " .Nhấp vào liên kết sau để xác nhận đăng ký tài khoản:\n" + confirmationUrl;

    EmailMix e = new EmailMix("thuong0205966@huce.edu.vn", "crognmvpbikkgogj",0);
    e.sendContentToVer2(recipientAddress,subject,message);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
    //Nếu thành công ridirect https://mail.google.com/mail/u/0/#sent
  }
  public AuthenticationResponse registerShipper(@Valid RegisterRequest request) {

    var user = User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.SHIPPER)
            .enabled(false)
            .authType(AuthenticationType.DATABASE)
            .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    var profileDetail= ProfileDetail.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .createdAt(UserUtils.getCurrentDay())
            .user(user)
            .build();
    profileDetailRepository.save(profileDetail);
    var notify= Notify.builder()
            .description("\n" +
                    "Your account has been initialized. Welcome to our service!")
            .notiStatus(false)   //Chưa xem
            .deletedNoti(false)  //Chưa xóa
            .user(savedUser)
            .build();
    notifyRepository.save(notify);
    String recipientAddress = request.getEmail();
    String subject = "Xác nhận tài khoản";
    String confirmationUrl
            =   "http://localhost:9000/api/v1/auth/registrationConfirm.html?token=" + jwtToken;
    String message = "Tài khoản được khởi tạo từ DuyThuong. Tên tài khoản email : " +  recipientAddress + " .Nhấp vào liên kết sau để xác nhận đăng ký tài khoản:\n" + confirmationUrl;

    EmailMix e = new EmailMix("thuong0205966@huce.edu.vn", "crognmvpbikkgogj",0);
    e.sendContentToVer2(recipientAddress,subject,message);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
    //Nếu thành công ridirect https://mail.google.com/mail/u/0/#sent
  }
  public AuthenticationResponse registerManager(@Valid RegisterRequest request) {

    var user = User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.MANAGER)
            .enabled(false)
            .authType(AuthenticationType.DATABASE)
            .build();
    var savedUser = repository.save(user);

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    var profileDetail= ProfileDetail.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .createdAt(UserUtils.getCurrentDay())
            .user(user)
            .build();
    profileDetailRepository.save(profileDetail);
    var notify= Notify.builder()
            .description("\n" +
                    "Your account has been initialized. Welcome to our service!")
            .notiStatus(false)   //Chưa xem
            .deletedNoti(false)  //Chưa xóa
            .user(savedUser)
            .build();
    notifyRepository.save(notify);
    String recipientAddress = request.getEmail();
    String subject = "Xác nhận tài khoản";
    String confirmationUrl
            =   "http://localhost:9000/api/v1/auth/registrationConfirm.html?token=" + jwtToken;
    String message = "Tài khoản được khởi tạo từ DuyThuong. Tên tài khoản email : " +  recipientAddress + " .Nhấp vào liên kết sau để xác nhận đăng ký tài khoản:\n" + confirmationUrl;

    EmailMix e = new EmailMix("thuong0205966@huce.edu.vn", "crognmvpbikkgogj",0);
    e.sendContentToVer2(recipientAddress,subject,message);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
    //Nếu thành công ridirect https://mail.google.com/mail/u/0/#sent
  }


  public AuthenticationResponse authenticate(@NotNull AuthenticationRequest request) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );
    var user = repository.findByEmail(request.getEmail())
            .orElseThrow();
    user.setOnlineStatus(true);
    repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);

    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
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

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
