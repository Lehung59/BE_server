package com.example.secumix.services.impl;


import com.example.secumix.Utils.EmailMix;
import com.example.secumix.Utils.UserUtils;
import com.example.secumix.exception.CustomException;
import com.example.secumix.notify.Notify;
import com.example.secumix.notify.NotifyRepository;
import com.example.secumix.entities.Cart;
import com.example.secumix.entities.Store;
import com.example.secumix.payload.request.AuthenticationRequest;
import com.example.secumix.payload.request.RegisterRequest;
import com.example.secumix.payload.response.AuthenticationResponse;
import com.example.secumix.repository.CartRepo;
import com.example.secumix.repository.StoreRepo;
import com.example.secumix.entities.Token;
import com.example.secumix.repository.TokenRepository;
import com.example.secumix.entities.TokenType;
import com.example.secumix.entities.AuthenticationType;
import com.example.secumix.entities.Role;
import com.example.secumix.entities.User;
import com.example.secumix.repository.UserRepository;
import com.example.secumix.entities.ProfileDetail;
import com.example.secumix.repository.ProfileDetailRepository;
import com.example.secumix.security.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ProfileDetailRepository profileDetailRepository;
    private final EmailMix e;
    @Value("${default_avt}")
    private String defaultAvt;

    private final CartRepo cartRepo;
    private final UserUtils userUtils;
    private final NotifyRepository notifyRepository;
    private final StoreRepo storeRepo;



    public Optional<Token> getVerificationToken(String token) {
        Optional<Token> result = tokenRepository.findByToken(token);
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
        var profileDetail = ProfileDetail.builder()
                .avatar(defaultAvt)
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .createdAt(UserUtils.getCurrentDay())
                .user(user)
                .build();
        profileDetailRepository.save(profileDetail);
        var notify = Notify.builder()
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
                = "https://charismatic-friendship-production.up.railway.app/api/v1/auth/registrationConfirm.html?token=" + jwtToken;
        String message = "Tài khoản được khởi tạo từ Admin. Tên tài khoản email : " + recipientAddress + " .Nhấp vào liên kết sau để xác nhận đăng ký tài khoản:\n" + confirmationUrl;

        e.sendContent(recipientAddress, subject, message);
        return AuthenticationResponse.builder()
                .userId(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .role(user.getRole())
                .email(user.getEmail())
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
        var profileDetail = ProfileDetail.builder()
                .avatar(defaultAvt)
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .createdAt(UserUtils.getCurrentDay())
                .user(user)
                .build();
        profileDetailRepository.save(profileDetail);
        var notify = Notify.builder()
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
                = "https://charismatic-friendship-production.up.railway.app/api/v1/auth/registrationConfirm.html?token=" + jwtToken;
        String message = "Tài khoản được khởi tạo từ Admin. Tên tài khoản email : " + recipientAddress + " .Nhấp vào liên kết sau để xác nhận đăng ký tài khoản:\n" + confirmationUrl;

        e.sendContent(recipientAddress, subject, message);
        return AuthenticationResponse.builder()
                .userId(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .role(user.getRole())
                .email(user.getEmail())
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
        var profileDetail = ProfileDetail.builder()
                .avatar(defaultAvt)
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .createdAt(UserUtils.getCurrentDay())
                .user(user)
                .build();
        profileDetailRepository.save(profileDetail);
        var notify = Notify.builder()
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
                = "https://charismatic-friendship-production.up.railway.app/api/v1/auth/registrationConfirm.html?token=" + jwtToken;
        String message = "Tài khoản được khởi tạo từ Admin. Tên tài khoản email : " + recipientAddress + " .Nhấp vào liên kết sau để xác nhận đăng ký tài khoản:\n" + confirmationUrl;

        e.sendContent(recipientAddress, subject, message);
        return AuthenticationResponse.builder()
                .userId(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .role(user.getRole())
                .email(user.getEmail())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
        //Nếu thành công ridirect https://mail.google.com/mail/u/0/#sent
    }

    public AuthenticationResponse authenticateOauth2(User user){
        user.setOnlineStatus(true);
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        Integer storeId = null;
        if(user.getRole().equals(Role.MANAGER) ){
            storeId = 0;
            Optional<Store> store = storeRepo.findByEmailManager(user.getEmail());
            if(store.isPresent()) storeId = store.get().getStoreId();
        }

        return AuthenticationResponse.builder()
                .userId(user.getId())
                .storeId(storeId)
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .role(user.getRole())
                .email(user.getEmail())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
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
        Integer storeId = null;
        if(user.getRole().equals(Role.MANAGER) ){
            storeId = 0;
            Optional<Store> store = storeRepo.findByEmailManager(user.getEmail());
            if(store.isPresent()) storeId = store.get().getStoreId();
        }

        return AuthenticationResponse.builder()
                .userId(user.getId())
                .storeId(storeId)
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .role(user.getRole())
                .email(user.getEmail())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void createForgetPassToken(String email){
        User user = repository.findByEmail(email).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"Khong ton tai mail nay"));
        String newToken = UUID.randomUUID().toString().substring(0,8).toUpperCase();

        var token = Token.builder()
                .user(user)
                .tokenType(TokenType.RESETPASSWORD)
                .token(newToken)
                .expired(false)
                .revoked(false)
                .created_at(UserUtils.getCurrentDay())
                .build();
        tokenRepository.save(token);
    }


    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .created_at(UserUtils.getCurrentDay())
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
        validUserTokens.forEach(token -> {
            tokenRepository.delete(token);
        });
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
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
                        .userId(user.getId())
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .role(user.getRole())
                        .email(user.getEmail())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
