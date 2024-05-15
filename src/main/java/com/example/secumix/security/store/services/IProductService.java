package com.example.secumix.security.store.services;

import com.example.secumix.security.store.model.entities.Product;
import com.example.secumix.security.store.model.request.AddProductRequest;
import com.example.secumix.security.store.model.response.ProductResponse;
import com.example.secumix.security.store.model.response.StoreCustomerRespone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    List<ProductResponse> getAllProduct();
    List<ProductResponse>GetAllByStore();

    Optional<ProductResponse> findbyId(int id);
    List<ProductResponse> SearchByKey(String keyword);
    List<ProductResponse> findByProductType(int producttypeid);

    Optional<Product> findById(int productid);

    Optional<Product> findByName(int storeid, String name);

    void saveProduct(AddProductRequest addProductRequest);

    Page<ProductResponse> findAllProductPaginable(Pageable paging,int storeId);

    Page<ProductResponse> findByTitleContainingIgnoreCase(String keyword, Pageable paging, int storeid);


}
