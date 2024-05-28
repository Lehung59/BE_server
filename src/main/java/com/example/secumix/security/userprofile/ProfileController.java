package com.example.secumix.security.userprofile;

import com.cloudinary.Cloudinary;
import com.example.secumix.security.Exception.CustomException;
import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.Utils.ImageUpload;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/user")
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;
    private final IProfileService profileService;
    private final ProfileDetailRepository profileDetailRepository;
    private final ImageUpload imageUpload;
    @Value("${default_avt}")
    private String defaultAvt;


    @GetMapping("/profile/view")
    ResponseEntity<ResponseObject> getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<ProfileResponse> profileResponse = profileDetailRepository.findProfileDetailBy(email).map(
                profileDetail -> {
                    ProfileResponse mapper = new ProfileResponse();
                    mapper.setUserId(profileDetail.getUser().getId());
                    mapper.setFirstname(profileDetail.getFirstname());
                    mapper.setLastname(profileDetail.getLastname());
                    mapper.setAddress(profileDetail.getAddress());
                    mapper.setPhoneNumber(profileDetail.getPhoneNumber());
                    mapper.setSocialContact(profileDetail.getSocialContact());
                    mapper.setAvatar(profileDetail.getAvatar());
                    return mapper;
                }

        );
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Thành Công", profileResponse)
        );
    }

    @PostMapping(value = "/profile/create")
    ResponseEntity<ResponseObject> createProfile(@RequestBody ProfileRequest profileRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Thành Công", "")
            );
        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }


//    @PostMapping(value = "/update/{userID}")
//    ResponseEntity<String> update(@PathVariable int userID,
//                                  @RequestParam String firstname,
//                                  @RequestParam String lastname,
//                                  @RequestParam String address,
//                                  @RequestParam String phoneNumber,
//                                  @RequestParam(required = false) String socialContact,
//                                  @RequestParam(required = false) MultipartFile avatar) {
//        ProfileRequest profileRequest = ProfileRequest.builder()
//                .userId(userID)
//                .firstname(firstname)
//                .lastname(lastname)
//                .address(address)
//                .phoneNumber(phoneNumber)
//                .socialContact(socialContact)
//                .avatar(address==null?defaultAvt:address)
//                .build();
//        profileService.createProfile(profileRequest);
//
//    }


}
