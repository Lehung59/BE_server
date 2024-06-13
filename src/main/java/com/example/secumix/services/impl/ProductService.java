package com.example.secumix.services.impl;



import com.example.secumix.exception.CustomException;
import com.example.secumix.Utils.UserUtils;
import com.example.secumix.entities.Product;
import com.example.secumix.entities.ProductType;
import com.example.secumix.entities.Store;
import com.example.secumix.payload.request.AddProductRequest;
import com.example.secumix.payload.request.ProductRequest;
import com.example.secumix.payload.response.ProductResponse;

import com.example.secumix.repository.ProductTypeRepo;
import com.example.secumix.repository.StoreRepo;
import com.example.secumix.services.IProductService;
import com.example.secumix.repository.ProductRepo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.secumix.entities.Role.CUSTOMER;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private StoreRepo storeRepo;
    @Autowired
    private ProductTypeRepo productTypeRepo;
    @Autowired
    private UserUtils userUtils;

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
                    productResponse.setStatus(product.isStatus());
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
                    productResponse.setStatus(product.isStatus());
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
                    userUtils.getRole();
                    if(userUtils.getRole().contains("ROLE_USER")){
                        product.setView(product.getView()+1);
                        productRepo.save(product);
                    }

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
                    productResponse.setStatus(product.isStatus());
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
                    productResponse.setStatus(product.isStatus());
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
        ProductType productType = productTypeRepo.findProductTypeByName(addProductRequest.getProducttypename(), addProductRequest.getStoreId()).get();
        Product newObj = Product.builder()
                .avatarProduct(addProductRequest.getAvatar())
                .discount(0)
                .store(store)
                .view(0)
                .status(false)
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

    @Override
    public void updateProduct(ProductRequest productRequest) {
        Optional<Product> checkName = productRepo.findByName(productRequest.getStoreId(), productRequest.getProductName());
        if (checkName.isPresent() && !Objects.equals(checkName.get().getProductName(), productRequest.getProductName())) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Tên sản phẩm đã tồn tại");
        }

        Product product = checkName.get();

        if(productRequest.isStatus() != product.isStatus())
            product.setStatus(productRequest.isStatus());
        if(productRequest.getDiscount() != product.getDiscount())
            product.setDiscount(productRequest.getDiscount());
        if(productRequest.getPrice() != product.getPrice())
            product.setPrice(productRequest.getPrice());
        if(product.getProductName() != productRequest.getProductName())
            product.setProductName(productRequest.getProductName());
        if(productRequest.getDescription() != productRequest.getDescription())
            product.setDescription(productRequest.getDescription());
        if(productRequest.getProductTypeId() != product.getProductType().getProductTypeId())
            product.setProductId(productRequest.getProductTypeId());
        if (productRequest.getAvatar() != product.getAvatarProduct())
            product.setAvatarProduct(productRequest.getAvatar());
        if(productRequest.getQuantity() != product.getQuantity())
            product.setQuantity(productRequest.getQuantity());

        product.setUpdatedAt(UserUtils.getCurrentDay());
        productRepo.save(product);






    }


    public static ProductResponse convertToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setProductId(product.getProductId());
        response.setAvatarProduct(product.getAvatarProduct());
        response.setDiscount(product.getDiscount());
        response.setPrice(product.getPrice());
        response.setProductName(product.getProductName());
        response.setQuantity(product.getQuantity());
        response.setStatus(product.isStatus());
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