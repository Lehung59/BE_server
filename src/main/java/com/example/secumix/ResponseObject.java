package com.example.secumix;

import com.example.secumix.payload.Pagination;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.Setter;

@Data
public class ResponseObject {
    @Setter
    @SerializedName("status")
    private String status;
    @Setter
    @SerializedName("message")
    private String message;
    @Setter
    @SerializedName("data")
    private Object data;
    @SerializedName("pagination")
    private Pagination pagination;


    public Pagination getPagination() {
        return pagination;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public ResponseObject(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
    public ResponseObject(String status, String message, Object data, Pagination pagination) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.pagination = pagination;
    }
}
