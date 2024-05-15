package com.example.secumix.security.store.services.impl;



import com.example.secumix.security.Utils.UserUtils;
import com.example.secumix.security.store.model.entities.Product;
import com.example.secumix.security.store.model.entities.ProductType;
import com.example.secumix.security.store.model.entities.Store;
import com.example.secumix.security.store.model.request.AddProductRequest;
import com.example.secumix.security.store.model.response.ProductResponse;

import com.example.secumix.security.store.model.response.StoreCustomerRespone;
import com.example.secumix.security.store.repository.ProductTypeRepo;
import com.example.secumix.security.store.repository.StoreRepo;
import com.example.secumix.security.store.services.IProductService;
import com.example.secumix.security.store.repository.ProductRepo;


import com.example.secumix.security.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private StoreRepo storeRepo;
    @Autowired
    private ProductTypeRepo productTypeRepo;

    @Override
    public List<ProductResponse> getAllProduct() {
        return productRepo.findAll().stream().map(
                product -> {
                    ProductResponse productResponse = new ProductResponse();
                    productResponse.setAvatarProduct(product.getAvatarProduct());
                    productResponse.setProductName(product.getProductName());
                    productResponse.setProductType(product.getProductType().getProductTypeName());
                    productResponse.setQuantity(product.getQuantity());
                    productResponse.setStoreName(product.getStore().getStoreName());
                    productResponse.setDescription(product.getDescription());
                    productResponse.setPrice(product.getPrice());
                    productResponse.setStatus(product.getStatus());
                    productResponse.setDiscount(product.getDiscount());
                    productResponse.setView(product.getView());
                    return productResponse;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> GetAllByStore() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return productRepo.getAllByStore(email).stream().map(
                product -> {
                    ProductResponse productResponse = new ProductResponse();
                    productResponse.setAvatarProduct(product.getAvatarProduct());
                    productResponse.setProductName(product.getProductName());
                    productResponse.setProductType(product.getProductType().getProductTypeName());
                    productResponse.setQuantity(product.getQuantity());
                    productResponse.setStoreName(product.getStore().getStoreName());
                    productResponse.setDescription(product.getDescription());
                    productResponse.setPrice(product.getPrice());
                    productResponse.setStatus(product.getStatus());
                    productResponse.setDiscount(product.getDiscount());
                    productResponse.setView(product.getView());
                    return productResponse;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public Optional<ProductResponse> findbyId(int id) {
        return productRepo.findById(id).map(
                product -> {
                    product.setView(product.getView()+1);
                    productRepo.save(product);
                    ProductResponse productResponse = new ProductResponse();
                    productResponse.setAvatarProduct(product.getAvatarProduct());
                    productResponse.setProductName(product.getProductName());
                    productResponse.setProductType(product.getProductType().getProductTypeName());
                    productResponse.setQuantity(product.getQuantity());
                    productResponse.setStoreName(product.getStore().getStoreName());
                    productResponse.setDescription(product.getDescription());
                    productResponse.setPrice(product.getPrice());
                    productResponse.setStatus(product.getStatus());
                    productResponse.setDiscount(product.getDiscount());
                    productResponse.setView(product.getView());
                    return productResponse;
                }
        );
    }

    @Override
    public List<ProductResponse> SearchByKey(String keyword) {
        return productRepo.SearchByKey(keyword).stream().map(
                product -> {
                    ProductResponse productResponse = new ProductResponse();
                    productResponse.setAvatarProduct(product.getAvatarProduct());
                    productResponse.setProductName(product.getProductName());
                    productResponse.setProductType(product.getProductType().getProductTypeName());
                    productResponse.setQuantity(product.getQuantity());
                    productResponse.setStoreName(product.getStore().getStoreName());
                    productResponse.setDescription(product.getDescription());
                    productResponse.setPrice(product.getPrice());
                    productResponse.setStatus(product.getStatus());
                    productResponse.setDiscount(product.getDiscount());
                    productResponse.setView(product.getView());
                    return productResponse;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> findByProductType(int producttypeid) {
        return productRepo.findByProductType(producttypeid).stream().map(
                product -> {
                    ProductResponse productResponse = new ProductResponse();
                    productResponse.setAvatarProduct(product.getAvatarProduct());
                    productResponse.setProductName(product.getProductName());
                    productResponse.setProductType(product.getProductType().getProductTypeName());
                    productResponse.setQuantity(product.getQuantity());
                    productResponse.setStoreName(product.getStore().getStoreName());
                    productResponse.setDescription(product.getDescription());
                    productResponse.setPrice(product.getPrice());
                    productResponse.setStatus(product.getStatus());
                    productResponse.setDiscount(product.getDiscount());
                    productResponse.setView(product.getView());
                    return productResponse;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public Optional<Product> findById(int productid) {
        return productRepo.findById(productid);
    }

    @Override
    public Optional<Product> findByName(int storeid, String name) {
        return productRepo.findByName(storeid, name);
    }

    @Override
    public void saveProduct(AddProductRequest addProductRequest) {
        Store store = storeRepo.findStoreById(addProductRequest.getStoreId()).get();
        System.out.println("here1");
        ProductType productType = productTypeRepo.findProductTypeByName(addProductRequest.getProducttypename(), addProductRequest.getStoreId()).get();
        Product newObj = Product.builder()
                .avatarProduct(addProductRequest.getAvatar())
                .discount(0)
                .store(store)
                .view(0)
                .status(0)
                .productType(productType)
                .createdAt(UserUtils.getCurrentDay())
                .updatedAt(UserUtils.getCurrentDay())
                .productName(addProductRequest.getName())
                .description(addProductRequest.getDescription())
                .price(addProductRequest.getPrice())
                .quantity(0)
                .build();
        System.out.println(newObj.getAvatarProduct()+newObj.getProductName());
        productRepo.save(newObj);
        System.out.println("hihi");
    }

    @Override
    public Page<ProductResponse> findAllProductPaginable(Pageable pageable, int storeId) {
        Page<Product> products = productRepo.getAllByStoreWithPagination(storeId,pageable);
        List<ProductResponse> productResponseList = products
                .stream()
                .map(ProductService::convertToProductResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(productResponseList, pageable, products.getTotalElements());
    }

    @Override
    public Page<ProductResponse> findByTitleContainingIgnoreCase(String keyword, Pageable pageable, int storeId) {
        Page<Product> products = productRepo.findByTitleContainingIgnoreCase(storeId, keyword, pageable);
        List<ProductResponse> productResponseList = products
                .stream()
                .map(ProductService::convertToProductResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(productResponseList, pageable, products.getTotalElements());
    }


    public static ProductResponse convertToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setProductId(product.getProductId());
        response.setAvatarProduct(product.getAvatarProduct());
        response.setDiscount(product.getDiscount());
        response.setPrice(product.getPrice());
        response.setProductName(product.getProductName());
        response.setQuantity(product.getQuantity());
        response.setStatus(product.getStatus());
        response.setDescription(product.getDescription());
        response.setView(product.getView());
        // Lấy tên của cửa hàng từ đối tượng Store
        if (product.getStore() != null) {
            response.setStoreName(product.getStore().getStoreName());
        }
        // Lấy tên loại sản phẩm từ đối tượng ProductType
        if (product.getProductType() != null) {
            response.setProductType(product.getProductType().getProductTypeName());
        }
        return response;
    }
}
