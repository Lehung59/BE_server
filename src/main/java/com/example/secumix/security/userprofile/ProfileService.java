package com.example.secumix.security.userprofile;


import com.example.secumix.security.Exception.CustomException;
import com.example.secumix.security.store.repository.ProfileDetailRepo;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService implements IProfileService {
    private final ProfileDetailRepository profileDetailRepository;
    private final UserService userService;
    private final ModelMapper modelMapper= new ModelMapper();
    private final ProfileDetailRepo profileDetailRepo;

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

    @Override
    public void createProfile(ProfileRequest profileRequest) {
        if(profileDetailRepo.findByUserIdCheck(profileRequest.getUserId()).isPresent())
            throw new CustomException(HttpStatus.NOT_IMPLEMENTED,"Da ton tai profile nay");
        User user= userService.findById(profileRequest.getUserId());
        ProfileDetail profileDetail = ProfileDetail.builder()
                .firstname(profileRequest.getFirstname())
                .lastname(profileRequest.getLastname())
                .phoneNumber(profileRequest.getPhoneNumber())
                .socialContact(profileRequest.getSocialContact())
                .avatar(profileRequest.getAvatar())


                .build();
    }

}
