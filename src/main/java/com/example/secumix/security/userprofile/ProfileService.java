package com.example.secumix.security.userprofile;


import com.example.secumix.security.Exception.CustomException;
import com.example.secumix.security.store.repository.ProfileDetailRepo;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserRepository;
import com.example.secumix.security.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService implements IProfileService {
    private final ProfileDetailRepository profileDetailRepository;
    private final UserService userService;
    private final ModelMapper modelMapper= new ModelMapper();
    private final ProfileDetailRepo profileDetailRepo;
    private final UserRepository userRepository;

//    @Override
//    public void UpdateProfile(ProfileRequest profileRequest,int userID){
//        var user= userService.findById(userID);
//        if (user!=null){
//            ProfileDetail profileDetail= profileDetailRepository.findProfileDetailBy(user.getEmail()).get();
//            profileDetail.setAddress(profileRequest.getAddress());
//            profileDetail.setLastname(profileRequest.getLastname());
//            profileDetail.setFirstname(profileRequest.getFirstname());
//            profileDetail.setPhoneNumber(profileRequest.getPhoneNumber());
//            profileDetail.setSocialContact(profileRequest.getSocialContact());
//            profileDetail.setAvatar(profileRequest.getAvatar());
//            profileDetailRepository.save(profileDetail);
//        }
//    }


    @Override
    public ProfileResponse findProfileByUserID(int userID){
      ProfileDetail profileDetail =profileDetailRepository.findByUserID(userID);
      ProfileResponse profileResponse= modelMapper.map(profileDetail, ProfileResponse.class);
      return profileResponse;
    }

    @Override
    public void updateProfile(ProfileRequest profileRequest) {
        ProfileDetail oldProfile = profileDetailRepo.findByUserId(profileRequest.getUserId());
        oldProfile.setAddress(profileRequest.getAddress()!=null?profileRequest.getAddress():oldProfile.getAddress());
        oldProfile.setAvatar(!Objects.equals(profileRequest.getAvatar(), oldProfile.getAvatar()) ?profileRequest.getAvatar(): oldProfile.getAvatar());
        oldProfile.setFirstname(profileRequest.getFirstname()!=null?profileRequest.getFirstname():oldProfile.getFirstname());
        oldProfile.setLastname(profileRequest.getLastname()!=null?profileRequest.getLastname():oldProfile.getLastname());
        oldProfile.setAddress(profileRequest.getAddress()!=null?profileRequest.getAddress():oldProfile.getAddress());
        oldProfile.setPhoneNumber(profileRequest.getPhoneNumber()!=null?profileRequest.getPhoneNumber():oldProfile.getPhoneNumber());
        oldProfile.setSocialContact(profileRequest.getSocialContact()!=null?profileRequest.getSocialContact():oldProfile.getSocialContact());
        profileDetailRepo.save(oldProfile);
    }

}
