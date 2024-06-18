package com.example.secumix.controller;


import com.example.secumix.Utils.EmailMix;
import com.example.secumix.Utils.UserUtils;
import com.example.secumix.entities.TokenType;
import com.example.secumix.exception.CustomException;
import com.example.secumix.exception.UserAlreadyExistsException;
import com.example.secumix.ResponseObject;
import com.example.secumix.payload.request.AuthenticationRequest;
import com.example.secumix.payload.response.AuthenticationResponse;
import com.example.secumix.repository.TokenRepository;
import com.example.secumix.repository.UserRepository;
import com.example.secumix.services.impl.AuthenticationService;
import com.example.secumix.payload.request.RegisterRequest;
import com.example.secumix.entities.Token;
import com.example.secumix.entities.User;
import com.example.secumix.services.impl.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailMix e;
    private final UserUtils userUtils;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//    @GetMapping("/login/oauth2")
//    public ResponseEntity<ResponseObject> oauth2Login(@AuthenticationPrincipal OAuth2User oauth2User) {
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("OK", "Login successly", oauth2User.getAttributes())
//        );
//    }


//    @GetMapping("/login/oauth2/code/{clientRegistrationId}")
//    public ResponseEntity<ResponseObject> oauth2LoginCallback(
//            @PathVariable String clientRegistrationId,
//            @AuthenticationPrincipal OAuth2User oauth2User) {
//        User userInfo = new User();
//        userInfo.setEmail(oauth2User.getAttribute("name"));
//        userInfo.setEmail(oauth2User.getAttribute("email"));
//        userInfo.setAuthType(AuthenticationType.valueOf(clientRegistrationId));
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("OK", "Login successly", userInfo)
//        );
//    }

    @PostMapping("/register/user")
    public ResponseEntity<ResponseObject> register(
            @RequestBody RegisterRequest request
    ) {
        try {
            userService.FindByEmail(request.getEmail())
                    .ifPresent(existingUser -> {
                        throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists.");
                    });

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Register success!", service.registerUser(request))
            );
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("FAILED", "User with email " + request.getEmail() + " already exists.", ""));
        }

    }

    @PostMapping("/register/shipper")
    public ResponseEntity<ResponseObject> registerShipper(
            @RequestBody RegisterRequest request
    ) {
        try {
            userService.FindByEmail(request.getEmail())
                    .ifPresent(existingUser -> {
                        throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists.");
                    });

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Register success!", service.registerShipper(request))
            );
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("FAILED", "User with email " + request.getEmail() + " already exists.", ""));
        }

    }

    @PostMapping("/register/manager")
    public ResponseEntity<ResponseObject> registerManager(
            @RequestBody RegisterRequest request
    ) {
        try {
            userService.FindByEmail(request.getEmail())
                    .ifPresent(existingUser -> {
                        throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists.");
                    });

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Register success!", service.registerManager(request))
            );
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("FAILED", "User with email " + request.getEmail() + " already exists.", ""));
        }

    }

    @GetMapping("/registrationConfirm.html")
    public ResponseEntity<?> confirmRegistration(@RequestParam("token") String token) {

        Optional<Token> verificationToken = service.getVerificationToken(token);

        if (verificationToken == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND", "Not found verificationToken ", "")
            );
        } else if (verificationToken.get().expired == true) {

            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", "Link dẫn đã hết hạn. Vui lòng liên hệ trợ giúp tại trang chủ", "")
            );

        } else {
            User user = verificationToken.get().getUser();
            user.setEnabled(true);
            userService.SaveUser(user);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, "http://localhost:5173/login")
                    .body(null);
        }
    }

    @GetMapping("/validtoken")
    public  boolean checkToken(@RequestParam("token") String token) {

        Optional<Token> verificationToken = service.getVerificationToken(token);
        if (verificationToken == null) {
            return false;

        } else if (verificationToken.get().expired == true) {

            return false;

        } else {
            User user = verificationToken.get().getUser();
            user.setEnabled(true);
            return true;


        }
    }

    @PostMapping("/forgetpassword")
    public ResponseEntity<?> forgetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        User user = userRepository.findByEmail(email).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy mail"));

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }
        List<Token> tokens = tokenRepository.findAllValidResetPassWord(user.getId());
        tokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokens.forEach(token -> {
            tokenRepository.delete(token);
        });

        // Tạo token mới
        String newToken = UUID.randomUUID().toString().substring(0,8).toUpperCase();
        Token passwordResetToken = Token.builder()
                .user(user)
                .tokenType(TokenType.RESETPASSWORD)
                .token(newToken)
                .expired(false)
                .revoked(false)
                .created_at(UserUtils.getCurrentDay())
                .build();;
        tokenRepository.save(passwordResetToken);

        // Gửi email với token
        String subject = "Mã đổi mật khẩu";
        String confirmationUrl = "<b>" + newToken + "</b>";
        String message = "Đây là mã yêu cầu thay đổi mật khẩu. Mã có hiệu lực trong vòng 10 phút." + confirmationUrl;

        e.sendContent(email, subject, message);
        return ResponseEntity.ok("Password reset token sent to email");
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<?> resetPassword(
            @RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        Token passwordResetToken = tokenRepository.findResetPassToken(token).orElseThrow(()->new CustomException(HttpStatus.BAD_REQUEST,"Invalid or expired token"));

        if (passwordResetToken.isRevoked() || (UserUtils.getCurrentDay().getTime() - passwordResetToken.getCreated_at().getTime() ) >10 * 60 * 1000 ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
        }

        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword)); // Mã hóa mật khẩu trước khi lưu
        userRepository.save(user);

        // Xóa token sau khi sử dụng
        tokenRepository.delete(passwordResetToken);

        return ResponseEntity.ok("Password reset successfully");
    }

    @PostMapping("/changepassword")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request) {
        String email = userUtils.getUserEmail();
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        User user = userRepository.findByEmail(email).orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND,"Khong tim thay"));


        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect");
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password must be different from the old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("Password changed successfully");
    }


