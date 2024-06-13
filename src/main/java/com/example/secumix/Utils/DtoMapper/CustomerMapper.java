package com.example.secumix.Utils.DtoMapper;

import com.example.secumix.payload.dtos.CustomerDto;
import com.example.secumix.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
@Component
@RequiredArgsConstructor
public class CustomerMapper {
    private final StoreMapper storeMapper;
    private final OrderDetailMapper orderDetailMapper;

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
}
