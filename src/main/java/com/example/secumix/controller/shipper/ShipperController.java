package com.example.secumix.controller.shipper;

import com.example.secumix.exception.CustomException;
import com.example.secumix.ResponseObject;
import com.example.secumix.Utils.UserUtils;
import com.example.secumix.entities.OrderDetail;
import com.example.secumix.payload.response.OrderDetailResponse;
import com.example.secumix.services.IOrderDetailService;
import com.example.secumix.repository.OrderDetailRepo;
import com.example.secumix.entities.User;
import com.example.secumix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/shipper")
public class ShipperController {
    @Autowired
    private IOrderDetailService orderService;
    @Autowired
    private OrderDetailRepo orderDetailRepo;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserUtils userUtils;

    @GetMapping(value = "/orderdetail")
    ResponseEntity<ResponseObject> viewOrderDetail(@RequestParam(name = "orderid") int orderId){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Chi tiet don hang",orderService.findDtoById(orderId))
            );


        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }

    @GetMapping(value = "/changestatus1/{orderdetailid}")
    ResponseEntity<ResponseObject> Changestatus1(@PathVariable int orderdetailid){
        Optional<OrderDetail> orderDetail= orderDetailRepo.findById(orderdetailid);
        if (orderDetail.isPresent()){
            if (orderDetail.get().getShipperid()==null){
                orderService.ChangeStatus1(orderdetailid);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK","Nhận đơn thành công","")
                );
            }else {
                if(orderDetail.get().getOrderStatus().getOrderStatusId() == 4){
                    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                            new ResponseObject("NOT IMPLEMENT","Don hang da bi huy","")
                    );
                }
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("NOT FOUND","Đã có người nhận đơn này","")
                );
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("NOT FOUND","Khong tim thay","")
        );
    }
    @GetMapping(value = "/changestatus2/{orderdetailid}")
    ResponseEntity<ResponseObject> Changestatus2(@PathVariable int orderdetailid){
        Optional<OrderDetail> orderDetail= orderDetailRepo.findById(orderdetailid);
        if (orderDetail.isPresent()){
            if (orderDetail.get().getShipperid()!=null){
                orderService.ChangeStatus2(orderdetailid);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK","Giao hàng thành công","")
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("NOT FOUND","Đã có người nhận ffown này","")
                );
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("NOT FOUND","Khong tim thay","")
        );
    }
    @GetMapping(value = "/orderlist/view")
    ResponseEntity<ResponseObject> orderlistview(){
        var listOrderNotShipped = orderService.findOrderNotShipped();
        if(listOrderNotShipped.isEmpty())
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Hien khong co don hang nao ",""
                    )
            );
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Danh sach cac don hang chua duoc giao",listOrderNotShipped
                )
        );
    }
    @GetMapping(value = "/orderlist/taken")
    ResponseEntity<ResponseObject> orderlisttaken(){
        int shipperId = userUtils.getUserId();
        List<OrderDetailResponse> listOrder = orderService.findOrderReadyToShip(shipperId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Danh sach cac don hang ban da nhan",listOrder
                )
        );
    }

    @GetMapping(value = "/orderlist/shipped")
    ResponseEntity<ResponseObject> orderlistshipped(){
        int shipperId = userUtils.getUserId();
        List<OrderDetailResponse> listOrder = orderService.findOrderShipped(shipperId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Danh sach cac don hang ban da giao",listOrder
                )
        );
    }




    @GetMapping(value = "/getallorder")
    ResponseEntity<ResponseObject> GetAllOrder(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User shipper = userRepository.findByEmail(auth.getName()).get();
        List<OrderDetailResponse> orderDetailResponses= orderService.getOrderDetailByShipperId(shipper.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Khong tim thay",orderDetailResponses)
        );
    }
}
