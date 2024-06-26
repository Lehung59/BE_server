package com.example.secumix.services.impl;

import com.example.secumix.Utils.UserUtils;
import com.example.secumix.notify.Notify;
import com.example.secumix.notify.NotifyRepository;
import com.example.secumix.entities.Cart;
import com.example.secumix.entities.CartItem;
import com.example.secumix.entities.Product;
import com.example.secumix.payload.request.CartItemRequest;
import com.example.secumix.payload.response.CartItemResponse;
import com.example.secumix.services.ICartItemService;
import com.example.secumix.repository.CartItemRepo;
import com.example.secumix.repository.CartRepo;
import com.example.secumix.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final ProductRepo productRepo;
    private final CartItemRepo cartItemRepo;
    private final CartRepo cartRepo;
    private final NotifyRepository notifyRepository;
    private final UserUtils userUtils;



    @Override
    public Page<CartItem> findByUser(int page, int size) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Pageable paging = PageRequest.of(page - 1, size);
        return cartItemRepo.findByUser(email, paging);
    }


    @Override
    public void Insert(CartItemRequest cartItemRequest) {
        String email = userUtils.getUserEmail();
        Product product= productRepo.findById(cartItemRequest.getProductid()).get();

        long realPrice = product.getPrice();
        if (product.getDiscount() != 0) {
            realPrice -= product.getDiscount() * product.getPrice() / 100;
        }

        Cart cart= cartRepo.findByEmail(email);
        Optional<CartItem> rscartItem= cartItemRepo.finfByProductandUser(cartItemRequest.getProductid(), email);
        if (rscartItem.isEmpty()){
            CartItem cartItem= CartItem.builder()
                    .quantity(cartItemRequest.getQuantity())
                    .createAt(UserUtils.getCurrentDay())
                    .updatedAt(UserUtils.getCurrentDay())
                    .cart(cart)
                    .pricetotal(realPrice * cartItemRequest.getQuantity())
                    .product(product)
                    .build();
            cartItemRepo.save(cartItem);
//            product.setQuantity(product.getQuantity()- cartItemRequest.getQuantity());
//            productRepo.save(product);
        } else {
            rscartItem.get().setQuantity(rscartItem.get().getQuantity()+ cartItemRequest.getQuantity());
            rscartItem.get().setPricetotal(realPrice*rscartItem.get().getQuantity());
            cartItemRepo.save(rscartItem.get());
//            product.setQuantity(product.getQuantity()- cartItemRequest.getQuantity());
//            productRepo.save(product);
        }
        Notify notify= Notify.builder()
                .user(cart.getUser())
                .description("Them vao gio hang thanh cong !")
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
        Optional<CartItem> cartItem = cartItemRepo.findByitemidandUser(cartitemid,email);
        return cartItem;
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

    @Override
    public void updateCartItem(int cartItemId, int quantity) {


        CartItem cartItem = cartItemRepo.findById(cartItemId).get();
Product product = cartItem.getProduct();

        long realPrice = product.getPrice();
        if (product.getDiscount() != 0) {
            realPrice -= product.getDiscount() * product.getPrice() / 100;
        }


        cartItem.setQuantity(quantity);
        cartItem.setPricetotal(realPrice*quantity);
        cartItem.setUpdatedAt(UserUtils.getCurrentDay());
        cartItemRepo.save(cartItem);
    }

}
