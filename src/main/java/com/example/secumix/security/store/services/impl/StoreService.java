package com.example.secumix.security.store.services.impl;

import com.example.secumix.security.Exception.CustomException;
import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.Utils.DtoMapper.StoreMapper;
import com.example.secumix.security.store.model.dtos.CustomerDto;
import com.example.secumix.security.store.model.dtos.ProductDto;
import com.example.secumix.security.store.model.dtos.StoreDto;
import com.example.secumix.security.store.model.dtos.UserDto;
import com.example.secumix.security.store.model.entities.ImportDetail;
import com.example.secumix.security.store.model.entities.Product;
import com.example.secumix.security.store.model.entities.ProductType;
import com.example.secumix.security.store.model.entities.Store;
import com.example.secumix.security.store.model.request.StoreInfoEditRequest;
import com.example.secumix.security.store.model.request.StoreViewResponse;
import com.example.secumix.security.store.model.response.ImportResponse;
import com.example.secumix.security.store.model.response.ProductResponse;
import com.example.secumix.security.store.model.response.StoreInfoView;
import com.example.secumix.security.store.repository.StoreRepo;
import com.example.secumix.security.store.services.IStoreService;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StoreService implements IStoreService {
    private final StoreRepo storeRepo;
    private final StoreMapper storeMapper;
    private final UserRepository userRepository;

    @Override
    public Optional<Store> findStoreById(int storeId) {
        return storeRepo.findById(storeId);
    }


    public void checkStoreAuthen(int storeId) throws CustomException {
        return;
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName();
//        Optional<Store> store = storeRepo.findStoreById(storeId);
//
//        if (store.isEmpty()) {
//            throw new CustomException(HttpStatus.NOT_FOUND, "Cửa hàng không tồn tại");
//        }
//
//        if (!store.get().getEmailmanager().equals(email)) {
//            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Bạn không là chủ của cửa hàng");
//        }
    }

    @Override
    public Optional<Store> findStoreByEmail(String email) {
        return storeRepo.findByEmailManager(email);
    }

    @Override
    public void updateInfo(StoreInfoEditRequest storeInfoEditRequest) {
        Optional<Store> storeOptional = storeRepo.findStoreByName(storeInfoEditRequest.getStoreName());
        if (storeOptional.isPresent() && !Objects.equals(storeOptional.get().getStoreName(), storeInfoEditRequest.getStoreName())) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Tên cửa hàng đã tồn tại");
        }
        // Kiểm tra số điện thoại đã tồn tại
        Optional<Store> storeOptional2 = storeRepo.findByPhonenumber(storeInfoEditRequest.getPhonumber());
        if (storeOptional2.isPresent() && !Objects.equals(storeOptional2.get().getPhoneNumber(), storeInfoEditRequest.getPhonumber())) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Số điện thoại đã tồn tại");
        }

        // Tìm đối tượng store để cập nhật
        Store newObject = storeRepo.findById(storeInfoEditRequest.getStoreId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Store không tồn tại"));

        // Cập nhật thông tin store
        if(storeInfoEditRequest.getPhonumber() != null)
            newObject.setPhoneNumber(storeInfoEditRequest.getPhonumber());
        if(storeInfoEditRequest.getStoreName() != null)
            newObject.setStoreName(storeInfoEditRequest.getStoreName());
        if(storeInfoEditRequest.getAddress() != null)
            newObject.setAddress(storeInfoEditRequest.getAddress());
        if(storeInfoEditRequest.getAvatar() != null)
            newObject.setImage(storeInfoEditRequest.getAvatar());

        // Lưu đối tượng store đã cập nhật
        storeRepo.save(newObject);

    }

    @Override
    public Page<StoreViewResponse> findAllStorePaginable(Pageable pageable) {
        Page<Store> stores = storeRepo.getAllStoreWithPagination(pageable);
        List<StoreViewResponse> storeViewResponses = stores
                .stream()
                .map(storeMapper::convertToStoreViewResponse)
                .toList();
        return new PageImpl<>(storeViewResponses, pageable, stores.getTotalElements());
    }

    @Override
    public Page<StoreViewResponse> findAllStoreByTitleContainingIgnoreCase(String keyword, Pageable pageable) {
        Page<Store> stores = storeRepo.findStoreByTitleContainingIgnoreCase(keyword, pageable);
        List<StoreViewResponse> importResponses = stores
                .stream()
                .map(storeMapper::convertToStoreViewResponse)
                .toList();

        return new PageImpl<>(importResponses, pageable, stores.getTotalElements());
    }

    @Override
    public StoreInfoView getInfo(int storeid) {
        Store store = storeRepo.findStoreById(storeid).orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND,"Không tồn tại cửa hàng này"));
        List<String> productName = store.getProductList().stream().map(Product::getProductName).toList();
        List<String> productTypeName = store.getProductType().stream().map(ProductType::getProductTypeName).toList();
        StoreInfoView storeInfoView = StoreInfoView.builder()
                .storeId(storeid)
                .storeName(store.getStoreName())
                .address(store.getAddress())
                .image(store.getImage())
                .phoneNumber(store.getPhoneNumber())
                .emailmanager(store.getEmailmanager())
                .rate(store.getRate())
                .productTypeName(productTypeName)
                .productListName(productName)
                .build();
        return storeInfoView;
    }

    @Override
    public void addStoreToFavor(int userId, int storeid) {
        Store store = storeRepo.findStoreById(storeid).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"Khong ton tai cua hang nay!"));
        if(!storeRepo.checkFavor(userId, storeid).isEmpty()){
            throw new CustomException(HttpStatus.NOT_IMPLEMENTED,"Da ton tai cua hang nay trong phan ua thich cua ban!");
        }
        storeRepo.saveToFavor(userId,storeid);

    }

    @Override
    public Page<StoreDto> viewFavor(int userId, String keyword, int page, int size) {
        Pageable paging = PageRequest.of(page - 1, size);
        Page<Store> stores;
        if(keyword == null || keyword.isEmpty()){
            stores = storeRepo.findStoreFavor(userId, paging);

        } else stores = storeRepo.findStoreFavorKeyword(userId, keyword,paging);
        List<Store> storeList=stores.getContent();
        List<StoreDto> storeDtoList = storeList
                .stream()
                .map(store ->
                {return storeMapper.toDto(store);})
                .toList();

        return new PageImpl<>(storeDtoList, paging, stores.getTotalElements());


    }

}
