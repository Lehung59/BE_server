package com.example.secumix.controller.admin;

import com.example.secumix.ResponseObject;
import com.example.secumix.constants.Constants;
import com.example.secumix.entities.Product;
import com.example.secumix.exception.CustomException;
import com.example.secumix.payload.Pagination;
import com.example.secumix.payload.response.ProductResponse;
import com.example.secumix.services.IProductService;
import com.example.secumix.services.impl.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class ProductControllerAdmin {

    private final IProductService productService;

    @GetMapping(value= "/product/ban")
    public ResponseEntity<ResponseObject> banProduct(@RequestParam("productId") int productId,
                                                     @RequestParam String banReason) {
        try{
            productService.banProduct(productId,banReason);
            ResponseObject responseObject = new ResponseObject(
                    "OK",
                    "Khoa thanh cong san pham ."+productId,
                    ""
            );
            return ResponseEntity.status(HttpStatus.OK).body(responseObject);

        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }
    @DeleteMapping(value = "/product/delete")
    public ResponseEntity<ResponseObject> deleteProduct(@RequestParam("productId") int productId){
        try{
            productService.deleteProductAdmin(productId);
            ResponseObject responseObject = new ResponseObject(
                    "OK",
                    "Xoa thanh cong."+productId,
                    ""
            );
            return ResponseEntity.status(HttpStatus.OK).body(responseObject);

        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }



    @GetMapping(value= "/product/open")
    public ResponseEntity<ResponseObject> openProduct(@RequestParam("productId") int productId) {
        try{
            productService.unBanProduct(productId);
            ResponseObject responseObject = new ResponseObject(
                    "OK",
                    "Mo khoa thanh cong san pham ."+productId,
                    ""
            );
            return ResponseEntity.status(HttpStatus.OK).body(responseObject);

        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }

    @GetMapping(value = "/store/product/view")
    public ResponseEntity<ResponseObject> getAllProductByStore(
            @RequestParam int storeid,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = Constants.PAGE) int page,
            @RequestParam(defaultValue = Constants.SIZE) int size) {

        try {
            Page<Product> products;

            if (keyword == null) {
                products = productService.findAllProductPaginableAdmin(storeid, page, size);
            } else {
                products = productService.findByTitleContainingIgnoreCaseAdmin(keyword, storeid, page, size);
            }

            List<ProductResponse> productResponses = products.getContent().stream()
                    .map(ProductService::convertToProductResponse)
                    .collect(Collectors.toList());

            Pagination pagination = new Pagination(
                    products.getTotalElements(),
                    products.getTotalPages(),
                    products.getNumber() + 1,
                    products.getSize()
            );

            ResponseObject responseObject = new ResponseObject(
                    "OK",
                    "Cac san pham cua cua hang ."+storeid,
                    productResponses,
                    pagination
            );

            return ResponseEntity.status(HttpStatus.OK).body(responseObject);

        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }

}
