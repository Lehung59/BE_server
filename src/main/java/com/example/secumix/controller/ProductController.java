package com.example.secumix.controller;

import com.example.secumix.entities.Product;
import com.example.secumix.exception.CustomException;
import com.example.secumix.payload.response.ProductResponse;
import com.example.secumix.repository.ImportDetailRepo;
import com.example.secumix.repository.ProductImageRepo;
import com.example.secumix.repository.ProductTypeRepo;
import com.example.secumix.repository.StoreTypeRepo;
import com.example.secumix.ResponseObject;
import com.example.secumix.Utils.ImageUpload;


import com.example.secumix.services.IProductService;
import com.example.secumix.services.IProductTypeService;
import com.example.secumix.services.IStoreService;
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

        try{

            ProductResponse product = productService.findbyId(productid);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Thành Công", product)
            );
        }catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
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
