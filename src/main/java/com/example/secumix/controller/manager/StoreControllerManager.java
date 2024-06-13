package com.example.secumix.controller.manager;

import com.cloudinary.Cloudinary;
import com.example.secumix.repository.*;
import com.example.secumix.exception.CustomException;
import com.example.secumix.ResponseObject;
import com.example.secumix.Utils.ImageUpload;
import com.example.secumix.notify.Notify;
import com.example.secumix.notify.NotifyRepository;
import com.example.secumix.entities.Store;
import com.example.secumix.entities.StoreType;
import com.example.secumix.payload.request.StoreInfoEditRequest;
import com.example.secumix.payload.response.StoreInfoView;
import com.example.secumix.services.ICustomerService;
import com.example.secumix.services.IOrderDetailService;
import com.example.secumix.services.IProductService;
import com.example.secumix.services.IStoreService;
import com.example.secumix.entities.User;
import com.example.secumix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/management")
@RequiredArgsConstructor
public class StoreControllerManager {
    private final IProductService productService;
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
    private final IStoreService storeService;
    private final ImageUpload imageUpload;
    @Value("${default_avt}")
    private String defaultAvt;

    @GetMapping(value = "/store/revenue")
    ResponseEntity<ResponseObject> RevenueByStore(@RequestParam int storeid) {
        long revenue = orderDetailRepo.RevenueByStore(storeid);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                "OK", "Doanh thu ", revenue
        ));
    }

    @GetMapping(value = "{storeid}/info/view")
    public ResponseEntity<ResponseObject> viewStoreInfo(@PathVariable int storeid) {
        try{
            storeService.checkStoreAuthen(storeid);

            StoreInfoView storeInfoView = storeService.getInfo(storeid);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Success", storeInfoView)
            );
        } catch (CustomException ex){
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
    }

    @PutMapping(value = "{storeid}/info/edit")
    public ResponseEntity<ResponseObject> editStoreInfo(@PathVariable int storeid,
                                                        @RequestParam(required = false) String address,
                                                        @RequestParam(required = false) String phonenumber,
                                                        @RequestParam(required = false) String storename,
                                                        @RequestParam(required = false) MultipartFile image
                                                        ) {
        try {
            storeService.checkStoreAuthen(storeid);

            String uploadResult = imageUpload.upload(image);
            StoreInfoEditRequest storeInfoEditRequest = new StoreInfoEditRequest(storeid, storename,address,phonenumber, uploadResult);
            storeService.updateInfo(storeInfoEditRequest);


        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Success", "")
        );

    }

    @PostMapping(value = "/info/insert")
    public ResponseEntity<ResponseObject> insertStoreInfo(@RequestParam String address,
                                                          @RequestParam(required = false) MultipartFile image,
                                                          @RequestParam String phonenumber,
                                                          @RequestParam String name,
                                                          @RequestParam int storetypeid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email).get();
        Optional<Store> store = storeRepo.findByPhonenumber(phonenumber);
        Optional<StoreType> storeType = storeTypeRepo.findById(storetypeid);
        if(storeRepo.findStoreByName(name).isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("FAILED", "Tên cửa hàng đã tồn tại", "")
            );
        }
        if (store.isPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("FAILED", "Số điện thoại này đã tồn tại", "")
            );
        if (storeType.isEmpty())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Không tồn tại loại cửa hàng này", "")
            );
        String img = defaultAvt;
        if(!image.isEmpty())
            img = imageUpload.upload(image);
        Store newObj = Store.builder()
                .address(address)
                .storeName(name)
                .rate(5)
                .emailmanager(email)
                .phoneNumber(phonenumber)
                .storeType(storeType.get())
                .image(img)
                .build();
        Store newStore = storeRepo.save(newObj);
        Notify notify = Notify.builder()
                .description("Cửa hàng của bạn đã được mở".concat(newObj.getStoreName())) //Truyền link đến store đó
                .user(user)
                .build();
        notifyRepository.save(notify);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Success", newStore)
        );
    }


}
