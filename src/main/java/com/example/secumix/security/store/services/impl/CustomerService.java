package com.example.secumix.security.store.services.impl;

import com.example.secumix.security.store.model.entities.OrderDetail;
import com.example.secumix.security.store.model.entities.Product;
import com.example.secumix.security.store.model.response.ProductResponse;
import com.example.secumix.security.store.model.response.StoreCustomerRespone;
import com.example.secumix.security.store.repository.OrderDetailRepo;
import com.example.secumix.security.store.repository.ProfileDetailRepo;
import com.example.secumix.security.store.services.ICustomerService;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserRepository;
import com.example.secumix.security.userprofile.ProfileDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService{
    private final UserRepository userRepository;
    private final ProfileDetailRepo profileDetailRepo;
    private final OrderDetailRepo orderDetailRepo;

    @Override
    public Page<StoreCustomerRespone> findAllCustomerPaginable(Pageable pageable, int storeid) {
        Page<ProfileDetail> customers = profileDetailRepo.getAllCustomerByStoreWithPagination(storeid,pageable);
        List<StoreCustomerRespone> customerRespones = customers
                .stream()
                .map(customer -> convertToCustomerResponse(customer, storeid))
                .collect(Collectors.toList());
        return new PageImpl<>(customerRespones, pageable, customers.getTotalElements());
    }



    @Override
    public Page<StoreCustomerRespone> findCustomerByTitleContainingIgnoreCase(String keyword, Pageable pageable, int storeid) {
        Page<ProfileDetail> customers = profileDetailRepo.findCustomerByTitleContainingIgnoreCase(storeid, keyword, pageable);
        List<StoreCustomerRespone> productResponseList = customers
                .stream()
                .map(customer -> convertToCustomerResponse(customer, storeid))
                .collect(Collectors.toList());
        return new PageImpl<>(productResponseList, pageable, customers.getTotalElements());
    }

    private StoreCustomerRespone convertToCustomerResponse(ProfileDetail customer, int storeId) {
        if (customer == null) {
            return null;
        }
        StoreCustomerRespone response = new StoreCustomerRespone();
        response.setCustomerId(customer.getUser().getId());  // Chuyển ID từ Integer sang String
        response.setCustomerName(customer.getFirstname() + " " + customer.getLastname());
        response.setCustomerPhoneNumber(customer.getEmail());  // Giả sử bạn lưu số điện thoại trong trường email
        response.setTotalPayment(calculateTotalPayment(customer.getUser().getId(),storeId));  // Giả sử bạn có phương thức để tính tổng thanh toán
        response.setTotalOrder(calculateTotalOrder(customer.getUser().getId(),storeId));  // Giả sử bạn có phương thức để tính tổng số đơn hàng
        response.setOrderDetails(fetchOrderDetails(customer.getUser().getId(),storeId));  // Giả sử bạn có phương thức để lấy chi tiết đơn hàng
        return response;
    }
    private long calculateTotalPayment(int userId, int storeId) {
        // Giả sử phương thức này cần ID của User và ID của Store
        return orderDetailRepo.RevenueByStoreAndUser(userId, storeId);  // Thay đổi phù hợp với phương thức bạn cần
    }

    private int calculateTotalOrder(int userId, int storeId) {
        // Tính toán và trả về tổng số đơn hàng của khách hàng
        return orderDetailRepo.totalOrderByStoreAndUser(userId, storeId);  // Thay thế bằng giá trị thực tế
    }

    private List<Integer> fetchOrderDetails(int userId,int storeId) {
        List<Integer> orderDetailIds = new ArrayList<>();
        // Assuming you have access to the database and the necessary methods to fetch order details
        List<OrderDetail> orderDetails = orderDetailRepo.listOrderByStoreAndUser(userId, storeId);
        for (OrderDetail orderDetail : orderDetails) {
            orderDetailIds.add(orderDetail.getOrderDetailId());
        }
        return orderDetailIds;
    }

//    private List<OrderDetail> fetchOrderDetails(int userId, int storeId) {
//        // Lấy và trả về danh sách chi tiết đơn hàng của khách hàng
//        return orderDetailRepo.listOrderByStoreAndUser(userId, storeId);  // Thay thế bằng giá trị thực tế
//    }

}