//    @PostMapping("/oauth2")
//    public ResponseEntity<AuthenticationResponse> oauth2(@RequestBody AuthenticationRequest request) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = service.loginOauth2(authentication);
//        if (user == null) {
//            return "redirect:/login?fault";
//        }
//        if (user.getRole().getName().equalsIgnoreCase(Constant.ADMIN_NAME)) {
//            return "redirect:/dashboard";
//        } else if (user.getRole().getName().equalsIgnoreCase(Constant.USER_NAME)) {
//            return "redirect:/";
//        }
//        return "redirect:/login";
//    }

//    @GetMapping("/user")
//    public Map<String, Object> getUsers(@AuthenticationPrincipal OAuth2User oAuth2User) {
//        return oAuth2User.getAttributes();
//    }
//
//    @GetMapping("/oauth2/authorize/google")
//    public String getUser(@AuthenticationPrincipal OAuth2User oauth2User) {
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        return oauth2User.getAttribute("email");
////        if (authentication == null || !authentication.isAuthenticated()) {
////            return "null";
////        }
////
////        Object principal = authentication.getPrincipal();
////        if (!(principal instanceof OAuth2User oauth)) {
////            return "null";
////        }
////
////        String email = oauth2User.getAttribute("email");
////        String googleId = oauth2User.getAttribute("sub");
////        if (email == null) {
////            return "null";
////        }
////        return email;
//    }

//    @PostMapping("/")

    @GetMapping("/getUserFromToken")
    public ResponseEntity<?> getUserFromToken(@RequestParam String token) {
        Optional<Token> verificationToken = service.getVerificationToken(token);
        User user = verificationToken.get().getUser();
        AuthenticationResponse authenticationResponse =  AuthenticationResponse.builder()
                .userId(user.getId())
                .storeId(null)
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .role(user.getRole())
                .email(user.getEmail())
                .accessToken(token)
                .refreshToken(token)
                .build();

        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        Optional<User> user = userService.FindByEmail(request.getEmail());
        if (user.isPresent()) {
            return user.get().isEnabled() ?
                    ResponseEntity.status(HttpStatus.OK).body(service.authenticate(request)
                    ) :
                    ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseObject("FAILED", "Tài khoản chưa được kích hoạt", ""));

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }


}
