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
                                                       @RequestParam(required = false) String keyword,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        List<StoreCustomerRespone> storeCustomerRespones = new ArrayList<StoreCustomerRespone>();
        Pageable paging = PageRequest.of(page - 1, size);

        Page<StoreCustomerRespone> pageTuts;
        if (keyword == null) {
            pageTuts = customerService.findAllCustomerPaginable(paging,storeid);
        } else {
            pageTuts = customerService.findCustomerByTitleContainingIgnoreCase(keyword, paging, storeid);
        }

        storeCustomerRespones = pageTuts.getContent();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Cac khach hang cua ban.", storeCustomerRespones)
        );
    }

    @GetMapping(value = "/{storeid}/customer/{customerid}/detail")
    ResponseEntity<ResponseObject> viewCustomerOrderListByStore(@PathVariable("storeid") int storeid,
                                                                @PathVariable("customerid") int customerid,
                                                                @RequestParam(required = false) String keyword,
                                                                @RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        List<OrderDetailResponse> orderDetailResponses = new ArrayList<OrderDetailResponse>();
        Pageable paging = PageRequest.of(page - 1, size);

        Page<OrderDetailResponse> pageTuts;
        if (keyword == null) {
            pageTuts = orderDetailService.findAllOrderByCustomerAndStorePaginable(paging,storeid,customerid);
        } else {
            pageTuts = orderDetailService.findOrderByTitleContainingIgnoreCase(keyword, paging, storeid,customerid);
        }

        orderDetailResponses = pageTuts.getContent();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Các đơn hàng của khách "+customerid, orderDetailResponses)
        );
    }

}