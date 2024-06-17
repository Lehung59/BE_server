package com.example.secumix.controller.manager;

import com.example.secumix.ResponseObject;
import com.example.secumix.Utils.DtoMapper.CustomerMapper;
import com.example.secumix.Utils.DtoMapper.OrderDetailMapper;
import com.example.secumix.constants.Constants;
import com.example.secumix.entities.OrderDetail;
import com.example.secumix.entities.ProfileDetail;
import com.example.secumix.payload.Pagination;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/management")
@RequiredArgsConstructor
public class CustomerControllerManager {
    private final IOrderDetailService orderDetailService;
    private final ICustomerService customerService;
    private final OrderDetailMapper orderDetailMapper;
    private final CustomerMapper customerMapper;




    @GetMapping(value = "/{storeid}/customer/view")
    public ResponseEntity<ResponseObject> viewCustomerByStore(@PathVariable("storeid") int storeid,
                                                              @RequestParam(required = false) String keyword,
                                                              @RequestParam(defaultValue = Constants.PAGE) int page,
                                                              @RequestParam(defaultValue = Constants.SIZE) int size) {
        Page<ProfileDetail> customers;
        if (keyword == null || keyword.isEmpty()) {
            customers = customerService.findAllCustomerPaginable(storeid, page, size);
        } else {
            customers = customerService.findCustomerByTitleContainingIgnoreCase(keyword, page, size, storeid);
        }

        List<StoreCustomerRespone> storeCustomerRespones = customers.getContent().stream()
                .map(customer -> customerMapper.convertToCustomerResponse(customer, storeid))
                .collect(Collectors.toList());

        Pagination pagination = new Pagination(
                customers.getTotalElements(),
                customers.getTotalPages(),
                customers.getNumber() + 1,
                customers.getSize()
        );

        ResponseObject responseObject = new ResponseObject(
                "OK",
                "Các khách hàng của bạn.",
                storeCustomerRespones,
                pagination
        );

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }


    @GetMapping(value = "/{storeid}/customer/{customerid}/detail")
    public ResponseEntity<ResponseObject> viewCustomerOrderListByStore(@PathVariable("storeid") int storeid,
                                                                       @PathVariable("customerid") int customerid,
                                                                       @RequestParam(required = false) String keyword,
                                                                       @RequestParam(defaultValue = Constants.PAGE) int page,
                                                                       @RequestParam(defaultValue = Constants.SIZE) int size) {
        Page<OrderDetail> orderDetails;
        if (keyword == null) {
            orderDetails = orderDetailService.findAllOrderByCustomerAndStorePaginable(storeid, customerid, page, size);
        } else {
            orderDetails = orderDetailService.findOrderByTitleContainingIgnoreCase(keyword, storeid, customerid, page, size);
        }

        List<OrderDetailResponse> orderDetailResponses = orderDetails.getContent().stream()
                .map(orderDetailMapper::convertToOrderDetailResponse)
                .collect(Collectors.toList());

        Pagination pagination = new Pagination(
                orderDetails.getTotalElements(),
                orderDetails.getTotalPages(),
                orderDetails.getNumber() + 1,
                orderDetails.getSize()
        );

        ResponseObject responseObject = new ResponseObject(
                "OK",
                "Các đơn hàng của khách " + customerid,
                orderDetailResponses,
                pagination
        );

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

}
