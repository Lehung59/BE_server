package com.example.secumix.security.store.controller.customer;

import com.cloudinary.Cloudinary;
import com.example.secumix.security.Exception.CustomException;
import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.Utils.ImageUpload;
import com.example.secumix.security.Utils.UserUtils;
import com.example.secumix.security.notify.NotifyRepository;
import com.example.secumix.security.store.model.dtos.StoreDto;
import com.example.secumix.security.store.repository.*;
import com.example.secumix.security.store.services.ICustomerService;
import com.example.secumix.security.store.services.IOrderDetailService;
import com.example.secumix.security.store.services.IProductService;
import com.example.secumix.security.store.services.IStoreService;
import com.example.secumix.security.user.UserRepository;
import com.example.secumix.security.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/customer")

public class StoreControllerCustomer {
    private final IProductService productService;
    private final ImportDetailRepo importDetailRepo;
    private final StoreRepo storeRepo;
    private final OrderDetailRepo orderDetailRepo;
    private final IOrderDetailService orderDetailService;
    private final ProductRepo productRepo;
    private final StoreTypeRepo storeTypeRepo;
    private final NotifyRepository notifyRepository;
    private final Cloudinary cloudinary;
    private final UserRepository userRepository;
    private final ICustomerService customerService;
    private final ProfileDetailRepo profileDetailRepo;
    private final IStoreService storeService;
    private final ImageUpload imageUpload;
    private final UserUtils userUtils;
    private final UserService userService;

    @GetMapping(value = "/favor/view")
    ResponseEntity<ResponseObject> viewFavor(@RequestParam(required = false) String keyword,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size){
        try{
            int userId = userUtils.getUserId();

            Page<StoreDto> storeDtoList = storeService.viewFavor(userId,keyword,page,size);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                    "OK", "Cac cua hang yeu thich cua ban", storeDtoList.getContent()
            ));
        } catch (CustomException ex){
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }


    @PostMapping(value = "/favor/add")
    ResponseEntity<ResponseObject> addToFavor(@RequestParam int storeid) {
        try {
            int id = userUtils.getUserId();
            storeService.addStoreToFavor(id, storeid);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                    "OK", "Them thanh cong cua hang vao danh sach yeu thich ", ""
            ));
        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }

}


