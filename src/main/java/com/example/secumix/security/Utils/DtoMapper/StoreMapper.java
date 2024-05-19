package com.example.secumix.security.Utils.DtoMapper;
import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.secumix.security.store.model.dtos.CustomerDto;
import com.example.secumix.security.store.model.dtos.StoreDto;
import com.example.secumix.security.store.model.entities.Product;
import com.example.secumix.security.store.model.entities.ProductType;
import com.example.secumix.security.store.model.entities.Store;
import com.example.secumix.security.store.model.entities.StoreType;
import com.example.secumix.security.store.model.request.StoreViewResponse;
import com.example.secumix.security.store.repository.ProductRepo;
import com.example.secumix.security.store.repository.ProductTypeRepo;
import com.example.secumix.security.store.repository.StoreRepo;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class StoreMapper {

    private final ProductTypeRepo productTypeRepository;
    private final UserRepository userRepository;
    private final ProductRepo productRepository;
    private final StoreRepo storeRepo;

    public StoreDto toDto(Store store) {
        if (store == null) {
            return null;
        }

        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(store.getStoreId());
        storeDto.setStoreName(store.getStoreName());
        storeDto.setAddress(store.getAddress());
        storeDto.setPhoneNumber(store.getPhoneNumber());
        storeDto.setRate(store.getRate());
        storeDto.setImage(store.getImage());
        storeDto.setStoreTypeId(store.getStoreType().getStoreTypeId());
        storeDto.setProductTypeIds(store.getProductType().stream().map(ProductType::getProductTypeId).collect(Collectors.toList()));
        storeDto.setProductListIds(store.getProductList().stream().map(Product::getProductId).collect(Collectors.toList()));
        storeDto.setEmailManager(store.getEmailmanager());
        storeDto.setUserIds(store.getUsers().stream().map(User::getId).collect(Collectors.toSet()));

        return storeDto;
    }

    public Store toEntity(StoreDto storeDto) {
        if (storeDto == null) {
            return null;
        }

        Store store = new Store();
        store.setStoreId(storeDto.getStoreId());
        store.setStoreName(storeDto.getStoreName());
        store.setAddress(storeDto.getAddress());
        store.setPhoneNumber(storeDto.getPhoneNumber());
        store.setRate(storeDto.getRate());
        store.setImage(storeDto.getImage());

        StoreType storeType = new StoreType();
        storeType.setStoreTypeId(storeDto.getStoreTypeId());
        store.setStoreType(storeType);

        List<ProductType> productTypes = storeDto.getProductTypeIds().stream()
                .map(productTypeRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        store.setProductType(productTypes);

        List<Product> products = storeDto.getProductListIds().stream()
                .map(productRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        store.setProductList(products);

        store.setEmailmanager(storeDto.getEmailManager());

        Set<User> users = storeDto.getUserIds().stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        store.setUsers(users);

        return store;
    }

    public StoreViewResponse convertToStoreViewResponse(Store store) {
        if (store == null) {
            return null;
        }
        StoreViewResponse storeViewResponse = new StoreViewResponse();
        storeViewResponse.setStoreId(store.getStoreId());
        storeViewResponse.setStoreName(store.getStoreName());
        storeViewResponse.setAddress(store.getAddress());
        storeViewResponse.setPhoneNumber(store.getPhoneNumber());
        storeViewResponse.setRate(store.getRate());
        storeViewResponse.setImage(store.getImage());
        storeViewResponse.setStoreType(store.getStoreType() != null ? store.getStoreType().getStoreTypeName() : null);
        storeViewResponse.setProductType(store.getProductType().stream().map(ProductType::getProductTypeName).toList());
//        List<ProductDto> productListDtos = store.getProductList().stream()
//                .map(product -> {
//                    ProductDto dto = new ProductDto();
//                    dto.setProductId(product.getProductId());
//                    dto.setProductName(product.getProductName());
//                    // Map other fields as needed
//                    return dto;
//                }).collect(Collectors.toList());
//        storeViewResponse.setProductList(productListDtos);
        storeViewResponse.setEmailmanager(store.getEmailmanager());
//        List<Integer> userId = storeRepo.

//        List<User> userId = storeRepo.findCustomerByStoreId(1);
        Set<CustomerDto> customerDtos = storeRepo.findCustomerByStoreId(store.getStoreId()).stream()
                .map(customer -> {
                    CustomerDto dto = new CustomerDto();
                    dto.setId(customer.getId());
                    dto.setEmail(customer.getEmail());
                    dto.setLastname(customer.getLastname());
                    dto.setFirstname(customer.getFirstname());
                    dto.setEnabled(customer.isEnabled());
                    dto.setOnlineStatus(customer.isOnlineStatus());
                    return dto;
                }).collect(Collectors.toSet());
        storeViewResponse.setCustomerDtos(customerDtos);

        return storeViewResponse;
    }


}
