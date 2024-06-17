package com.example.secumix.payload.dtos;

import lombok.Data;

@Data
public class PagingDto {
    private int page;
    private int size;
    private String keyword;
}
