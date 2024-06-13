package com.example.secumix.services;

import com.example.secumix.payload.request.ProfileRequest;
import com.example.secumix.payload.response.ProfileResponse;

public interface IProfileService {
    ProfileResponse findProfileByUserID(int userID);

    void updateProfile(ProfileRequest profileRequest);
}
