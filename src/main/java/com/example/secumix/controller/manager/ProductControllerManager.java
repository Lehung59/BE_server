package com.example.secumix.controller.manager;


import com.example.secumix.constants.Constants;
import com.example.secumix.payload.Pagination;
import com.example.secumix.repository.*;
import com.example.secumix.exception.CustomException;
import com.example.secumix.ResponseObject;
import com.example.secumix.Utils.DtoMapper.ProductMapper;
import com.example.secumix.Utils.ImageUpload;
import com.example.secumix.Utils.UserUtils;
import com.example.secumix.entities.Product;
import com.example.secumix.entities.ProductImage;
import com.example.secumix.entities.ProductType;
import com.example.secumix.payload.request.AddProductRequest;
import com.example.secumix.payload.request.ProductRequest;
import com.example.secumix.payload.response.ProductResponse;
import com.example.secumix.services.IProductService;
import com.example.secumix.services.IProductTypeService;
import com.example.secumix.services.IStoreService;
import com.example.secumix.services.impl.ProductService;
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
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/management")
@RequiredArgsConstructor
public class ProductControllerManager {
    private final StoreRepo storeRepo;
    private final ProductMapper productMapper;
    private final ProductRepo productRepo;
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

            Product product = productService.findById(productId);


            String avtUrl = image != null ? imageUpload.upload(image) : defaultAvt;
            ProductImage productImage = ProductImage.builder()
                    .imageProduct(avtUrl)
                    .status(Integer.parseInt(status))
                    .title(title)
                    .product(product)
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
    public ResponseEntity<ResponseObject> getAllProductByStore(
            @PathVariable int storeid,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = Constants.PAGE) int page,
            @RequestParam(defaultValue = Constants.SIZE) int size) {

        try {
            Page<Product> products;

            if (keyword == null) {
                products = productService.findAllProductPaginable(storeid, page, size);
            } else {
                products = productService.findByTitleContainingIgnoreCase(keyword, storeid, page, size);
            }

            List<ProductResponse> productResponses = products.getContent().stream()
                    .map(ProductService::convertToProductResponse)
                    .collect(Collectors.toList());

            Pagination pagination = new Pagination(
                    products.getTotalElements(),
                    products.getTotalPages(),
                    products.getNumber() + 1,
                    products.getSize()
            );

            ResponseObject responseObject = new ResponseObject(
                    "OK",
                    "Cac san pham trong cua hang cua ban.",
                    productResponses,
                    pagination
            );

            return ResponseEntity.status(HttpStatus.OK).body(responseObject);

        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }

    @GetMapping(value = "/{storeid}/product/delete")
    ResponseEntity<ResponseObject> deleteProductByStore(@PathVariable int storeid,
                                                        @RequestParam int productId) {
        try {
            storeService.checkStoreAuthen(storeid);
            Optional<Product> product = productRepo.findById(productId);
            if (product.isEmpty() || product.get().getStore().getStoreId() != storeid)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("FAILED", "Khong tim thay san pham trong cua hang", "")
                );
            productService.deleteProduct(productId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Xoa thanh cong.", "")
            );


        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }

    }

    @PutMapping(value = "/{storeid}/product/edit")
    ResponseEntity<ResponseObject> editProduct(@PathVariable int storeid,
                                               @RequestParam int productId,
                                               @RequestParam(required = false) MultipartFile avatar,
                                               @RequestParam(required = false) String name,
                                               @RequestParam(required = false) String description,
                                               @RequestParam(required = false) Integer productTypeId,
                                               @RequestParam(required = false) Long price,
                                               @RequestParam(required = false) Integer discount,
                                               @RequestParam(required = false) Boolean status,
                                               @RequestParam(required = false) Integer quantity
    ) {

        try {
            storeService.checkStoreAuthen(storeid);
            Product product = productService.findById(productId);
            if (product.getStore().getStoreId() != storeid)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("FAILED", "Khong tim thay san pham trong cua hang", "")
                );
            String avrUrl = product.getAvatarProduct();
            if (avatar != null) {
                avrUrl = imageUpload.upload(avatar);
            }
            ProductRequest productRequest = productMapper.toProductRequest(product);
            if (name != null) productRequest.setProductName(name);
            if (description != null) productRequest.setDescription(description);
            if (productTypeId != null) productRequest.setProductTypeId(productTypeId);
            if (quantity != null) productRequest.setQuantity(quantity);
            if (price != null) productRequest.setPrice(price);
            if (discount != null) productRequest.setDiscount(discount);
            if (avrUrl != null) productRequest.setAvatar(avrUrl);
            if (status != null)
            {
                if(status){
                    productRequest.setStatus(Product.ChannelStatus.SELL.getValue());
                } else productRequest.setStatus(Product.ChannelStatus.UNSELL.getValue());

            }
            productService.updateProduct(productRequest);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Sua thanh cong.", "")
            );
        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }


}
