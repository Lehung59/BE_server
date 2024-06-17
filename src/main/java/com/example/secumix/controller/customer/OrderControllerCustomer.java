package com.example.secumix.controller.customer;

import com.example.secumix.constants.Constants;
import com.example.secumix.exception.CustomException;
import com.example.secumix.ResponseObject;
import com.example.secumix.entities.CartItem;
import com.example.secumix.entities.OrderDetail;
import com.example.secumix.entities.Product;
import com.example.secumix.payload.request.OrderDetailRequest;
import com.example.secumix.repository.OrderDetailRepo;
import com.example.secumix.repository.ProductRepo;
import com.example.secumix.services.ICartItemService;
import com.example.secumix.services.IOrderDetailService;
import com.example.secumix.entities.Permission;
import com.example.secumix.entities.User;
import com.example.secumix.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/customer")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderControllerCustomer {
    IOrderDetailService orderService;
    ProductRepo productRepo;
    ICartItemService cartItemService;
    UserRepository userRepository;
    OrderDetailRepo orderDetailRepo;
    @GetMapping(value = "/orderdetail/view")
    ResponseEntity<ResponseObject> getAllByUSer(@RequestParam(defaultValue = Constants.PAGE) int page,
                                                @RequestParam(defaultValue = Constants.SIZE) int size,
                                                @RequestParam(required = false) String orderStatus
                                                ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Lay ra thanh cong", orderService.GetAllByUser(page,size,orderStatus))
        );
    }
    @GetMapping(value = "/customer/orderdetail/view/{orderdetailid}")
    ResponseEntity<ResponseObject> getInfoOrder(@PathVariable int orderdetailid) {
        if (IsPermisson(orderdetailid)) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Lay ra thanh cong", orderService.GetInfoOrder(orderdetailid))
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("OK", "Không có quyền truy cập", "")
        );
    }
    private boolean IsPermisson(int orderdetailid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email).get();
        OrderDetail orderDetail = orderDetailRepo.findById(orderdetailid).orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND,"Khong tim thay don hang voi id" + orderdetailid));
        if (orderDetail.getUser().getEmail().equals(email) || user.getRole().getPermissions().contains(Permission.SHIPPER_READ)) {
            return true;
        }
        return false;
    }



    //đặt hàng tực tiếp
    @PostMapping(value = "/order/insert")
    ResponseEntity<ResponseObject> orderDirecly(@RequestParam int quantity,
                                            @RequestParam int productid) {
        Optional<Product> product = productRepo.findById(productid);
        if (quantity > product.get().getQuantity()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("OK", "Không đủ hàng trong kho", "")
            );
        }

        OrderDetailRequest orderDetailRequest = new OrderDetailRequest(quantity, productid);
        orderService.Insert(orderDetailRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Đặt hàng thành công", "")
        );
    }

    //Đặt hàng qua gio hàng
    @PostMapping(value = "/order/insert/cartitem")
    ResponseEntity<ResponseObject> orderByCartItem(@RequestParam int cartitemid) {
        Optional<CartItem> cartItem = cartItemService.findByIdandUser(cartitemid);
        if (cartItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", " không tồn tại trong giỏ hàng", "")
            );
        }
        OrderDetailRequest orderDetailRequest = new OrderDetailRequest(cartItem.get().getQuantity(), cartItem.get().getProduct().getProductId());
        orderService.InsertIDR(orderDetailRequest, cartItem.get());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Đặt hàng thành công", "")
        );
    }

    @GetMapping(value = "/order/delete/{orderdetailid}")
    ResponseEntity<ResponseObject> ChangeStatus(@PathVariable int orderdetailid) {
        orderService.ChangeStatus3(orderdetailid);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Hoàn đơn thành công", "")
        );
    }

}
