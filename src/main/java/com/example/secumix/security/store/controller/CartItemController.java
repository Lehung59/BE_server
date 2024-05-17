package com.example.secumix.security.store.controller;

import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.store.model.entities.CartItem;
import com.example.secumix.security.store.model.entities.Product;
import com.example.secumix.security.store.model.request.CartItemRequest;

import com.example.secumix.security.store.services.ICartItemService;
import com.example.secumix.security.store.repository.ProductRepo;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.netty.udp.UdpServer;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/customer")
@RequiredArgsConstructor
public class CartItemController {
    private final ICartItemService cartItemService;
    private final ProductRepo productRepo;
    private final UserService userService;
    @GetMapping("/cartitem/view")
    ResponseEntity<ResponseObject> getAllItemByUser(){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Get All" , cartItemService.findByUser())
        );
    }

    @PostMapping(value = "/cartitem/insert")
    ResponseEntity<ResponseObject> InsertCartItem(@RequestParam int quantity,
                                                  @RequestParam int productid
    ){

        Optional<Product> product= productRepo.findById(productid);
        if (product.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED","Khong tim thay san pham","")
            );
        }
        if (quantity<=0 ){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED","So luong la mot so duong","")
            );
        }
        CartItemRequest cartItemRequest= new CartItemRequest(quantity,productid);
        cartItemService.Insert(cartItemRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Them vao gio hang thanh cong","")
        );
    }

    @PutMapping (value = "/cartitem/update")
    ResponseEntity<ResponseObject> UpdateCartItem(@RequestParam int quantity,
                                                  @RequestParam int cartitemid
    ){
        Optional<CartItem> cartItem= cartItemService.findByIdandUser(cartitemid);
        if (cartItem.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED","Khong tim thay don dat san pham","")
            );
        }

        if (quantity<=0 ){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED","So luong la mot so duong","")
            );
        }
//        cartItem.get().setQuantity(quantity);
//        cartItem.get().setPricetotal(cartItem.get().getProduct().getPrice()*quantity);
        cartItemService.updateCartItem(cartitemid, quantity);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Cập nhật giỏ hàng thành công","")
        );
    }

    @GetMapping(value = "/cartitem/delete")
    ResponseEntity<ResponseObject> DeleteCartItem(@RequestParam int cartitemid){
        boolean RS = cartItemService.Delete(cartitemid);
        if (RS){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Xoa thành công","")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("OK","Khong tim thay","")
            );
        }
    }

}
