package com.example.secumix.security.userprofile;


import com.example.secumix.security.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService implements IProfileService {
    @Autowired
    private ProfileDetailRepository profileDetailRepository;
    @Autowired
    private UserService userService;

    private final ModelMapper modelMapper= new ModelMapper();
    @Override
    public void UpdateProfile(ProfileRequest profileRequest,int userID){
        var user= userService.findById(userID);
        if (user!=null){
            ProfileDetail profileDetail= profileDetailRepository.findProfileDetailBy(user.getEmail()).get();
            profileDetail.setAddress(profileRequest.getAddress());
            profileDetail.setLastname(profileRequest.getLastname());
            profileDetail.setFirstname(profileRequest.getFirstname());
            profileDetail.setPhoneNumber(profileRequest.getPhoneNumber());
            profileDetail.setSocialContact(profileRequest.getSocialContact());
            profileDetail.setAvatar(profileRequest.getAvatar());
            profileDetailRepository.save(profileDetail);
        }
    }@Override
    public ProfileResponse findProfileByUserID(int userID){
      ProfileDetail profileDetail =profileDetailRepository.findByUserID(userID);
      ProfileResponse profileResponse= modelMapper.map(profileDetail, ProfileResponse.class);
      return profileResponse;
    }

}
