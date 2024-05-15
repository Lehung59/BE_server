package com.example.secumix.security.userprofile;

public interface IProfileService {
    void UpdateProfile(ProfileRequest profileRequest,int userID);
    ProfileResponse findProfileByUserID(int userID);
}
