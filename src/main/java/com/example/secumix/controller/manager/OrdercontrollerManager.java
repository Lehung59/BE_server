package com.example.secumix.controller.manager;

import com.example.secumix.Utils.DtoMapper.OrderDetailMapper;
import com.example.secumix.constants.Constants;
import com.example.secumix.entities.OrderDetail;
import com.example.secumix.exception.CustomException;
import com.example.secumix.ResponseObject;
import com.example.secumix.payload.Pagination;
import com.example.secumix.payload.dtos.OrderDetailDto;
import com.example.secumix.payload.response.OrderDetailResponse;
import com.example.secumix.services.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/management")
@RequiredArgsConstructor

public class OrdercontrollerManager {
    private final IOrderDetailService orderDetailService;
    private final OrderDetailMapper orderDetailMapper;

    @GetMapping(value = "/{storeid}/order/view")
    public ResponseEntity<ResponseObject> GetAllOrderDetailByStore(@PathVariable int storeid,
                                                                   @RequestParam(value = "keyword", required = false) String keyword,
                                                                   @RequestParam(defaultValue = Constants.PAGE) int page,
                                                                   @RequestParam(defaultValue = Constants.SIZE) int size) {

        Page<OrderDetail> orderDetails;
        if (keyword == null) {
            orderDetails = orderDetailService.findAllOrderPaginable(storeid, page, size);
        } else {
            orderDetails = orderDetailService.findByTitleContainingIgnoreCase(keyword, storeid, page, size);
        }

        List<OrderDetailResponse> orderDetailResponses = orderDetails.getContent().stream()
                .map(orderDetailMapper::convertToOrderDetailResponse)
                .collect(Collectors.toList());

        Pagination pagination = new Pagination(
                orderDetails.getTotalElements(),
                orderDetails.getTotalPages(),
                orderDetails.getNumber() + 1,
                orderDetails.getSize()
        );

        ResponseObject responseObject = new ResponseObject(
                "OK",
                "Các sản phẩm trong cửa hàng của bạn.",
                orderDetailResponses,
                pagination
        );

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @GetMapping(value = "/{storeid}/orderdetail/view/{orderDetailId}")
    ResponseEntity<ResponseObject> ViewOrderDetailByStore(@PathVariable int orderDetailId,
                                                            @PathVariable int storeid){


        try{

            OrderDetailDto orderDetailDto = orderDetailService.findDtoById(orderDetailId);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Chi tiet don hang "+orderDetailId,orderDetailDto)
            );
        }catch (CustomException ex){
            return ResponseEntity.status(ex.getStatus())
                    .body(new ResponseObject("FAILED", ex.getMessage(), ""));
        }

    }


}
