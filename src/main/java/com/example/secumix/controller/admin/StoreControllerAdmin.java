package com.example.secumix.controller.admin;

import com.example.secumix.exception.CustomException;
import com.example.secumix.ResponseObject;


import com.example.secumix.entities.StoreType;
import com.example.secumix.payload.request.StoreViewResponse;
import com.example.secumix.repository.StoreTypeRepo;

import com.example.secumix.services.IStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin")
public class StoreControllerAdmin {
    @Autowired
    private StoreTypeRepo storeTypeRepo;
    @Autowired
    private IStoreService storeService;

    //--------------------------------admin

    @GetMapping(value = "/store/view")
    public ResponseEntity<ResponseObject> viewStore(@RequestParam(required = false) String keyword,
                                                    @RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "10") int size ) {
        try{
            List<StoreViewResponse> storeViewRespones = new ArrayList<StoreViewResponse>();
            Pageable paging = PageRequest.of(page - 1, size);
            Page<StoreViewResponse> pageTuts;


            if (keyword == null) {
                pageTuts = storeService.findAllStorePaginable(paging);
            } else {
                pageTuts = storeService.findAllStoreByTitleContainingIgnoreCase(keyword, paging);
            }

            storeViewRespones = pageTuts.getContent();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Các cua hang hien tai",storeViewRespones)
            );


        }catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }


    }


    @PostMapping(value = "/storetype/insert")
    public ResponseEntity<ResponseObject> insertStoreType(@RequestParam String name
    ){
        Optional<StoreType> store = storeTypeRepo.findByName(name);
        if(store.isPresent()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED","Storetype exsis","")
            );
        }
        StoreType storeType = StoreType.builder()
                .storeTypeName(name.toUpperCase())
                .build();
        storeTypeRepo.save(storeType);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Success","")
        );
    }
}
