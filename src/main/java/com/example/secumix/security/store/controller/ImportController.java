package com.example.secumix.security.store.controller;

import com.cloudinary.Cloudinary;
import com.example.secumix.security.Exception.CustomException;
import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.Utils.UserUtils;
import com.example.secumix.security.store.model.entities.ImportDetail;
import com.example.secumix.security.store.model.entities.Product;
import com.example.secumix.security.store.model.entities.ProductType;
import com.example.secumix.security.store.model.entities.Store;
import com.example.secumix.security.store.model.response.ImportResponse;
import com.example.secumix.security.store.repository.*;
import com.example.secumix.security.store.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/api/v1")
public class ImportController {

    @Value("${default_avt}")
    private String defaultAvt;

    @Autowired
    private IProductTypeService productTypeService;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ProductImageRepo productImageRepo;
    @Autowired
    private IImportDetailService importDetailService;
    @Autowired
    private ImportDetailRepo importDetailRepo;

    @Autowired
    private IStoreService storeService;
    @Autowired
    private IStoreTypeService storeTypeService;
    @Autowired
    private ProductTypeRepo productTypeRepo;

    @Autowired
    IProductService productService;

    public Map upload(MultipartFile file) {
        try {
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            return data;
        } catch (IOException io) {
            throw new RuntimeException("Image upload fail");
        }
    }
    @GetMapping(value = "/management/{storeid}/import/view")
    ResponseEntity<ResponseObject> getAllImport(@PathVariable int storeid,
                                                @RequestParam(required = false) String keyword,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int size){
        try{
            storeService.checkStoreAuthen(storeid);
            List<ImportResponse> importResponses = new ArrayList<ImportResponse>();
            Pageable paging = PageRequest.of(page - 1, size);
            Page<ImportResponse> pageTuts;
            if (keyword == null) {
                pageTuts = importDetailService.findAllImportPaginable(paging,storeid);
            } else {
                pageTuts = importDetailService.findImportByTitleContainingIgnoreCase(keyword, paging, storeid);
            }

            importResponses = pageTuts.getContent();

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Các hóa đơn nhập hàng của bạn",importResponses)
            );


        }catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }

    }
