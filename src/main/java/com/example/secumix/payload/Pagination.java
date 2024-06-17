package com.example.secumix.payload;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Pagination {
    @SerializedName("totalItems")
    private long totalItems;

    @SerializedName("totalPages")
    private int totalPages;

    @SerializedName("currentPage")
    private int currentPage;

    @SerializedName("pageSize")
    private int pageSize;

    public Pagination(long totalItems, int totalPages, int currentPage, int pageSize) {
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }
}