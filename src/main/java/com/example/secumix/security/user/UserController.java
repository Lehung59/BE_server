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
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    @Autowired
    private ProfileDetailRepository profileDetailRepository;
    @GetMapping("/profile")
    ResponseEntity<ProfileResponse> getProfile(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<ProfileResponse> profileResponse= profileDetailRepository.findProfileDetailBy(email).map(
                profileDetail -> {
                    ProfileResponse mapper= new ProfileResponse();
                    mapper.setFirstname(profileDetail.getFirstname());
                    mapper.setLastname(profileDetail.getLastname());
                    mapper.setAddress(profileDetail.getAddress());
                    mapper.setPhoneNumber(profileDetail.getPhoneNumber());
                    mapper.setSocialContact(profileDetail.getSocialContact());
                    mapper.setAvatar(profileDetail.getAvatar());
                    return mapper;
                }

        );
        return ResponseEntity.status(HttpStatus.OK).body(profileResponse.get());
    }

    @PostMapping(value = "/changepassword")
    public ResponseEntity<String> changePassword(
          @RequestBody ChangePasswordRequest request
    ) {
        service.changePassword(request);
        return ResponseEntity.ok().body("Thành công");
    }
}
