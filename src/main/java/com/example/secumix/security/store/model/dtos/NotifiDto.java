package com.example.secumix.security.store.model.dtos;


import lombok.Data;

@Data
public class NotifiDto {

    private int notiId;
    private String description;
    private boolean notiStatus;
    private boolean deletedNoti;
    private int userId;
}
