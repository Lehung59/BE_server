package com.example.secumix.security.store.controller;




import com.cloudinary.Cloudinary;
import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.notify.Notify;
import com.example.secumix.security.notify.NotifyRepository;
import com.example.secumix.security.store.model.entities.OrderDetail;
import com.example.secumix.security.store.model.entities.Store;
import com.example.secumix.security.store.model.entities.StoreType;
import com.example.secumix.security.store.model.response.OrderDetailResponse;
import com.example.secumix.security.store.model.response.ProductResponse;
import com.example.secumix.security.store.model.response.StoreCustomerRespone;
import com.example.secumix.security.store.repository.*;
import com.example.secumix.security.store.services.ICustomerService;
import com.example.secumix.security.store.services.IOrderDetailService;
import com.example.secumix.security.store.services.IProductService;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/management")
@RequiredArgsConstructor
public class ManagerStoreController {
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


    @GetMapping(value = "/{storeid}/product/view")
    ResponseEntity<ResponseObject>GetAllProductByStore(@PathVariable int storeid,
                                                       @RequestParam(required = false) String keyword,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size){

            List<ProductResponse> products = new ArrayList<ProductResponse>();
            Pageable paging = PageRequest.of(page - 1, size);

            Page<ProductResponse> pageTuts;
            if (keyword == null) {
                pageTuts = productService.findAllProductPaginable(paging,storeid);
            } else {
                pageTuts = productService.findByTitleContainingIgnoreCase(keyword, paging, storeid);
            }

            products = pageTuts.getContent();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Cac san pham torng cua hang cua ban.",products)
        );
    }

    @GetMapping(value = "/store/{storeid}/revenue")
    ResponseEntity<ResponseObject> RevenueByStore(@PathVariable int storeid){
       long  revenue = orderDetailRepo.RevenueByStore(storeid);
       return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
               "OK","Doanh thu ",revenue
       ));
    }

    @PostMapping(value = "info/{storetypeid}/insert")
    public ResponseEntity<ResponseObject> insertStore(@RequestParam String address,
                                                      @RequestParam MultipartFile image,
                                                      @RequestParam String phonenumber,
                                                      @RequestParam String name,
                                                      @PathVariable int storetypeid){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user= userRepository.findByEmail(email).get();
        Optional<Store> store = storeRepo.findByPhonenumber(phonenumber);
        Optional<StoreType> storeType = storeTypeRepo.findById(storetypeid);

        if(store.isPresent())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED","Store phonenumber alreasy exsis","")
            );
        if(storeType.isEmpty())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED","Not exist storetype","")
            );
        Map<String, Object> uploadResult = upload(image);

        Store newObj = Store.builder()
                .address(address)
                .storeName(name)
                .rate(5)
                .emailmanager(email)
                .phoneNumber(phonenumber)
                .storeType(storeType.get())
                .image(uploadResult.get("secure_url").toString())
                .build();
        storeRepo.save(newObj);
        Notify notify= Notify.builder()
                .description("Cửa hàng của bạn đã được mở".concat(newObj.getStoreName())) //Truyền link đến store đó
                .user(user)
                .build();
        notifyRepository.save(notify);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Success","")
        );
    }


}
