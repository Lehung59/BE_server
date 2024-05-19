package com.example.secumix.security.store.controller.manager;

import com.cloudinary.Cloudinary;
import com.example.secumix.security.Exception.CustomException;
import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.notify.Notify;
import com.example.secumix.security.notify.NotifyRepository;
import com.example.secumix.security.store.model.entities.Store;
import com.example.secumix.security.store.model.entities.StoreType;
import com.example.secumix.security.store.model.request.StoreInfoEditRequest;
import com.example.secumix.security.store.repository.*;
import com.example.secumix.security.store.services.ICustomerService;
import com.example.secumix.security.store.services.IOrderDetailService;
import com.example.secumix.security.store.services.IProductService;
import com.example.secumix.security.store.services.IStoreService;
import com.example.secumix.security.store.services.impl.StoreService;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
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

    public Map upload(MultipartFile file) {
        try {
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            return data;
        } catch (IOException io) {
            throw new RuntimeException("Image upload fail");
        }
    }

    @GetMapping(value = "/store/revenue")
    ResponseEntity<ResponseObject> RevenueByStore(@RequestParam int storeid) {
        long revenue = orderDetailRepo.RevenueByStore(storeid);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                "OK", "Doanh thu ", revenue
        ));
    }

    @PutMapping(value = "{storeid}/info/edit")
    public ResponseEntity<ResponseObject> editStoreInfo(@PathVariable int storeid,
                                                        @RequestParam(required = false) String address,
                                                        @RequestParam(required = false) String phonenumber,
                                                        @RequestParam(required = false) String storename
    ) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            Store store = storeService.findStoreById(storeid)
                    .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Khong ton tai cua hang"));
            if (!store.getEmailmanager().equals(email)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ResponseObject("FAILED","Khong phai cua hang cua ban" , ""));
            }
            StoreInfoEditRequest storeInfoEditRequest = new StoreInfoEditRequest(storeid, storename,address,phonenumber);
            storeService.updateInfo(storeInfoEditRequest);


        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Success", "")
        );

    }

    @PostMapping(value = "/info/{storetypeid}/insert")
    public ResponseEntity<ResponseObject> insertStoreInfo(@RequestParam String address,
                                                          @RequestParam MultipartFile image,
                                                          @RequestParam String phonenumber,
                                                          @RequestParam String name,
                                                          @PathVariable int storetypeid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email).get();
        Optional<Store> store = storeRepo.findByPhonenumber(phonenumber);
        Optional<StoreType> storeType = storeTypeRepo.findById(storetypeid);

        if (store.isPresent())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Store phonenumber alreasy exsis", "")
            );
        if (storeType.isEmpty())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Not exist storetype", "")
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
        Notify notify = Notify.builder()
                .description("Cửa hàng của bạn đã được mở".concat(newObj.getStoreName())) //Truyền link đến store đó
                .user(user)
                .build();
        notifyRepository.save(notify);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Success", "")
        );
    }


}
