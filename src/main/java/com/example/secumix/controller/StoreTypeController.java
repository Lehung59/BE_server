package com.example.secumix.controller;

import com.example.secumix.exception.CustomException;
import com.example.secumix.ResponseObject;
import com.example.secumix.services.IStoreTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StoreTypeController {
    private final IStoreTypeService storeTypeService;
    @PreAuthorize("permitAll()")
    @GetMapping("/auth/storetype/view")
    ResponseEntity<ResponseObject> getAllStoreType() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Thành công", storeTypeService.getAll())
            );
        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }
}
