package com.example.secumix.services.impl;


import com.example.secumix.entities.*;
import com.example.secumix.repository.*;
import com.example.secumix.exception.CustomException;
import com.example.secumix.Utils.DtoMapper.OrderDetailMapper;
import com.example.secumix.Utils.UserUtils;
import com.example.secumix.notify.Notify;
import com.example.secumix.notify.NotifyRepository;
import com.example.secumix.payload.dtos.OrderDetailDto;
import com.example.secumix.payload.request.OrderDetailRequest;
import com.example.secumix.payload.response.OrderDetailResponse;
import com.example.secumix.services.ICartItemService;
import com.example.secumix.services.IOrderDetailService;
import com.example.secumix.entities.User;
import com.example.secumix.repository.UserRepository;
import com.example.secumix.entities.ProfileDetail;
import com.example.secumix.repository.ProfileDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final OrderDetailRepo orderDetailRepo;
    private final ProductRepo productRepo;
    private final StoreRepo storeRepo;
    private final ICartItemService cartItemService;
    private final OrderStatusRepo orderStatusRepo;
    private final CartRepo cartRepo;
    private final NotifyRepository notifyRepository;
    private final UserRepository userRepository;
    private final CartItemRepo cartItemRepo;
    private final PayRepo payRepo;
    private final ProfileDetailRepository detailRepository;
    private final UserUtils userUtils;
    private final OrderDetailMapper orderDetailMapper;
    private final StoreService storeService;
    private final ProfileDetailRepo profileDetailRepo;


    @Override
    public List<OrderDetailResponse> GetAllByUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return orderDetailRepo.getAllByUser(email).stream().map(
                orderDetail -> {
                    Store store = orderDetail.getStore();
                    Product product = productRepo.findById(orderDetail.getProduct().getProductId()).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"Khong tim thay san pham"));
                    OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
                    orderDetailResponse.setOrderDetailId(orderDetail.getOrderDetailId());
                    orderDetailResponse.setProductName(product.getProductName());
                    orderDetailResponse.setOrderStatusName(orderDetail.getOrderStatus().getOrderStatusName());
                    orderDetailResponse.setQuantity(orderDetail.getQuantity());
                    orderDetailResponse.setProductImg(product.getAvatarProduct());
                    orderDetailResponse.setAddress(detailRepository.findProfileDetailBy(orderDetail.getUser().getEmail()).get().getAddress());
                    orderDetailResponse.setPriceTotal(orderDetail.getPriceTotal());
                    orderDetailResponse.setStoreName(store.getStoreName());
                    return orderDetailResponse;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDetail> findByIDandUser(int orderdetailid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return orderDetailRepo.findByIDandUser(orderdetailid, email);
    }

    @Override
    @Transactional
    public void Insert(OrderDetailRequest orderDetailRequest) {
        Product product = productRepo.findProductForUpdate(orderDetailRequest.getProductid());
        Store store = product.getStore();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(HttpStatus.NOT_IMPLEMENTED, "Khong ton tai nguoi dung nay"));
        OrderStatus orderStatus = orderStatusRepo.findById(1).get();
        OrderDetail orderDetail = OrderDetail.builder()
                .orderStatus(orderStatus)
                .quantity(orderDetailRequest.getQuantity())
                .priceTotal(product.getPrice() * orderDetailRequest.getQuantity())
                .user(user)
                .product(product)
                .createdAt(UserUtils.getCurrentDay())
                .updatedAt(UserUtils.getCurrentDay())
                .store(store)
                .build();
        orderDetailRepo.save(orderDetail);
        product.setQuantity(product.getQuantity() - orderDetailRequest.getQuantity());
        productRepo.save(product);

        // Thông báo cho người dùng
        Notify notifyuser = Notify.builder()
                .user(user)
                .description("Đặt hàng thanh công. Vui lòng theo dõi thông tin đơn hàng.")
                .build();
        notifyRepository.save(notifyuser);

        // Thông báo cho quản lý
        User manager = userRepository.findByEmail(store.getEmailmanager()).get();
        Notify notifymanager = Notify.builder()
                .user(manager)
                .description("Đơn hàng mới từ khách hàng" + email)
                .build();
        notifyRepository.save(notifymanager);
    }

    @Override
    public void Save(OrderDetail orderDetail) {
        orderDetailRepo.save(orderDetail);
    }

    @Override
    public void ChangeStatus1(int orderdetailid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User shipper = userRepository.findByEmail(email).get();
        OrderStatus orderStatus = orderStatusRepo.findById(2).get();
        OrderDetail orderDetail = orderDetailRepo.findById(orderdetailid).get();
        User customer = orderDetail.getUser();
        Product product = orderDetail.getProduct();
        orderDetail.setOrderStatus(orderStatus);
        orderDetail.setUpdatedAt(UserUtils.getCurrentDay());
        orderDetail.setShipperid(shipper.getId());
        orderDetail.setShipTakenAt(UserUtils.getCurrentDay());
        orderDetailRepo.save(orderDetail);
        Notify notify = Notify.builder()
                .user(customer)
                .description("Đơn hàng " + product.getProductName() + " của bạn đã được vận chuyển")
                .build();
        notifyRepository.save(notify);
    }

    /*
     * Chuyển trạng thái giao hàng thành công
     * PreAuthoz = SHIPPER
     */
    @Override
    public void ChangeStatus2(int orderdetailid) {
        OrderStatus orderStatus = orderStatusRepo.findById(3).get();
        OrderDetail orderDetail = orderDetailRepo.findById(orderdetailid).get();
        User customer = orderDetail.getUser();
        orderDetail.setOrderStatus(orderStatus);
        orderDetail.setShippedAt(UserUtils.getCurrentDay());
        orderDetail.setUpdatedAt(UserUtils.getCurrentDay());
        orderDetailRepo.save(orderDetail);
        Product product = orderDetail.getProduct();
        Pay pay = Pay.builder()
                .status(0)
                .originalPrice(orderDetail.getPriceTotal())
                .orderDetail(orderDetail)
                .createdAt(UserUtils.getCurrentDay())
                .userId(customer.getId())
                .paymentMethod("PTTT")
                .build();
        payRepo.save(pay);
        Notify notify = Notify.builder()
                .user(customer)
                .description("Đơn hàng " + product.getProductName() + " của bạn đã hoàn thành")
                .build();
        notifyRepository.save(notify);
    }

    /*
     * Chuyển trạng thái sang huy đơn
     * PreAuthoz = USER
     */
    @Override
    public void ChangeStatus3(int orderdetailid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        OrderStatus orderStatus = orderStatusRepo.findById(4).get();
        OrderDetail orderDetail = orderDetailRepo.findByIDandUser(orderdetailid, email).orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND,"Khong tim thay order"));
        if(orderDetail.getOrderStatus().getOrderStatusId() == 4) throw new CustomException(HttpStatus.NOT_IMPLEMENTED,"Don hang da bi huy truoc do");
        if(orderDetail.getOrderStatus().getOrderStatusId() == 2 || orderDetail.getOrderStatus().getOrderStatusId() == 3 ) throw new CustomException(HttpStatus.NOT_IMPLEMENTED,"Khong the huy, don hang da duoc giao");
        User customer = orderDetail.getUser();
        orderDetail.setOrderStatus(orderStatus);
        orderDetail.setUpdatedAt(UserUtils.getCurrentDay());
        orderDetail.setCancelAt(UserUtils.getCurrentDay());
        orderDetailRepo.save(orderDetail);
        Store store = orderDetail.getStore();
        Product product = orderDetail.getProduct();
        product.setQuantity(product.getQuantity() + orderDetail.getQuantity());
        productRepo.save(product);
        var noti = Notify.builder()
                .description("Cập nhật đơn hàng " + product.getProductName())
                .user(customer)
                .build();
        notifyRepository.save(noti);
    }

    /*
     * Đặt hàng gián tiếp thông qua cartitem
     * PreAuthoz = USER
     */
    @Override
    @Transactional
    public void InsertIDR(OrderDetailRequest orderDetailRequest, CartItem cartItem) {
        Product product = productRepo.findProductForUpdate(orderDetailRequest.getProductid());
        Store store = product.getStore();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Cart cart = cartRepo.findByEmail(email);
        OrderStatus orderStatus = orderStatusRepo.findById(1).get();
        OrderDetail orderDetail = OrderDetail.builder()
                .orderStatus(orderStatus)
                .cart(cart)
                .user(cart.getUser())
                .quantity(orderDetailRequest.getQuantity())
                .priceTotal(product.getPrice() * orderDetailRequest.getQuantity())
                .product(product)
                .store(store)
                .createdAt(UserUtils.getCurrentDay())
                .updatedAt(UserUtils.getCurrentDay())
                .build();
        orderDetailRepo.save(orderDetail);
        cartItemRepo.delete(cartItem);
        Notify notify = Notify.builder()
                .user(cart.getUser())
                .description("Đặt hàng thanh công. Vui lòng theo dõi thông tin đơn hàng.")
                .build();
        notifyRepository.save(notify);
    }

    @Override
    public Optional<OrderDetailResponse> GetInfoOrder(int orderdetailid) {
        User user = userRepository.findByEmail(userUtils.getUserEmail()).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Khong thay nguoi dung nao dang dang nhap hien tai"));
        return orderDetailRepo.findById(orderdetailid).map(
                orderDetail -> {
                    Store store = storeRepo.findStoreByName(orderDetail.getStore().getStoreName()).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"Khong tim thay cua hang"));
                    Product product = orderDetail.getProduct();
                    OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
                    orderDetailResponse.setOrderDetailId(orderDetail.getOrderDetailId());
                    orderDetailResponse.setProductName(product.getProductName());
                    orderDetailResponse.setOrderStatusName(orderDetail.getOrderStatus().getOrderStatusName());
                    orderDetailResponse.setQuantity(orderDetail.getQuantity());
                    orderDetailResponse.setProductImg(product.getAvatarProduct());
                    orderDetailResponse.setPriceTotal(orderDetail.getPriceTotal());
                    orderDetailResponse.setAddress(detailRepository.findProfileDetailBy(user.getEmail()).get().getAddress());
                    orderDetailResponse.setStoreName(store.getStoreName());
                    return orderDetailResponse;
                }
        );
    }

    @Override
    public List<OrderDetailResponse> getOrderDetailByShipperId(int id) {
        String email = userUtils.getUserEmail();
        List<OrderDetailResponse> orderDetailResponses = orderDetailRepo.getOrderDetailByShipperId(id).stream().map(
                orderDetail -> {
                    Store store = orderDetail.getStore();
                    Product product = orderDetail.getProduct();
                    OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
                    orderDetailResponse.setOrderDetailId(orderDetail.getOrderDetailId());
                    orderDetailResponse.setProductName(product.getProductName());
                    orderDetailResponse.setOrderStatusName(orderDetail.getOrderStatus().getOrderStatusName());
                    orderDetailResponse.setQuantity(orderDetail.getQuantity());
                    orderDetailResponse.setProductImg(product.getAvatarProduct());
                    orderDetailResponse.setPriceTotal(orderDetail.getPriceTotal());
                    orderDetailResponse.setStoreName(store.getStoreName());
                    orderDetailResponse.setAddress(detailRepository.findProfileDetailBy(email).get().getAddress());
                    return orderDetailResponse;
                }
        ).collect(Collectors.toList());
        return orderDetailResponses;
    }

    @Override
    public Page<OrderDetailResponse> findAllOrderByCustomerAndStorePaginable(Pageable pageable, int storeid, int customerid) {
        String storeName = storeRepo.findStoreById(storeid).get().getStoreName();
        Page<OrderDetail> orderDetails = orderDetailRepo.findAllOrderByCustomerAndStorePaginable(pageable, storeName, customerid);
        List<OrderDetailResponse> orderDetailResponses = orderDetails
                .stream()
                .map(this::convertToOrderDetailResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(orderDetailResponses, pageable, orderDetails.getTotalElements());
    }

    @Override
    public Page<OrderDetailResponse> findOrderByTitleContainingIgnoreCase(String keyword, Pageable pageable, int storeid, int customerid) {
        String storeName = storeRepo.findStoreById(storeid).get().getStoreName();
        Page<OrderDetail> orderDetails = orderDetailRepo.findOrderByTitleContainingIgnoreCase(keyword, pageable, storeName, customerid);
        List<OrderDetailResponse> orderDetailResponses = orderDetails
                .stream()
                .map(this::convertToOrderDetailResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(orderDetailResponses, pageable, orderDetails.getTotalElements());
    }

    @Override
    public Page<OrderDetailResponse> findAllOrderPaginable(Pageable pageable, int storeId) {
        Page<OrderDetail> orderDetails = orderDetailRepo.getAllByStoreWithPagination(storeId, pageable);
        List<OrderDetailResponse> orderDetailResponses = orderDetails.getContent()
                .stream()
                .map(this::convertToOrderDetailResponse)
                .toList();

        return new PageImpl<>(orderDetailResponses, pageable, orderDetails.getTotalElements());
    }

    @Override
    public Page<OrderDetailResponse> findByTitleContainingIgnoreCase(String keyword, Pageable pageable, int storeId) {
        Page<OrderDetail> orderDetails = orderDetailRepo.findByTitleContainingIgnoreCase(storeId, keyword, pageable);
        List<OrderDetailResponse> orderDetailResponses = orderDetails.getContent()
                .stream()
                .map(this::convertToOrderDetailResponse)
                .toList();

        return new PageImpl<>(orderDetailResponses, pageable, orderDetails.getTotalElements());
    }

    @Override
    public OrderDetailDto findDtoById(int orderDetailId) {
        OrderDetail orderDetail = orderDetailRepo.findById(orderDetailId).orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND,"Khong ton tai order nay"));
        Store store = orderDetail.getStore();
        if(!userUtils.getRole().contains("ROLE_SHIPPER"))
            storeService.checkStoreAuthen(store.getStoreId());
        return orderDetailMapper.toDto(orderDetail);
    }

    @Override
    public List<OrderDetailResponse> findOrderNotShipped() {
        return orderDetailRepo.findOrderNotShipped().stream().map(
                this::convertToOrderDetailResponse
        ).toList();
    }

    @Override
    public List<OrderDetailResponse> findOrderReadyToShip(int shipperId) {
        return orderDetailRepo.findOrderReadyToShip(shipperId).stream().map(
                this::convertToOrderDetailResponse
        ).toList();


    }
    @Override
    public List<OrderDetailResponse> findOrderShipped(int shipperId) {
        return orderDetailRepo.findOrderReadyToShip(shipperId).stream().map(
                this::convertToOrderDetailResponse
        ).toList();


    }


    private OrderDetailResponse convertToOrderDetailResponse(OrderDetail orderDetail) {
        OrderDetailResponse response = new OrderDetailResponse();
        String storeName = orderDetail.getStore().getStoreName();
        Product product = orderDetail.getProduct();
        ProfileDetail profileDetail = profileDetailRepo.findByUserId(orderDetail.getUser().getId());
        response.setOrderDetailId(orderDetail.getOrderDetailId());
        response.setQuantity(orderDetail.getQuantity());
        response.setProductName(product.getProductName());
        response.setPriceTotal(orderDetail.getPriceTotal());

        // Assuming orderStatus is not null and contains order status name
        response.setOrderStatusName(orderDetail.getOrderStatus().getOrderStatusName());

        // Assuming other properties are directly mapped
        response.setAddress(profileDetail.getAddress());
        response.setProductImg(product.getAvatarProduct());
        response.setStoreName(storeName);

        return response;
    }

}
