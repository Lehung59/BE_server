package com.example.secumix.security.store.controller;

import com.cloudinary.Cloudinary;
import com.example.secumix.security.Exception.CustomException;
import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.Utils.UserUtils;
import com.example.secumix.security.store.model.entities.Product;
import com.example.secumix.security.store.model.entities.ProductType;
import com.example.secumix.security.store.model.entities.Store;
import com.example.secumix.security.store.model.entities.StoreType;
import com.example.secumix.security.store.repository.*;
import com.example.secumix.security.store.services.IProductService;
import com.example.secumix.security.store.services.IProductTypeService;
import com.example.secumix.security.store.services.impl.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping(value = "/api/v1")

public class ProductTypeController {
    @Value("${default_avt}")
    private String defaultAvt;
    @Autowired
    private IProductTypeService productTypeService;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private StoreRepo storeRepo;
    @Autowired
    private StoreTypeRepo storeTypeRepo;
    @Autowired
    private ProductTypeRepo productTypeRepo;

    @Autowired
    IProductService productService;
    @Autowired
    private StoreService storeService;

    public Map upload(MultipartFile file) {
        try {
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            return data;
        } catch (IOException io) {
            throw new RuntimeException("Image upload fail");
        }
    }

    @GetMapping(value = "/management/{storeid}/producttype/getall")
    ResponseEntity<ResponseObject> getAllProductType(@PathVariable int storeid) {
        try {
            storeService.checkStoreAuthen(storeid);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Thành công", productTypeService.getAllProductType(storeid))
            );
        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }

    @GetMapping(value = "/management/{storeid}/producttype/{producttypeid}")
    ResponseEntity<ResponseObject> GetAllProductByType(@PathVariable int producttypeid,
                                                       @PathVariable int storeid) {
        try {
            storeService.checkStoreAuthen(storeid);
            Optional<ProductType> productType = productTypeRepo.findById(producttypeid);
            if (productType.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Thành công", productService.findByProductType(producttypeid))
                );
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("OK", "Không có đâu", "")
                );
            }
        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }


    @PostMapping(value = "/management/{storeid}/producttype/insert")
    ResponseEntity<ResponseObject> insertProductType(@RequestParam String name,
                                                     @RequestParam(required = false) MultipartFile avatar,
                                                     @PathVariable int storeid) {

        try{
            storeService.checkStoreAuthen(storeid);
            Optional<ProductType> RS = productTypeService.findProductTypeByName(name, storeid);
            if (RS.isEmpty()) {
                try {
                    String avturl = defaultAvt;

                    if (avatar != null) {
                        Map<String, Object> uploadResult = upload(avatar);
                        avturl = uploadResult.get("secure_url").toString();
                    }
                    ProductType productType = ProductType.builder()
                            .productTypeName(name)
                            .store(storeRepo.findStoreById(storeid).get())
                            .productTypeImg(avturl)
                            .createdAt(UserUtils.getCurrentDay())
                            .build();
                    productTypeService.Save(productType);

                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("OK", "Thành Công", "")
                    );
                } catch (RuntimeException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                            new ResponseObject("FAILED", "Upload không thành công", "")
                    );
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        new ResponseObject("FAILED", "đã tồn tại loại sản phẩm đó", "")
                );
            }}
        catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }


}
