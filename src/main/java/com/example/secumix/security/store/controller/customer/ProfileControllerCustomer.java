package com.example.secumix.security.store.controller.customer;


import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserService;
import com.example.secumix.security.userprofile.ProfileResponse;
import com.example.secumix.security.userprofile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")

public class ProfileControllerCustomer {
    private final ProfileService profileService;
    private final UserService userService;

    @GetMapping(value = "/info/{userID}")
    ResponseEntity<ProfileResponse> getProfileByUserID(@PathVariable int userID){
        User user = userService.findById(userID);
        if (user!=null){
            return ResponseEntity.status(HttpStatus.OK).body(profileService.findProfileByUserID(userID));
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
