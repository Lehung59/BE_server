package com.example.secumix.controller;

import com.example.secumix.exception.CustomException;
import com.example.secumix.ResponseObject;
import com.example.secumix.Utils.ImageUpload;
import com.example.secumix.Utils.UserUtils;
import com.example.secumix.services.impl.UserService;
import com.example.secumix.services.IProfileService;
import com.example.secumix.repository.ProfileDetailRepository;
import com.example.secumix.payload.request.ProfileRequest;
import com.example.secumix.payload.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;
    private final IProfileService profileService;
    private final ProfileDetailRepository profileDetailRepository;
    private final ImageUpload imageUpload;
    @Value("${default_avt}")
    private String defaultAvt;
    private final UserUtils userUtils;


    @GetMapping(value = "/profile/view")
    ResponseEntity<ResponseObject> getProfile() {
        try{
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
        } catch (CustomException ex){
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }

//    @PostMapping(value = "/profile/update")
//    ResponseEntity<ResponseObject> updateProfile(@RequestBody ProfileRequest profileRequest) {
//        try {
//            return ResponseEntity.status(HttpStatus.OK).body(
//                    new ResponseObject("OK", "Thành Công", "")
//            );
//        } catch (CustomException ex) {
//            return ResponseEntity.status(ex.getStatus())
//                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
//        }
//    }


    @PutMapping(value = "/profile/update")
    ResponseEntity<ResponseObject> update(
                                  @RequestParam(required = false) String firstname,
                                  @RequestParam(required = false) String lastname,
                                  @RequestParam(required = false) String address,
                                  @RequestParam(required = false) String phoneNumber,
                                  @RequestParam(required = false) String socialContact,
                                  @RequestParam(required = false) MultipartFile avatar) {
        try{

            String avt = defaultAvt;
            if(avatar != null){
                avt = imageUpload.upload(avatar);
            }
            ProfileRequest profileRequest = ProfileRequest.builder()
                    .userId(userUtils.getUserId())
                    .firstname(firstname)
                    .lastname(lastname)
                    .address(address)
                    .phoneNumber(phoneNumber)
                    .socialContact(socialContact)
                    .avatar(avt)
                    .build();
            profileService.updateProfile(profileRequest);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Thành Công", "")
            );
        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }

    }


}
