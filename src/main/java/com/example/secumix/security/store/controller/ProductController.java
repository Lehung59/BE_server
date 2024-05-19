package com.example.secumix.security.store.controller;

import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.Utils.ImageUpload;


import com.example.secumix.security.store.model.entities.*;
import com.example.secumix.security.store.services.IProductService;
import com.example.secumix.security.store.services.IProductTypeService;
import com.example.secumix.security.store.repository.*;
import com.example.secumix.security.store.services.IStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
public class ProductController {

    @Value("${default_avt}")
    private String defaultAvt;
    private final ImageUpload imageUpload;


    private final IProductTypeService productTypeService;
    private final IProductService productService;
    private final ProductImageRepo productImageRepo;
    private final ImportDetailRepo importDetailRepo;

    private final IStoreService storeService;
    private final StoreTypeRepo storeTypeRepo;
    private final ProductTypeRepo productTypeRepo;

//    public Map upload(MultipartFile file)  {
//        try{
//            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
//            return data;
//        }catch (IOException io){
//            throw new RuntimeException("Image upload fail");
//        }
//    }
//
//


    @GetMapping(value = "/product/info/{productid}")
    ResponseEntity<ResponseObject> GetInfoProduct(@PathVariable int productid) {
        Optional<Product> product = productService.findById(productid);
        if (product.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Thành Công", productService.findbyId(productid))
            );
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Không tồn tại sản phẩm trên", "")
            );
        }
    }



//    @PutMapping(value = "")

    @PostMapping(value = "/search")
    ResponseEntity<ResponseObject> SearchByKey(@RequestParam String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Tim kiem thanh cong", productService.SearchByKey(keyword))
        );
    }

//    @PostMapping(value = "//management/product/{storeid}/edit/{productid}")
//    ResponseEntity<ResponseObject> editProduct(@RequestParam(required = false) MultipartFile avatar,
//                                                @RequestParam(required = false) String name,
//                                                @RequestParam(required = false) String description,
//                                                @PathVariable(required = false) int storeid,
//                                                @RequestParam(required = false) String producttypename,
//                                                @RequestParam(required = false) int price,
//                                               @RequestParam(required = false) boolean status,
//                                               @RequestParam(required = false) int discount,
//                                               @RequestParam(required = false) int quantity
//                                               ){
//        String avtUrl = "";
//        try{
//        if (avatar != null) {
//            Map<String, Object> uploadResult = upload(avatar);
//            avtUrl = uploadResult.get("secure_url").toString();
//        }}catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                    new ResponseObject("FAILED", "Upload không thành công", "")
//            );
//        }
//
//
//    }


}
