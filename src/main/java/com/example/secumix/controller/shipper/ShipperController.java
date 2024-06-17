package com.example.secumix.controller.shipper;

import com.example.secumix.Utils.DtoMapper.OrderDetailMapper;
import com.example.secumix.constants.Constants;
import com.example.secumix.exception.CustomException;
import com.example.secumix.ResponseObject;
import com.example.secumix.Utils.UserUtils;
import com.example.secumix.entities.OrderDetail;
import com.example.secumix.payload.Pagination;
import com.example.secumix.payload.response.OrderDetailResponse;
import com.example.secumix.services.IOrderDetailService;
import com.example.secumix.repository.OrderDetailRepo;
import com.example.secumix.entities.User;
import com.example.secumix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/shipper")
@RequiredArgsConstructor
public class ShipperController {
    private final IOrderDetailService orderService;
    private final  OrderDetailRepo orderDetailRepo;
    private final  UserRepository userRepository;
    private final  UserUtils userUtils;
    private  final OrderDetailMapper orderDetailMapper;


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
    public ResponseEntity<ResponseObject> orderlistview(@RequestParam(defaultValue = Constants.PAGE) int page,
                                                        @RequestParam(defaultValue = Constants.SIZE) int size,
                                                        @RequestParam(required = false) String keyword) {
        Page<OrderDetail> orderDetails = orderService.findOrderNotShipped(page, size, keyword);

        List<OrderDetailResponse> listOrderNotShipped = orderDetails.getContent().stream()
                .map(orderDetailMapper::convertToOrderDetailResponse)
                .toList();

        if (listOrderNotShipped.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Hiện không có đơn hàng nào", "", null)
            );
        }

        Pagination pagination = new Pagination(
                orderDetails.getTotalElements(),
                orderDetails.getTotalPages(),
                orderDetails.getNumber() + 1,
                orderDetails.getSize()
        );

        ResponseObject responseObject = new ResponseObject(
                "OK",
                "Danh sách các đơn hàng chưa được giao",
                listOrderNotShipped,
                pagination
        );

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }
    @GetMapping(value = "/orderlist/taken")
    public ResponseEntity<ResponseObject> orderlisttaken(@RequestParam(defaultValue = Constants.PAGE) int page,
                                                         @RequestParam(defaultValue = Constants.SIZE) int size,
                                                         @RequestParam(required = false) String keyword) {
        int shipperId = userUtils.getUserId();
        Page<OrderDetail> orderDetails = orderService.findOrderReadyToShip(shipperId, page, size, keyword);

        List<OrderDetailResponse> listOrder = orderDetails.getContent().stream()
                .map(orderDetailMapper::convertToOrderDetailResponse)
                .toList();

        Pagination pagination = new Pagination(
                orderDetails.getTotalElements(),
                orderDetails.getTotalPages(),
                orderDetails.getNumber() + 1,
                orderDetails.getSize()
        );

        ResponseObject responseObject = new ResponseObject(
                "OK",
                "Danh sách các đơn hàng bạn đã nhận",
                listOrder,
                pagination
        );

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @GetMapping(value = "/orderlist/shipped")
    ResponseEntity<ResponseObject> orderlistshipped(@RequestParam(defaultValue = Constants.PAGE) int page,
                                                    @RequestParam(defaultValue = Constants.SIZE) int size,
                                                    @RequestParam(required = false) String keyword){
        int shipperId = userUtils.getUserId();
        Page<OrderDetail> orderDetails = orderService.findOrderShipped(shipperId, page, size, keyword);

        List<OrderDetailResponse> listOrder = orderDetails.getContent().stream()
                .map(orderDetailMapper::convertToOrderDetailResponse)
                .toList();

        Pagination pagination = new Pagination(
                orderDetails.getTotalElements(),
                orderDetails.getTotalPages(),
                orderDetails.getNumber() + 1,
                orderDetails.getSize()
        );

        ResponseObject responseObject = new ResponseObject(
                "OK",
                "Danh sách các đơn hàng bạn đã giao",
                listOrder,
                pagination
        );

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }




//    @GetMapping(value = "/getallorder")
//    ResponseEntity<ResponseObject> GetAllOrder(@RequestParam(defaultValue = Constants.PAGE) int page,
//                                               @RequestParam(defaultValue = Constants.SIZE) int size,
//                                               @RequestParam(required = false) String keyword){
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User shipper = userRepository.findByEmail(auth.getName()).get();
//        List<OrderDetailResponse> orderDetailResponses= orderService.getOrderDetailByShipperId(shipper.getId(),page,size,keyword);
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("OK","Khong tim thay",orderDetailResponses)
//        );
//    }
}
