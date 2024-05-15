package com.example.secumix.security.store.controller;

import com.cloudinary.Cloudinary;
import com.example.secumix.security.ResponseObject;


import com.example.secumix.security.notify.Notify;
import com.example.secumix.security.notify.NotifyRepository;
import com.example.secumix.security.store.model.entities.Store;
import com.example.secumix.security.store.model.entities.StoreType;
import com.example.secumix.security.store.repository.StoreRepo;
import com.example.secumix.security.store.repository.StoreTypeRepo;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class StoreController {
    @Autowired
    private StoreTypeRepo storeTypeRepo;

    //--------------------------------admin



    @PostMapping(value = "/admin/storetype/insert")
    public ResponseEntity<ResponseObject> insertStoreType(@RequestParam String name
    ){
        Optional<StoreType> store = storeTypeRepo.findByName(name);
        if(store.isPresent()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED","Storetype exsis","")
            );
        }
        StoreType storeType = StoreType.builder()
                .storeTypeName(name.toUpperCase())
                .build();
        storeTypeRepo.save(storeType);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Success","")
        );
    }
}
