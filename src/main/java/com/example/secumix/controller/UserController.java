package com.example.secumix.controller;

import com.example.secumix.repository.ProfileDetailRepository;
import com.example.secumix.services.impl.UserService;
import com.example.secumix.payload.request.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
