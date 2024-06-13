package com.example.secumix.controller.customer;

import com.example.secumix.ResponseObject;
import com.example.secumix.entities.CartItem;
import com.example.secumix.entities.Product;
import com.example.secumix.payload.request.CartItemRequest;

import com.example.secumix.services.ICartItemService;
import com.example.secumix.repository.ProductRepo;
import com.example.secumix.services.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
