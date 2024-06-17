package com.example.secumix.controller.customer;

import com.example.secumix.constants.Constants;
import com.example.secumix.exception.CustomException;
import com.example.secumix.ResponseObject;
import com.example.secumix.Utils.UserUtils;
import com.example.secumix.payload.response.ProductResponse;
import com.example.secumix.payload.response.StoreFavorRespone;
import com.example.secumix.payload.response.StoreInfoView;
import com.example.secumix.services.IStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/customer")

public class StoreControllerCustomer {

    private final IStoreService storeService;
    private final UserUtils userUtils;

    @GetMapping(value = "/store/view/{storeid}")
    ResponseEntity<ResponseObject> viewProduct(@RequestParam(required = false) String keyword,
                                               @PathVariable int storeid){
        try{
            List<ProductResponse> productResponseList = storeService.findSellingProduct(storeid,keyword);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                    "OK", "Cac san pham cuar cua hang id "+storeid, productResponseList
            ));

        } catch (CustomException ex){
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }

    @GetMapping(value = "/favor/view")
    ResponseEntity<ResponseObject> viewFavor(@RequestParam(required = false) String keyword,
                                             @RequestParam(defaultValue = Constants.PAGE) int page,
                                             @RequestParam(defaultValue = Constants.SIZE) int size
                                             ){
        try{
            int userId = userUtils.getUserId();

            List<StoreInfoView> storeFavorRespones = storeService.findFavorStore(userId,keyword,page,size);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                    "OK", "Cac cua hang yeu thich cua ban", storeFavorRespones
            ));
        } catch (CustomException ex){
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


