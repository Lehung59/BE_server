package com.example.secumix.controller;

import com.example.secumix.ResponseObject;


import com.example.secumix.entities.OrderDetail;
import com.example.secumix.entities.OrderStatus;
import com.example.secumix.repository.OrderDetailRepo;
import com.example.secumix.repository.OrderStatusRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderStatusController {
    private final OrderStatusRepo orderStatusRepo;
    private final OrderDetailRepo orderDetailRepo;

    @PostMapping(value = "/management/orderstatus/change")
    ResponseEntity<ResponseObject> changeOrderStatus( @RequestParam int orderId,
                                                     @RequestParam int orderStatusId
                                                     ){
        Optional<OrderStatus> orderStatus= orderStatusRepo.findById(orderStatusId);
        if(orderStatus.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED","Orderstatus not found","")
            );
        }
        Optional<OrderDetail> orderDetail= orderDetailRepo.findById(orderId);
        if(orderDetail.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED","Order not found","")
            );
        }
        OrderDetail newobj = orderDetail.get();
        newobj.setOrderStatus(orderStatus.get());
        orderDetailRepo.save(newobj);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Thêm thành công","")
        );
    }
}
