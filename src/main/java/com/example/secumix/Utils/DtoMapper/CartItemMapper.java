package com.example.secumix.Utils.DtoMapper;

import com.example.secumix.entities.CartItem;
import com.example.secumix.entities.Product;
import com.example.secumix.payload.response.CartItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartItemMapper {

    public CartItemResponse convertToCartItemResponse(CartItem cartItem) {
        Product product = cartItem.getProduct();
        long realPrice = product.getPrice();
        if (product.getDiscount() != 0) {
            realPrice -= product.getDiscount() * product.getPrice() / 100;
        }
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setCartItemId(cartItem.getCartItemId());
        cartItemResponse.setProductName(product.getProductName());
        cartItemResponse.setProductImg(product.getAvatarProduct());
        cartItemResponse.setQuantity(cartItem.getQuantity());
        cartItemResponse.setStoreName(product.getStore().getStoreName());
        cartItemResponse.setPriceTotal(realPrice * cartItem.getQuantity());
        return cartItemResponse;
    }
}
