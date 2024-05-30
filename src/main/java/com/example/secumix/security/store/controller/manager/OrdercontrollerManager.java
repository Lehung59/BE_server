package com.example.secumix.security.store.controller.manager;

import com.example.secumix.security.Exception.CustomException;
import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.store.model.dtos.OrderDetailDto;
import com.example.secumix.security.store.model.response.OrderDetailResponse;
import com.example.secumix.security.store.model.response.ProductResponse;
import com.example.secumix.security.store.services.IOrderDetailService;
import com.example.secumix.security.store.services.impl.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/management")
@RequiredArgsConstructor

public class OrdercontrollerManager {
    private final IOrderDetailService orderDetailService;

    @GetMapping(value = "/{storeid}/order/view")
    ResponseEntity<ResponseObject> GetAllOrderDetailByStore(@PathVariable int storeid,
                                                        @RequestParam(required = false) String keyword,
                                                        @RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int size){

        List<OrderDetailResponse> orderDetailResponses = new ArrayList<OrderDetailResponse>();
        Pageable paging = PageRequest.of(page - 1, size);

        Page<OrderDetailResponse> pageTuts;
        if (keyword == null) {
            pageTuts = orderDetailService.findAllOrderPaginable(paging,storeid);
        } else {
            pageTuts = orderDetailService.findByTitleContainingIgnoreCase(keyword, paging, storeid);
        }

        orderDetailResponses = pageTuts.getContent();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Cac san pham trong cua hang cua ban.",orderDetailResponses)
        );
    }

    @GetMapping(value = "/{storeid}/orderdetail/view/{orderDetailId}")
    ResponseEntity<ResponseObject> GetAllOrderDetailByStore(@PathVariable int orderDetailId,
                                                            @PathVariable int storeid){


        try{

            OrderDetailDto orderDetailDto = orderDetailService.findDtoById(orderDetailId,storeid);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Chi tiet don hang "+orderDetailId,orderDetailDto)
            );
        }catch (CustomException ex){
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }

    }


}
