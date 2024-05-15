package com.example.secumix.security.store.services.impl;

import com.example.secumix.security.Utils.UserUtils;
import com.example.secumix.security.notify.Notify;
import com.example.secumix.security.notify.NotifyRepository;
import com.example.secumix.security.store.model.entities.Cart;
import com.example.secumix.security.store.model.entities.CartItem;
import com.example.secumix.security.store.model.entities.Product;
import com.example.secumix.security.store.model.request.CartItemRequest;
import com.example.secumix.security.store.model.response.CartItemResponse;
import com.example.secumix.security.store.services.ICartItemService;
import com.example.secumix.security.store.repository.CartItemRepo;
import com.example.secumix.security.store.repository.CartRepo;
import com.example.secumix.security.store.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartItemService implements ICartItemService {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private NotifyRepository notifyRepository;

    @Override
    public List<CartItemResponse> findByProduct(int productid) {
        return cartItemRepo.findByProduct(productid).stream().map(
                cartItem -> {
                    Product product= cartItem.getProduct();
                    CartItemResponse cartItemResponse= new CartItemResponse();
                    cartItemResponse.setProductName(product.getProductName());
                    cartItemResponse.setProductImg(product.getAvatarProduct());
                    cartItemResponse.setQuantity(cartItem.getQuantity());
                    cartItemResponse.setStoreName(product.getStore().getStoreName());
                    cartItemResponse.setPriceTotal(product.getPrice()*cartItem.getQuantity());
                    return cartItemResponse;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public List<CartItemResponse> findByUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return cartItemRepo.findByUser(email).stream().map(
                cartItem -> {
                    Product product= cartItem.getProduct();
                    CartItemResponse cartItemResponse= new CartItemResponse();
                    cartItemResponse.setProductName(product.getProductName());
                    cartItemResponse.setProductImg(product.getAvatarProduct());
                    cartItemResponse.setQuantity(cartItem.getQuantity());
                    cartItemResponse.setStoreName(product.getStore().getStoreName());
                    cartItemResponse.setPriceTotal(product.getPrice()*cartItem.getQuantity());
                    return cartItemResponse;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public Optional<CartItemResponse> finfByProductandUser(int productid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return cartItemRepo.finfByProductandUser(productid, email).map(
                cartItem -> {
                    Product product= cartItem.getProduct();
                    CartItemResponse cartItemResponse= new CartItemResponse();
                    cartItemResponse.setProductName(product.getProductName());
                    cartItemResponse.setProductImg(product.getAvatarProduct());
                    cartItemResponse.setQuantity(cartItem.getQuantity());
                    cartItemResponse.setStoreName(product.getStore().getStoreName());
                    cartItemResponse.setPriceTotal(product.getPrice()*cartItem.getQuantity());
                    return cartItemResponse;
                }
        );
    }
    @Override
    public void Insert(CartItemRequest cartItemRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Product product= productRepo.findById(cartItemRequest.getProductid()).get();
        Cart cart= cartRepo.findByEmail(email);
        Optional<CartItem> rscartItem= cartItemRepo.finfByProductandUser(cartItemRequest.getProductid(), email);
        if (rscartItem.isEmpty()){
            CartItem cartItem= CartItem.builder()
                    .quantity(cartItemRequest.getQuantity())
                    .createAt(UserUtils.getCurrentDay())
                    .updatedAt(UserUtils.getCurrentDay())
                    .cart(cart)
                    .pricetotal(product.getPrice()* cartItemRequest.getQuantity())
                    .product(product)
                    .build();
            cartItemRepo.save(cartItem);
            product.setQuantity(product.getQuantity()- cartItemRequest.getQuantity());
            productRepo.save(product);
        } else {
            rscartItem.get().setQuantity(rscartItem.get().getQuantity()+ cartItemRequest.getQuantity());
            rscartItem.get().setPricetotal(product.getPrice()*rscartItem.get().getQuantity());
            cartItemRepo.save(rscartItem.get());
            product.setQuantity(product.getQuantity()- cartItemRequest.getQuantity());
            productRepo.save(product);
        }
        Notify notify= Notify.builder()
                .user(cart.getUser())
                .description("Dat hang thanh cong !")
                .build();
        notifyRepository.save(notify);
    }

    @Override
    public void Save(CartItem cartItem) {
        cartItemRepo.save(cartItem);
    }
    @Override
    public Optional<CartItem> findByIdandUser(int cartitemid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return cartItemRepo.findByitemidandUser(cartitemid,email);
    }

    @Override
    public boolean Delete(int cartitemid) {
        Optional<CartItem> cartItem=findByIdandUser(cartitemid);
        if (cartItem.isPresent()){
            cartItem.get().getProduct().setQuantity(cartItem.get().getQuantity()+cartItem.get().getProduct().getQuantity());
            productRepo.save(cartItem.get().getProduct());
            cartItemRepo.deleteById(cartitemid);
            return true;
        }else {
            return false;
        }

    }

}
