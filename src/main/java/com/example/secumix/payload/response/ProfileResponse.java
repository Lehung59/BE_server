package com.example.secumix.payload.response;

import lombok.Data;


import javax.validation.constraints.Pattern;
@Data
public class ProfileResponse {
    private int userId;
    private String firstname;
    private String lastname;

    private String address;

    @Pattern(regexp = "0\\d{9}", message = "Invalid phone number format. Should start with 0 and have 10 digits.")
    private String phoneNumber;

    private String socialContact;

    private String avatar;
}
