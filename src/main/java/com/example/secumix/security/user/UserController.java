package com.example.secumix.security.user;

import com.example.secumix.security.userprofile.ProfileDetailRepository;
import com.example.secumix.security.userprofile.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping(value ="/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    @Autowired
    private ProfileDetailRepository profileDetailRepository;

    @PostMapping(value = "/changepassword")
    public ResponseEntity<String> changePassword(
          @RequestBody ChangePasswordRequest request
    ) {
        service.changePassword(request);
        return ResponseEntity.ok().body("Thành công");
    }
}
