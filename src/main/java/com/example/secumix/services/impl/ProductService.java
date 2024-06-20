package com.example.secumix.services.impl;


import com.example.secumix.entities.User;
import com.example.secumix.exception.CustomException;
import com.example.secumix.Utils.UserUtils;
import com.example.secumix.entities.Product;
import com.example.secumix.entities.ProductType;
import com.example.secumix.entities.Store;
import com.example.secumix.notify.Notify;
import com.example.secumix.notify.NotifyRepository;
import com.example.secumix.payload.request.AddProductRequest;
import com.example.secumix.payload.request.ProductRequest;
import com.example.secumix.payload.response.ProductResponse;

import com.example.secumix.repository.ProductTypeRepo;
import com.example.secumix.repository.StoreRepo;
import com.example.secumix.repository.UserRepository;
import com.example.secumix.services.IProductService;
import com.example.secumix.repository.ProductRepo;


import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Collectors;

import static com.example.secumix.entities.Product.ChannelStatus.FORBIDDEN;
import static com.example.secumix.entities.Product.ChannelStatus.UNSELL;
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

    @Autowired
    private NotifyRepository notifyRepository;
    @Autowired
    private UserRepository userRepository;

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
                    productResponse.setStoreId(product.getStore().getStoreId());
                    productResponse.setProductTypeId(product.getProductType().getProductTypeId());
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
                    productResponse.setStoreId(product.getStore().getStoreId());
                    productResponse.setProductTypeId(product.getProductType().getProductTypeId());
                    return productResponse;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public Product findById(int id) {
        Product product = productRepo.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"Khong tim thay san pham"));
        return product;
    }

    @Override
    public ProductResponse findbyId(int id) {

        Product product = productRepo.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Khong tim thay san pham"));
        String role = userUtils.getRole();
        if (userUtils.getRole().contains("USER")) {
            product.setView(product.getView() + 1);
            product = productRepo.save(product);
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
        productResponse.setStatus(product.getStatus());
        productResponse.setDiscount(product.getDiscount());
        productResponse.setView(product.getView());
        productResponse.setStoreId(product.getStore().getStoreId());
        productResponse.setProductTypeId(product.getProductType().getProductTypeId());
        return productResponse;


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
                    productResponse.setStoreId(product.getStore().getStoreId());
                    productResponse.setProductTypeId(product.getProductType().getProductTypeId());
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
                    productResponse.setStoreId(product.getStore().getStoreId());
                    productResponse.setProductTypeId(product.getProductType().getProductTypeId());
                    productResponse.setView(product.getView());
                    return productResponse;
                }
        ).collect(Collectors.toList());
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
                .status(UNSELL.getValue())
                .deleted(false)
                .productType(productType)
                .createdAt(UserUtils.getCurrentDay())
                .updatedAt(UserUtils.getCurrentDay())
                .productName(addProductRequest.getName())
                .description(addProductRequest.getDescription())
                .price(addProductRequest.getPrice())
                .quantity(0)
                .build();
        System.out.println(newObj.getAvatarProduct() + newObj.getProductName());
        productRepo.save(newObj);
    }

    @Override
    public Page<Product> findAllProductPaginable(int storeId, int page, int size) {
        Pageable paging = PageRequest.of(page - 1, size);
        return productRepo.getAllByStoreWithPagination(storeId, paging);
    }

    @Override
    public Page<Product> findByTitleContainingIgnoreCase(String keyword, int storeId, int page, int size) {
        Pageable paging = PageRequest.of(page - 1, size);
        return productRepo.findByTitleContainingIgnoreCase(storeId, keyword, paging);
    }

    @Override
    public Page<Product> findAllProductPaginableAdmin(int storeId, int page, int size) {
        Pageable paging = PageRequest.of(page - 1, size);
        return productRepo.getAllByStoreWithPaginationAdmin(storeId, paging);
    }

    @Override
    public Page<Product> findByTitleContainingIgnoreCaseAdmin(String keyword, int storeId, int page, int size) {
        Pageable paging = PageRequest.of(page - 1, size);
        return productRepo.findByTitleContainingIgnoreCaseAdmin(storeId, keyword, paging);
    }

    @Override
    public void updateProduct(ProductRequest productRequest) {
        Optional<Product> checkName = productRepo.findByName(productRequest.getStoreId(), productRequest.getProductName());
        if (checkName.isPresent() && !Objects.equals(checkName.get().getProductName(), productRequest.getProductName())) {
            throw new CustomException(HttpStatus.NOT_IMPLEMENTED, "Tên sản phẩm đã tồn tại");
        }
        ProductType productType = productTypeRepo.findById(productRequest.getProductTypeId()).get();
        Product product = productRepo.findById(productRequest.getProductId()).get();

        product.setStatus(productRequest.getStatus());
        product.setDiscount(productRequest.getDiscount());
        product.setPrice(productRequest.getPrice());
        product.setProductName(productRequest.getProductName());
        product.setDescription(productRequest.getDescription());
        product.setProductType(productType);
        product.setAvatarProduct(productRequest.getAvatar());
        product.setQuantity(productRequest.getQuantity());

        product.setUpdatedAt(UserUtils.getCurrentDay());
        productRepo.save(product);


    }

    @Override
    public void deleteProduct(int productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Khong tin thay san pham"));
        product.setDeleted(true);
        product.setStatus(Product.ChannelStatus.UNSELL.getValue());
        productRepo.save(product);
    }

    @Override
    public void banProduct(int productId, String banReason) {
        Product product = productRepo.findByIdAdmin(productId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Khong tin thay san pham"));
        product.setBanReason(banReason);
        product.setStatus(FORBIDDEN.getValue());
        productRepo.save(product);
        String emailmanager = product.getStore().getEmailmanager();
        User storeManager = userRepository.findByEmail(emailmanager).get();

        Notify notify = Notify.builder()
                .description("Sản phẩm ".concat(product.getProductName()) + "đã bị khóa. Lí do vì " + banReason + ". Hãy liên hệ với Admin để được hỗ trợ") //Truyền link đến store đó
                .user(storeManager)
                .build();
        notifyRepository.save(notify);
    }

    @Override
    public void unBanProduct(int productId) {
        Product product = productRepo.findByIdAdmin(productId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Khong tin thay san pham"));
        product.setStatus(UNSELL.getValue());
        product.setBanReason(null);
        productRepo.save(product);
        String emailmanager = product.getStore().getEmailmanager();
        User storeManager = userRepository.findByEmail(emailmanager).get();

        Notify notify = Notify.builder()
                .description("Sản phẩm ".concat(product.getProductName()) + " được mở.") //Truyền link đến store đó
                .user(storeManager)
                .build();
        notifyRepository.save(notify);
    }

    @Override
    public void deleteProductAdmin(int productId) {
        Product product = productRepo.findByIdAdmin(productId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Khong tin thay san pham"));
        String emailmanager = product.getStore().getEmailmanager();
/////

        productRepo.deleteProductAdmin(productId);

        /////
        User storeManager = userRepository.findByEmail(emailmanager).get();

        Notify notify = Notify.builder()
                .description("Sản phẩm ".concat(product.getProductName()) + " đã bị xóa khỏi hệ thống.") //Truyền link đến store đó
                .user(storeManager)
                .build();
        notifyRepository.save(notify);


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
        response.setStoreId(product.getStore().getStoreId());
        response.setProductTypeId(product.getProductType().getProductTypeId());
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
