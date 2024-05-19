package com.example.secumix.security.store.controller.manager;

import com.cloudinary.Cloudinary;
import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.notify.NotifyRepository;
import com.example.secumix.security.store.model.response.OrderDetailResponse;
import com.example.secumix.security.store.model.response.StoreCustomerRespone;
import com.example.secumix.security.store.repository.*;
import com.example.secumix.security.store.services.ICustomerService;
import com.example.secumix.security.store.services.IOrderDetailService;
import com.example.secumix.security.store.services.IProductService;
import com.example.secumix.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/management")
@RequiredArgsConstructor
public class CustomerControllerManager {
    private  final IProductService productService;
    private final ImportDetailRepo importDetailRepo;
    private final StoreRepo storeRepo;
    private final OrderDetailRepo orderDetailRepo;
    private final IOrderDetailService orderDetailService;
    private final ProductRepo productRepo;
    private final StoreTypeRepo storeTypeRepo;
    private final NotifyRepository notifyRepository;
    private final Cloudinary cloudinary;
    private final UserRepository userRepository;
    private final ICustomerService customerService;
    private final ProfileDetailRepo profileDetailRepo;

    public Map upload(MultipartFile file)  {
        try{
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            return data;
        }catch (IOException io){
            throw new RuntimeException("Image upload fail");
        }
    }



    @GetMapping(value = "/{storeid}/customer/view")
    ResponseEntity<ResponseObject> viewCustomerByStore(@PathVariable("storeid") int storeid,
                                                       @RequestParam(required = false) String keyword,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        List<StoreCustomerRespone> storeCustomerRespones = new ArrayList<StoreCustomerRespone>();
        Pageable paging = PageRequest.of(page - 1, size);

        Page<StoreCustomerRespone> pageTuts;
        if (keyword == null) {
            pageTuts = customerService.findAllCustomerPaginable(paging,storeid);
        } else {
            pageTuts = customerService.findCustomerByTitleContainingIgnoreCase(keyword, paging, storeid);
        }

        storeCustomerRespones = pageTuts.getContent();
//        List<ProfileDetail> listtt = profileDetailRepo.getAllCustomerByStoreWithPagination(storeid);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Cac khach hang cua ban.", storeCustomerRespones)
        );
    }

    @GetMapping(value = "/{storeid}/customer/{customerid}/detail")
    ResponseEntity<ResponseObject> viewCustomerOrderListByStore(@PathVariable("storeid") int storeid,
                                                                @PathVariable("customerid") int customerid,
                                                                @RequestParam(required = false) String keyword,
                                                                @RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        List<OrderDetailResponse> orderDetailResponses = new ArrayList<OrderDetailResponse>();
        Pageable paging = PageRequest.of(page - 1, size);

        Page<OrderDetailResponse> pageTuts;
        if (keyword == null) {
            pageTuts = orderDetailService.findAllOrderByCustomerAndStorePaginable(paging,storeid,customerid);
        } else {
            pageTuts = orderDetailService.findOrderByTitleContainingIgnoreCase(keyword, paging, storeid,customerid);
        }
        String storeName = storeRepo.findStoreById(storeid).get().getStoreName();

//        List<String> orderDetailTitle = orderDetailRepo.findAllOrderByCustomerAndStorePaginable(storeName,customerid).stream();

        orderDetailResponses = pageTuts.getContent();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Các đơn hàng của khách "+customerid, orderDetailResponses)
        );
    }

}
