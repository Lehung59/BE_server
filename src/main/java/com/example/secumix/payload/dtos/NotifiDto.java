package com.example.secumix.payload.dtos;


import lombok.Data;

@Data
public class NotifiDto {

    private int notiId;
    private String description;
    private boolean notiStatus;
    private boolean deletedNoti;
    private int userId;
}