//
//
//    @GetMapping(value = "/import/{storeid}/getall")
//    ResponseEntity<ResponseObject> GetAllImport(@PathVariable int storeid) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName();
//        Optional<Store> store = storeService.findStoreById(storeid);
//        if (store.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                    new ResponseObject("OK", "Không có cửa hàng này.", "")
//            );
//        }
//        if (!email.equals(store.get().getEmailmanager())) {
//            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
//                    new ResponseObject("OK", "Không phải cửa hàng của bạn.", "")
//            );
//        }
//        List<ImportResponse> importResponses = importDetailService.findByStore(storeid).stream().map(
//                importDetail -> {
//                    ImportResponse importResponse = new ImportResponse();
//                    importResponse.setImportDetailId(importDetail.getImportDetailId());
//                    importResponse.setQuantity(importDetail.getQuantity());
//                    importResponse.setPrice(importDetail.getPrice());
//                    importResponse.setProductName(importDetail.getProduct().getProductName());
//                    importResponse.setStoreName(store.get().getStoreName());
//                    importResponse.setCreatedAt(importDetail.getCreatedAt());
//                    importResponse.setPriceTotal(importDetail.getPriceTotal());
//                    return importResponse;
//                }
//        ).collect(Collectors.toList());
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("OK", "Thành công.", importResponses)
//        );
//    }
//
//    @GetMapping(value = "/import/{storeid}/{productname}")
//    ResponseEntity<ResponseObject> GetAllImport(@PathVariable int storeid,
//                                                @PathVariable String productname) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName();
//        Optional<Store> store = storeService.findStoreById(storeid);
//        Optional<Product> product = productRepo.findByName(storeid, productname);
//
//        if (store.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                    new ResponseObject("OK", "Không có cửa hàng này.", "")
//            );
//        }
//        if (!email.equals(store.get().getEmailmanager())) {
//            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
//                    new ResponseObject("OK", "Không phải cửa hàng của m.", "")
//            );
//        }
//        if (product.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                    new ResponseObject("OK", "Không có sản phẩm này.", "")
//            );
//        }
//        List<ImportResponse> importResponses = importDetailService.findByStoreandProduct(storeid, productname).stream().map(
//                importDetail -> {
//                    ImportResponse importResponse = new ImportResponse();
//                    importResponse.setImportDetailId(importDetail.getImportDetailId());
//                    importResponse.setQuantity(importDetail.getQuantity());
//                    importResponse.setPrice(importDetail.getPrice());
//                    importResponse.setProductName(importDetail.getProduct().getProductName());
//                    importResponse.setStoreName(store.get().getStoreName());
//                    importResponse.setCreatedAt(importDetail.getCreatedAt());
//                    importResponse.setPriceTotal(importDetail.getPriceTotal());
//                    return importResponse;
//                }
//        ).collect(Collectors.toList());
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("OK", "Thành công.", importResponses)
//        );
//    }

    @PostMapping(value = "/management/{storeid}/import/insert")
    ResponseEntity<ResponseObject> importInsert(@RequestParam String productname,
                                                @RequestParam int priceIn,
                                                @RequestParam(required = false) Integer priceOut,
                                                @RequestParam(required = false) String producttypename,
                                                @RequestParam int quantity,
                                                @PathVariable int storeid) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();

            if (quantity <= 0)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        new ResponseObject("FAILED", "Quantity must > 0", "")
                );

            storeService.checkStoreAuthen(storeid);
            Optional<Product> product = productRepo.findByName(storeid, productname);
            Optional<Store> store = storeService.findStoreById(storeid);
            Product finalProduct = new Product();
            if (product.isEmpty()) {
                if (priceOut == null) {
                    return ResponseEntity.badRequest().body(new ResponseObject("error", "Missing required priceOut for new product", null));
                }
                if (producttypename == null) {
                    return ResponseEntity.badRequest().body(new ResponseObject("error", "Missing required producttypename for new product", null));
                }
                Optional<ProductType> productType = productTypeRepo.findProductTypeByName(producttypename.toUpperCase(), storeid);
                if (productType.isEmpty())
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                            new ResponseObject("FAILED", "Producttype does not exsit", "")
                    );
            }
            ProductType productType = productTypeRepo.findProductTypeByName(producttypename.toUpperCase(), storeid).get();

            Product newObj = Product.builder()
                    .avatarProduct(defaultAvt)
                    .quantity(quantity)
                    .discount(0)
                    .productType(productType)
                    .store(store.get())
                    .view(0)
                    .status(0)
                    .price(priceOut)
                    .createdAt(UserUtils.getCurrentDay())
                    .updatedAt(UserUtils.getCurrentDay())
                    .productName(productname)
                    .description(productname + "--" + store.get().getStoreName())
                    .build();
            finalProduct = newObj;
            productRepo.save(newObj);

            if (product.isPresent()) {
                //trường hợp có product trong kho
                Product updateProduct = product.get();
                updateProduct.setQuantity(updateProduct.getQuantity() + quantity);
                updateProduct.setUpdatedAt(UserUtils.getCurrentDay());
                if (priceOut != null) {
                    updateProduct.setPrice(priceOut); // Cập nhật giá nếu được cung cấp
                }
                productRepo.save(updateProduct);
                finalProduct = product.get();
            }

            ImportDetail importDetail = ImportDetail.builder()
                    .product(finalProduct)
                    .updatedAt(UserUtils.getCurrentDay())
                    .createdAt(UserUtils.getCurrentDay())
                    .quantity(quantity)
                    .price(priceIn)
                    .priceTotal(quantity * priceIn)
                    .build();
            importDetailRepo.save(importDetail);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Thành Công", "")
            );
        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }

}
