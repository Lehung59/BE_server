package com.example.secumix.controller.manager;

import com.example.secumix.exception.CustomException;
import com.example.secumix.ResponseObject;
import com.example.secumix.payload.dtos.OrderDetailDto;
import com.example.secumix.payload.response.OrderDetailResponse;
import com.example.secumix.services.IOrderDetailService;
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
                                                        @RequestParam (value = "keyword", required = false) String keyword,
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

            OrderDetailDto orderDetailDto = orderDetailService.findDtoById(orderDetailId);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Chi tiet don hang "+orderDetailId,orderDetailDto)
            );
        }catch (CustomException ex){
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }

    }


}
