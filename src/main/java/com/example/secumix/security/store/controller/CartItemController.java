package com.example.secumix.security.store.controller;

import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.store.model.entities.CartItem;
import com.example.secumix.security.store.model.entities.Product;
import com.example.secumix.security.store.model.request.CartItemRequest;

import com.example.secumix.security.store.services.ICartItemService;
import com.example.secumix.security.store.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/customer")
public class CartItemController {
    @Autowired
    private ICartItemService cartItemService;
    @Autowired
    private ProductRepo productRepo;
    @GetMapping("/getallitem")
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
        if (quantity>= product.get().getQuantity() ){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED","Luong hang khong du dap ung","")
            );
        }
        CartItemRequest cartItemRequest= new CartItemRequest(quantity,productid);
        cartItemService.Insert(cartItemRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Dat hang thanh cong","")
        );
    }
    @PutMapping (value = "/ordertail/update/{cartitemid}")
    ResponseEntity<ResponseObject> UpdateCartItem(@RequestParam int quantity,
                                                  @PathVariable int cartitemid
    ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
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
        if (cartItem.get().getQuantity()+cartItem.get().getProduct().getQuantity()<quantity){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED","Luong hang khong du dap ung","")
            );
        }
        cartItem.get().getProduct().setQuantity(cartItem.get().getQuantity()+cartItem.get().getProduct().getQuantity()-quantity);
        productRepo.save(cartItem.get().getProduct());
        cartItem.get().setQuantity(quantity);
        cartItem.get().setPricetotal(cartItem.get().getProduct().getPrice()*quantity);
        cartItemService.Save(cartItem.get());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Cập nhật thành công","")
        );
    }
    @GetMapping(value = "/ordertail/delete/{cartitemid}")
    ResponseEntity<ResponseObject> DeleteCartItem(@PathVariable int cartitemid){
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
