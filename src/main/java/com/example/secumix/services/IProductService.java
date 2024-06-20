package com.example.secumix.services;

import com.example.secumix.entities.Product;
import com.example.secumix.payload.request.AddProductRequest;
import com.example.secumix.payload.request.ProductRequest;
import com.example.secumix.payload.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    List<ProductResponse> getAllProduct();
    List<ProductResponse>GetAllByStore();

    Product findById(int id);
    ProductResponse findbyId(int id);
    List<ProductResponse> SearchByKey(String keyword);
    List<ProductResponse> findByProductType(int producttypeid);

    Optional<Product> findByName(int storeid, String name);

    void saveProduct(AddProductRequest addProductRequest);

    Page<Product> findAllProductPaginable(int storeId, int page, int size);

    Page<Product> findByTitleContainingIgnoreCase(String keyword, int storeid, int page, int size);

    Page<Product> findAllProductPaginableAdmin(int storeId, int page, int size);

    Page<Product> findByTitleContainingIgnoreCaseAdmin(String keyword, int storeid, int page, int size);


    void updateProduct(ProductRequest productRequest);

    void deleteProduct(int productId);

    void banProduct(int productId, String banReason);

    void unBanProduct(int productId);

    void deleteProductAdmin(int productId);
}
