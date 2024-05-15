package com.example.secumix.security.store.controller;

import com.example.secumix.security.ResponseObject;


import com.example.secumix.security.store.model.entities.OrderDetail;
import com.example.secumix.security.store.model.entities.OrderStatus;
import com.example.secumix.security.store.repository.OrderDetailRepo;
import com.example.secumix.security.store.repository.OrderStatusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class OrderStatusController {
    @Autowired
    private OrderStatusRepo orderStatusRepo;
    @Autowired
    private OrderDetailRepo orderDetailRepo;

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
