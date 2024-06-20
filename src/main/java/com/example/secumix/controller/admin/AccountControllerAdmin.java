//package com.example.secumix.controller.admin;
//
//import com.example.secumix.ResponseObject;
//import com.example.secumix.exception.CustomException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/v1/admin")
//@RequiredArgsConstructor
//public class AccountControllerAdmin {
//    @GetMapping(value= "/account/active")
//    public ResponseEntity<ResponseObject> banProduct(@RequestParam("accountId") int accountId
//                                                    ) {
//        try{
//            productService.banProduct(productId,banReason);
//            ResponseObject responseObject = new ResponseObject(
//                    "OK",
//                    "Khoa thanh cong san pham ."+productId,
//                    ""
//            );
//            return ResponseEntity.status(HttpStatus.OK).body(responseObject);
//
//        } catch (CustomException ex) {
//            return ResponseEntity.status(ex.getStatus())
//                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
//        }
//    }
//}
