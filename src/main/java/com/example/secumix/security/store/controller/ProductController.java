package com.example.secumix.security.store.controller;

import com.cloudinary.Cloudinary;
import com.example.secumix.security.Exception.CustomException;
import com.example.secumix.security.Exception.ImageUploadException;
import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.Utils.ImageUpload;
import com.example.secumix.security.Utils.UserUtils;


import com.example.secumix.security.store.model.entities.*;
import com.example.secumix.security.store.model.request.AddProductRequest;
import com.example.secumix.security.store.services.IProductService;
import com.example.secumix.security.store.services.IProductTypeService;
import com.example.secumix.security.store.repository.*;
import com.example.secumix.security.store.services.IStoreService;
import lombok.RequiredArgsConstructor;
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


    @PostMapping(value = "/mamagement/{storeid}/productimage/insert")
    ResponseEntity<ResponseObject> insertProductImage(@RequestParam MultipartFile image,
                                                      @RequestParam String title,
                                                      @RequestParam String status,
                                                      @RequestParam int productId,
                                                      @PathVariable int storeid
    ) {
        try {
            storeService.checkStoreAuthen(storeid);

            Optional<Product> product = productService.findById(productId);
            if (product.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Cannot find product", "")
            );

            String avtUrl = image != null ? imageUpload.upload(image) : defaultAvt;
            ProductImage productImage = ProductImage.builder()
                    .imageProduct(avtUrl)
                    .status(Integer.parseInt(status))
                    .title(title)
                    .product(product.get())
                    .build();
            productImageRepo.save(productImage);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Thành Công", "")
            );
        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }


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

    @PostMapping(value = "/management/{storeid}/product/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestParam(required = false) MultipartFile avatar,
                                                 @RequestParam String name,
                                                 @RequestParam String description,
                                                 @PathVariable int storeid,
                                                 @RequestParam String producttypename,
                                                 @RequestParam int price
    ) {
        try {
            storeService.checkStoreAuthen(storeid);
            producttypename = producttypename.toUpperCase();
            Optional<ProductType> productType = productTypeRepo.findProductTypeByName(producttypename, storeid);
            if (productType.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("FAILED", "Không tìm thấy kiểu sản phẩm " + producttypename, "")
                );
            }
            Optional<Product> product = productService.findByName(storeid, name);
            if (product.isPresent())
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        new ResponseObject("FAILED", "Product name is present", "")
                );

            String avtUrl = avatar != null ? imageUpload.upload(avatar) : defaultAvt;
            AddProductRequest addProductRequest = new AddProductRequest();
            addProductRequest.setName(name);
            addProductRequest.setDescription(description);
            addProductRequest.setProducttypename(producttypename);
            addProductRequest.setStoreId(storeid);
            addProductRequest.setAvatar(avtUrl);
            addProductRequest.setPrice(price);
            productService.saveProduct(addProductRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Thành Công", name)
            );

        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }

}
