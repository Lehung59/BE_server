package com.example.secumix.security.auth;

import com.example.secumix.security.Exception.UserAlreadyExistsException;
import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.token.Token;
import com.example.secumix.security.user.AuthenticationType;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserService userService;

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
    ResponseEntity<ResponseObject> confirmRegistration(
            @RequestParam("token") String token) {
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
            User user = verificationToken.get().user;
            user.setEnabled(true);
            userService.SaveUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Kích hoạt thành công tài khoản của bạn", "")
            );
        }
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

    @GetMapping("/user")
    public Map<String, Object> getUsers(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return oAuth2User.getAttributes();
    }

    @GetMapping("/login/oauth2")
    public String getUser(@AuthenticationPrincipal OAuth2User oauth2User) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "null";
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof OAuth2User oauth)) {
            return "null";
        }

        String email = oauth2User.getAttribute("email");
        String googleId = oauth2User.getAttribute("sub");
        if (email == null) {
            return "null";
        }
        return email;
    }



    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        Optional<User> user = userService.FindByEmail(request.getEmail());
        if (user.isPresent()) {
            return user.get().isEnabled() ?
                    ResponseEntity.status(HttpStatus.OK).body(service.authenticate(request)
                    ) :
                    ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();

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
