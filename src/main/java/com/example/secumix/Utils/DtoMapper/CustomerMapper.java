package com.example.secumix.Utils.DtoMapper;

import com.example.secumix.entities.OrderDetail;
import com.example.secumix.entities.ProfileDetail;
import com.example.secumix.payload.dtos.CustomerDto;
import com.example.secumix.entities.User;
import com.example.secumix.payload.response.StoreCustomerRespone;
import com.example.secumix.repository.OrderDetailRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Component
@RequiredArgsConstructor
public class CustomerMapper {
    private final StoreMapper storeMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final OrderDetailRepo orderDetailRepo;

    public CustomerDto toDto(User user) {
        if (user == null) {
            return null;
        }

        CustomerDto userDto = new CustomerDto();
        userDto.setId(user.getId());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setEmail(user.getEmail());
        userDto.setEnabled(user.isEnabled());
        userDto.setOnlineStatus(user.isOnlineStatus());
        userDto.setOrderDetails(user.getOrderDetails().stream().map(orderDetailMapper::toDto).collect(Collectors.toList()));
        userDto.setStores(user.getStores().stream().map(storeMapper::toDto).collect(Collectors.toSet()));

        return userDto;
    }

    public StoreCustomerRespone convertToCustomerResponse(ProfileDetail customer, int storeId) {
        if (customer == null) {
            return null;
        }
        StoreCustomerRespone response = new StoreCustomerRespone();
        response.setCustomerId(customer.getUser().getId());  // Chuyển ID từ Integer sang String
        response.setCustomerName(customer.getFirstname() + " " + customer.getLastname());
        response.setCustomerPhoneNumber(customer.getPhoneNumber());  // Giả sử bạn lưu số điện thoại trong trường email
        response.setTotalPayment(calculateTotalPayment(customer.getUser().getId(),storeId));  // Giả sử bạn có phương thức để tính tổng thanh toán
        response.setTotalOrder(calculateTotalOrder(customer.getUser().getId(),storeId));  // Giả sử bạn có phương thức để tính tổng số đơn hàng
        response.setOrderDetails(fetchOrderDetails(customer.getUser().getId(),storeId));  // Giả sử bạn có phương thức để lấy chi tiết đơn hàng
        return response;
    }
    private long calculateTotalPayment(int userId, int storeId) {
        // Giả sử phương thức này cần ID của User và ID của Store
        Long totalPayment = orderDetailRepo.RevenueByStoreAndUser(userId, storeId);
        if(totalPayment == null) return 0;
        return totalPayment;// Thay đổi phù hợp với phương thức bạn cần
    }

    private int calculateTotalOrder(int userId, int storeId) {
        // Tính toán và trả về tổng số đơn hàng của khách hàng
        return orderDetailRepo.totalOrderByStoreAndUser(userId, storeId);  // Thay thế bằng giá trị thực tế
    }

    private List<Integer> fetchOrderDetails(int userId, int storeId) {
        List<Integer> orderDetailIds = new ArrayList<>();
        // Assuming you have access to the database and the necessary methods to fetch order details
        List<OrderDetail> orderDetails = orderDetailRepo.listOrderByStoreAndUser(userId, storeId);
        for (OrderDetail orderDetail : orderDetails) {
            orderDetailIds.add(orderDetail.getOrderDetailId());
        }
        return orderDetailIds;
    }

}
