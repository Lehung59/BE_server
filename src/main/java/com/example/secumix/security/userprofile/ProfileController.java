package com.example.secumix.security.userprofile;

import com.cloudinary.Cloudinary;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping(value = "/users/profile")
public class ProfileController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private Cloudinary cloudinary;
    public Map upload(MultipartFile file)  {
        try{
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            return data;
        }catch (IOException io){
            throw new RuntimeException("Image upload fail");
        }
    }
    @PostMapping(value = "/update/{userID}")
    ResponseEntity<String> update(@PathVariable int userID,
                                  @RequestParam String firstname,
                                  @RequestParam String lastname,
                                  @RequestParam String address,
                                  @RequestParam String phoneNumber,
                                  @RequestParam String socialContact,
                                  @RequestParam MultipartFile avatar){
        User user = userService.findById(userID);
        if (user!=null){
            ProfileRequest profileRequest= new ProfileRequest();
            profileRequest.setFirstname(firstname);
            profileRequest.setLastname(lastname);
            profileRequest.setAddress(address);
            profileRequest.setSocialContact(socialContact);
            profileRequest.setPhoneNumber(phoneNumber);
            Map<String, Object> uploadResult = upload(avatar);
            profileRequest.setAvatar(uploadResult.get("secure_url").toString());
            profileService.UpdateProfile(profileRequest,userID);
            return ResponseEntity.status(HttpStatus.OK).body("Cập nhật thành công !");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy thông tin khách hàng");
        }

    }
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
