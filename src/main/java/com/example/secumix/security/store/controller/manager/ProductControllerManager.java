package com.example.secumix.security.store.controller.manager;


import com.example.secumix.security.Exception.CustomException;
import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.Utils.DtoMapper.ProductMapper;
import com.example.secumix.security.Utils.ImageUpload;
import com.example.secumix.security.Utils.UserUtils;
import com.example.secumix.security.store.model.entities.Product;
import com.example.secumix.security.store.model.entities.ProductImage;
import com.example.secumix.security.store.model.entities.ProductType;
import com.example.secumix.security.store.model.entities.Store;
import com.example.secumix.security.store.model.request.AddProductRequest;
import com.example.secumix.security.store.model.request.ProductRequest;
import com.example.secumix.security.store.model.response.ProductResponse;
import com.example.secumix.security.store.repository.*;
import com.example.secumix.security.store.services.IProductService;
import com.example.secumix.security.store.services.IProductTypeService;
import com.example.secumix.security.store.services.IStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/management")
@RequiredArgsConstructor
public class ProductControllerManager {
    private final StoreRepo storeRepo;
    private final ProductMapper productMapper;
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
    private final UserUtils userUtils;


    @PostMapping(value = "/{storeid}/product/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestParam(required = false) MultipartFile avatar,
                                                 @RequestParam String name,
                                                 @RequestParam String description,
                                                 @PathVariable int storeid,
                                                 @RequestParam String producttypename,
                                                 @RequestParam long price
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

    @PostMapping(value = "/{storeid}/productimage/insert")
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


    @GetMapping(value = "/{storeid}/product/view")
    ResponseEntity<ResponseObject>GetAllProductByStore(@PathVariable int storeid,
                                                       @RequestParam(required = false) String keyword,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size){

        List<ProductResponse> products = new ArrayList<ProductResponse>();
        Pageable paging = PageRequest.of(page - 1, size);

        Page<ProductResponse> pageTuts;
        if (keyword == null) {
            pageTuts = productService.findAllProductPaginable(paging,storeid);
        } else {
            pageTuts = productService.findByTitleContainingIgnoreCase(keyword, paging, storeid);
        }

        products = pageTuts.getContent();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Cac san pham trong cua hang cua ban.",products)
        );
    }
    @PutMapping(value = "/{storeid}/product/edit")
    ResponseEntity<ResponseObject>editProduct(@PathVariable int storeid,
                                              @RequestParam int productId,
                                              @RequestParam(required = false) MultipartFile avatar,
                                              @RequestParam(required = false) String name,
                                              @RequestParam(required = false) String description,
                                              @RequestParam(required = false) Integer productTypeId ,
                                              @RequestParam(required = false) Long price,
                                              @RequestParam(required = false) Integer discount,
                                              @RequestParam(required = false) Boolean status,
                                              @RequestParam(required = false) Integer quantity
    ){

        try{
            storeService.checkStoreAuthen(storeid);
            Optional<Product> product = productService.findById(productId);
            if (product.isEmpty() || product.get().getStore().getStoreId() != storeid) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Khong tim thay san pham trong cua hang", "")
            );
            String avrUrl = product.get().getAvatarProduct();
            if(avatar!=null){
                 avrUrl = imageUpload.upload(avatar);
            }
            ProductRequest productRequest = productMapper.toProductRequest(product.get());
            if(name!=null) productRequest.setProductName(name);
            if(description!=null) productRequest.setDescription(description);
            if(productTypeId!=null) productRequest.setProductTypeId(productTypeId);
            if(quantity!=null) productRequest.setQuantity(quantity);
            if(price!=null) productRequest.setPrice(price);
            if(discount!=null) productRequest.setDiscount(discount);
            if(avrUrl!=null) productRequest.setAvatar(avrUrl);
            if(status!=null) productRequest.setStatus(status);
            productService.updateProduct(productRequest);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Sua thanh cong.","")
            );
        }


        catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }




}
