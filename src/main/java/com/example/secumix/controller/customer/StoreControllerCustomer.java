package com.example.secumix.controller.customer;

import com.example.secumix.Utils.DtoMapper.ProductMapper;
import com.example.secumix.Utils.DtoMapper.StoreMapper;
import com.example.secumix.constants.Constants;
import com.example.secumix.entities.Product;
import com.example.secumix.entities.Store;
import com.example.secumix.exception.CustomException;
import com.example.secumix.ResponseObject;
import com.example.secumix.Utils.UserUtils;
import com.example.secumix.payload.Pagination;
import com.example.secumix.payload.response.ProductResponse;
import com.example.secumix.payload.response.StoreFavorRespone;
import com.example.secumix.payload.response.StoreInfoView;
import com.example.secumix.services.IStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/customer")

public class StoreControllerCustomer {

    private final IStoreService storeService;
    private final UserUtils userUtils;
    private final ProductMapper productMapper;
    private final StoreMapper storeMapper;


    @GetMapping(value = "/{storeid}/info/view")
    public ResponseEntity<ResponseObject> viewStoreInfo(@PathVariable int storeid) {
        try{
            StoreInfoView storeInfoView = storeService.getInfo(storeid);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Success", storeInfoView)
            );
        } catch (CustomException ex){
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }

    @GetMapping(value = "/store/view/{storeid}")
    public ResponseEntity<ResponseObject> viewProduct(@RequestParam(required = false) String keyword,
                                                      @PathVariable int storeid,
                                                      @RequestParam(defaultValue = Constants.PAGE) int page,
                                                      @RequestParam(defaultValue = Constants.SIZE) int size) {
        try {
            Page<Product> products = storeService.findSellingProduct(storeid, keyword, page, size);

            List<ProductResponse> productResponseList = products.getContent().stream()
                    .map(productMapper::convertToProductResponse)
                    .collect(Collectors.toList());

            Pagination pagination = new Pagination(
                    products.getTotalElements(),
                    products.getTotalPages(),
                    products.getNumber() + 1,
                    products.getSize()
            );

            ResponseObject responseObject = new ResponseObject(
                    "OK",
                    "Các sản phẩm của cửa hàng id " + storeid,
                    productResponseList,
                    pagination
            );

            return ResponseEntity.status(HttpStatus.OK).body(responseObject);
        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }

    @GetMapping(value = "/favor/view")
    public ResponseEntity<ResponseObject> viewFavor(@RequestParam(required = false) String keyword,
                                                    @RequestParam(defaultValue = Constants.PAGE) int page,
                                                    @RequestParam(defaultValue = Constants.SIZE) int size) {
        try {
            int userId = userUtils.getUserId();
            Page<Store> stores = storeService.findFavorStore(userId, keyword, page, size);

            List<StoreInfoView> storeFavorResponses = stores.getContent().stream()
                    .map(storeMapper::convertToStoreInfoView)
                    .collect(Collectors.toList());

            Pagination pagination = new Pagination(
                    stores.getTotalElements(),
                    stores.getTotalPages(),
                    stores.getNumber() + 1,
                    stores.getSize()
            );

            ResponseObject responseObject = new ResponseObject(
                    "OK",
                    "Các cửa hàng yêu thích của bạn",
                    storeFavorResponses,
                    pagination
            );

            return ResponseEntity.status(HttpStatus.OK).body(responseObject);
        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }

    @DeleteMapping(value = "/favor/remove")
    ResponseEntity<ResponseObject> removeFavor(@RequestParam int  storeid){
        try{
            int userId = userUtils.getUserId();

            storeService.removeStoreFromFavorList(storeid,userId);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                    "OK", "Da xoa thanh cong", ""
            ));
        }catch (CustomException ex){
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


