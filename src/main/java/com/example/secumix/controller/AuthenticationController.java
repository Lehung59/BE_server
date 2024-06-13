package com.example.secumix.controller;


import com.example.secumix.exception.UserAlreadyExistsException;
import com.example.secumix.ResponseObject;
import com.example.secumix.payload.request.AuthenticationRequest;
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
