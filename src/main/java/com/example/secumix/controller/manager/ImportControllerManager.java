package com.example.secumix.controller.manager;


import com.example.secumix.Utils.DtoMapper.ImportDetailMapper;
import com.example.secumix.constants.Constants;
import com.example.secumix.entities.*;
import com.example.secumix.exception.CustomException;
import com.example.secumix.ResponseObject;
import com.example.secumix.Utils.UserUtils;
import com.example.secumix.payload.Pagination;
import com.example.secumix.payload.request.ImportEditRequest;
import com.example.secumix.payload.response.ImportResponse;
import com.example.secumix.payload.response.OrderDetailResponse;
import com.example.secumix.repository.ImportDetailRepo;
import com.example.secumix.repository.ProductRepo;
import com.example.secumix.repository.ProductTypeRepo;
import com.example.secumix.services.IImportDetailService;
import com.example.secumix.services.IStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/management")
@RequiredArgsConstructor
public class ImportControllerManager {
    private final ImportDetailMapper importDetailMapper;
    @Value("${default_avt}")
    private String defaultAvt;

    private final ProductRepo productRepo;
    private final IImportDetailService importDetailService;
    private final ImportDetailRepo importDetailRepo;
    private final IStoreService storeService;
    private final ProductTypeRepo productTypeRepo;

    @GetMapping(value = "/{storeid}/import/view")
    public ResponseEntity<ResponseObject> getAllImport(
            @PathVariable int storeid,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = Constants.PAGE) int page,
            @RequestParam(defaultValue = Constants.SIZE) int size) {

        try {
            storeService.checkStoreAuthen(storeid);
            Page<ImportDetail> importDetails;

            if (keyword == null) {
                importDetails = importDetailService.findAllImportPaginable(storeid, page, size);
            } else {
                importDetails = importDetailService.findImportByTitleContainingIgnoreCase(keyword, storeid, page, size);
            }

            List<ImportResponse> importResponses = importDetails.getContent().stream()
                    .map(importDetailMapper::convertToImportResponse)
                    .collect(Collectors.toList());

            Pagination pagination = new Pagination(
                    importDetails.getTotalElements(),
                    importDetails.getTotalPages(),
                    importDetails.getNumber() + 1,
                    importDetails.getSize()
            );

            ResponseObject responseObject = new ResponseObject(
                    "OK",
                    "Các hóa đơn nhập hàng của bạn",
                    importResponses,
                    pagination
            );

            return ResponseEntity.status(HttpStatus.OK).body(responseObject);

        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }

    @PutMapping(value = "/{storeid}/import/edit/{importid}")
    ResponseEntity<ResponseObject> importEdit(@PathVariable int importid,
                                              @PathVariable int storeid,
                                              @ModelAttribute ImportEditRequest importEditRequest){
        try{
            importEditRequest.setImportDetailId(importid);
            if (importEditRequest.getQuantity() < 0)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        new ResponseObject("FAILED", "Quantity must >= 0", "")
                );
            if (importEditRequest.getPrice() < 0)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        new ResponseObject("FAILED", "Price must >= 0", "")
                );


            ImportResponse updatedImportDetail = importDetailService.updateImport(importEditRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Thành Công",updatedImportDetail)
            );
        }catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }

    }

    @PostMapping(value = "/{storeid}/import/insert")
    ResponseEntity<ResponseObject> importInsert(@RequestParam String productname,
                                                @RequestParam long priceIn,
                                                @RequestParam(required = false) Long priceOut,
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
            Store store = storeService.findStoreById(storeid).orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND,"Khong tim thay cua hang"));
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
                    .store(store)
                    .view(0)
                    .status(Product.ChannelStatus.UNSELL.getValue())
                    .price(priceOut)
                    .createdAt(UserUtils.getCurrentDay())
                    .updatedAt(UserUtils.getCurrentDay())
                    .productName(productname)
                    .description(productname + "--" + store.getStoreName())
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
