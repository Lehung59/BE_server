package com.example.secumix.services.impl;

import com.example.secumix.exception.CustomException;
import com.example.secumix.Utils.DtoMapper.StoreMapper;

import com.example.secumix.Utils.UserUtils;
import com.example.secumix.entities.Product;
import com.example.secumix.entities.ProductType;
import com.example.secumix.entities.Store;
import com.example.secumix.payload.request.StoreInfoEditRequest;
import com.example.secumix.payload.request.StoreViewResponse;

import com.example.secumix.payload.response.ProductResponse;
import com.example.secumix.payload.response.StoreFavorRespone;
import com.example.secumix.payload.response.StoreInfoView;
import com.example.secumix.repository.ProductRepo;
import com.example.secumix.repository.StoreRepo;
import com.example.secumix.services.IStoreService;
import com.example.secumix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StoreService implements IStoreService {
    private final StoreRepo storeRepo;
    private final StoreMapper storeMapper;
    private final UserRepository userRepository;
    private final ProductRepo productRepo;
    private final UserUtils userUtils;

    @Override
    public Optional<Store> findStoreById(int storeId) {
        return storeRepo.findById(storeId);
    }


    public void checkStoreAuthen(int storeId) throws CustomException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Store> store = storeRepo.findStoreById(storeId);

        if (store.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Cửa hàng không tồn tại");
        }

        if (!store.get().getEmailmanager().equals(email)) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Bạn không là chủ của cửa hàng");
        }
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
    public List<StoreViewResponse> findAllStorePaginable() {
        List<Store> stores = storeRepo.getAllStoreWithPagination();
        List<StoreViewResponse> storeViewResponses = stores
                .stream()
                .map(storeMapper::convertToStoreViewResponse)
                .toList();
        return storeViewResponses;
    }

    @Override
    public List<StoreViewResponse> findAllStoreByTitleContainingIgnoreCase(String keyword) {
        List<Store> stores = storeRepo.findStoreByTitleContainingIgnoreCase(keyword);
        List<StoreViewResponse> importResponses = stores
                .stream()
                .map(storeMapper::convertToStoreViewResponse)
                .toList();

        return importResponses;
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
                .storeType(store.getStoreType().getStoreTypeName())
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
    public List<ProductResponse> findSellingProduct(int storeid, String keyword) {
        int userId = userUtils.getUserId();

        List<Product> products;
        if(keyword == null || keyword.isEmpty()){
            products = productRepo.findSellingProduct(storeid);

        } else products = productRepo.findSellingProductKeyword(storeid,keyword);
        List<ProductResponse> productResponseList = products.stream().map(product -> {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setProductId(product.getProductId());
            productResponse.setAvatarProduct(product.getAvatarProduct());
            productResponse.setProductName(product.getProductName());
            productResponse.setProductType(product.getProductType().getProductTypeName());
            productResponse.setQuantity(product.getQuantity());
            productResponse.setStoreName(product.getStore().getStoreName());
            productResponse.setDescription(product.getDescription());
            productResponse.setPrice(product.getPrice());
            productResponse.setStatus(product.isStatus());
            productResponse.setStoreId(product.getStore().getStoreId());
            productResponse.setProductTypeId(product.getProductType().getProductTypeId());
            productResponse.setDiscount(product.getDiscount());
            productResponse.setView(product.getView());
            return productResponse;
        }).toList();


        return productResponseList;    }


    @Override
    public List<StoreInfoView> findFavorStore(int userId, String keyword, int page, int size) {

        Pageable paging = PageRequest.of(page - 1, size);

        Page<Store> stores;
        if(keyword == null || keyword.isEmpty()){
            stores = storeRepo.findStoreFavor(userId,paging);

        } else stores = storeRepo.findStoreFavorKeyword(userId, keyword,paging);

        List<StoreInfoView> listStores = stores.getContent().stream().map(store -> {
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
        }).toList();


        return listStores;
    }

    @Override
    public void removeStoreFromFavorList(int storeid, int userId) {

        if(storeRepo.checkFavor(userId,storeid).isEmpty())
            throw new CustomException(HttpStatus.NOT_FOUND,"Khong co cua hang nay trong pha nyeu thich cua ban");
        storeRepo.deleteStoreFavor(storeid,userId);
    }



}
