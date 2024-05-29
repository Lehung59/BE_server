package com.example.secumix.security.userprofile;

public interface IProfileService {
    ProfileResponse findProfileByUserID(int userID);

    void updateProfile(ProfileRequest profileRequest);
}
