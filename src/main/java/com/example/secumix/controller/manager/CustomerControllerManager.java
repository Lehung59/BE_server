package com.example.secumix.controller.manager;

import com.example.secumix.ResponseObject;
import com.example.secumix.payload.response.OrderDetailResponse;
import com.example.secumix.payload.response.StoreCustomerRespone;
import com.example.secumix.services.ICustomerService;
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
public class CustomerControllerManager {
    private final IOrderDetailService orderDetailService;
    private final ICustomerService customerService;




    @GetMapping(value = "/{storeid}/customer/view")
    ResponseEntity<ResponseObject> viewCustomerByStore(@PathVariable("storeid") int storeid,
                                                       @RequestParam(required = false) String keyword
    ) {
        List<StoreCustomerRespone> storeCustomerRespones = new ArrayList<StoreCustomerRespone>();


        if (keyword == null) {
            storeCustomerRespones = customerService.findAllCustomerPaginable(storeid);
        } else {
            storeCustomerRespones = customerService.findCustomerByTitleContainingIgnoreCase(keyword, storeid);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Cac khach hang cua ban.", storeCustomerRespones)
        );
    }

    @GetMapping(value = "/{storeid}/customer/{customerid}/detail")
    ResponseEntity<ResponseObject> viewCustomerOrderListByStore(@PathVariable("storeid") int storeid,
                                                                @PathVariable("customerid") int customerid,
                                                                @RequestParam(required = false) String keyword) {
        List<OrderDetailResponse> orderDetailResponses = new ArrayList<OrderDetailResponse>();

        if (keyword == null) {
            orderDetailResponses = orderDetailService.findAllOrderByCustomerAndStorePaginable(storeid,customerid);
        } else {
            orderDetailResponses = orderDetailService.findOrderByTitleContainingIgnoreCase(keyword, storeid,customerid);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Các đơn hàng của khách "+customerid, orderDetailResponses)
        );
    }

}
