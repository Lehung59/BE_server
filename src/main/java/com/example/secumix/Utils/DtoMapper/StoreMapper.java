package com.example.secumix.Utils.DtoMapper;
import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.secumix.payload.dtos.CustomerDto;
import com.example.secumix.payload.dtos.StoreDto;
import com.example.secumix.entities.Product;
import com.example.secumix.entities.ProductType;
import com.example.secumix.entities.Store;
import com.example.secumix.entities.StoreType;
import com.example.secumix.payload.request.StoreViewResponse;
import com.example.secumix.payload.response.StoreInfoView;
import com.example.secumix.repository.ProductRepo;
import com.example.secumix.repository.ProductTypeRepo;
import com.example.secumix.repository.StoreRepo;
import com.example.secumix.entities.User;
import com.example.secumix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class StoreMapper {

    private final ProductTypeRepo productTypeRepository;
    private final UserRepository userRepository;
    private final ProductRepo productRepository;
    private final StoreRepo storeRepo;
    private final ProductTypeRepo productTypeRepo;
    private final ProductRepo productRepo;

    public StoreDto toDto(Store store) {
        if (store == null) {
            return null;
        }
        List<Product> productList = store.getProductList();
        List<ProductType> productTypeList = store.getProductType();
        Set<User> userList = store.getUsers();

        List<String> products = productList.stream().map(Product::getProductName).toList();
        Set<String> usersEmail = userList.stream().map(User::getEmail).collect(Collectors.toSet());
        List<String> productTypes = productTypeList.stream().map(ProductType::getProductTypeName).toList();


        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(store.getStoreId());
        storeDto.setStoreName(store.getStoreName());
        storeDto.setAddress(store.getAddress());
        storeDto.setPhoneNumber(store.getPhoneNumber());
        storeDto.setRate(store.getRate());
        storeDto.setImage(store.getImage());
        storeDto.setStoreTypeId(store.getStoreType().getStoreTypeId());
        storeDto.setProductType(productTypes);
        storeDto.setProductList(products);
        storeDto.setEmailManager(store.getEmailmanager());
        storeDto.setUserEmail(usersEmail);

        return storeDto;
    }
    public StoreInfoView convertToStoreInfoView(Store store) {
        List<String> productName = store.getProductList().stream().map(Product::getProductName).toList();
        List<String> productTypeName = store.getProductType().stream().map(ProductType::getProductTypeName).toList();
        return StoreInfoView.builder()
                .storeId(store.getStoreId())
                .image(store.getImage())
                .storeName(store.getStoreName())
                .address(store.getAddress())
                .phoneNumber(store.getPhoneNumber())
                .rate(store.getRate())
                .productTypeName(productTypeName)
                .productListName(productName)
                .emailmanager(store.getEmailmanager())
                .storeType(store.getStoreType().getStoreTypeName())
                .build();
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

        List<ProductType> productTypes = storeDto.getProductType().stream()
                .map(productType -> {
                    return productTypeRepo.findProductTypeByName(productType,storeDto.getStoreId());
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        store.setProductType(productTypes);

        List<Product> products = storeDto.getProductList().stream()
                .map(productName ->{
                    return productRepo.findByName(storeDto.getStoreId(),productName);
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        store.setProductList(products);

        store.setEmailmanager(storeDto.getEmailManager());

        Set<User> users = storeDto.getUserEmail().stream()
                .map(userRepository::findByEmail)
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
