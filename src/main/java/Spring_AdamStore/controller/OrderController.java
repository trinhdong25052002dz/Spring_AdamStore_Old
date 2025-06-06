package Spring_AdamStore.controller;

import Spring_AdamStore.dto.request.OrderRequest;
import Spring_AdamStore.dto.request.PaymentCallbackRequest;
import Spring_AdamStore.dto.request.ShippingRequest;
import Spring_AdamStore.dto.request.UpdateOrderAddressRequest;
import Spring_AdamStore.dto.response.*;
import Spring_AdamStore.service.OrderService;
import Spring_AdamStore.service.PaymentService;
import Spring_AdamStore.service.ShippingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "ORDER-CONTROLLER")
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1")
@RestController
public class OrderController {

    private final OrderService orderService;
    private final ShippingService shippingService;
    private final PaymentService paymentService;


    @Operation(summary = "Create Order",
            description = "Api này dùng để tạo đơn hàng")
    @PostMapping("/orders")
    public ApiResponse<OrderResponse> create(@Valid @RequestBody OrderRequest request){
        return ApiResponse.<OrderResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create Order")
                .result(orderService.create(request))
                .build();
    }


    @GetMapping("/orders/{id}")
    public ApiResponse<OrderResponse> fetchById(@Min(value = 1, message = "ID phải lớn hơn 0")
                                                    @PathVariable Long id){
        return ApiResponse.<OrderResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch Order By Id")
                .result(orderService.fetchById(id))
                .build();
    }

    @GetMapping("/orders")
    public ApiResponse<PageResponse<OrderResponse>> fetchAll(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                                 @RequestParam(defaultValue = "1") int pageNo,
                                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                                 @RequestParam(required = false) String sortBy){
        return ApiResponse.<PageResponse<OrderResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(orderService.fetchAll(pageNo, pageSize, sortBy))
                .message("Fetch All Orders With Pagination")
                .build();
    }

    @Operation(summary = "Search Order For Current User Or Admin",
            description = "Search Order cho User hiện tại hoặc Admin dựa vào token")
    @GetMapping("/orders/search")
    public ApiResponse<PageResponse<OrderResponse>> searchOrder(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                                    @RequestParam(defaultValue = "1") int pageNo,
                                                                    @RequestParam(defaultValue = "10") int pageSize,
                                                                    @RequestParam(required = false) String sortBy,
                                                                    @RequestParam(required = false) List<String> search){
        return ApiResponse.<PageResponse<OrderResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(orderService.searchOrder(pageNo, pageSize, sortBy, search))
                .message("Search Order For Current User Or Admin")
                .build();
    }

    @Operation(summary = "Update Address for Order",
    description = "Cập nhập đia chỉ cho đơn hàng ở trạng thái PENDING hoặc PROCESSING")
    @PutMapping("/orders/{orderId}/address")
    public ApiResponse<OrderResponse> updateAddress(@Min(value = 1, message = "orderId phải lớn hơn 0")
                                                 @PathVariable Long orderId, @Valid @RequestBody UpdateOrderAddressRequest request){
        return ApiResponse.<OrderResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update Order By Id")
                .result(orderService.updateAddress(orderId, request))
                .build();
    }


    @DeleteMapping("/orders/{id}")
    public ApiResponse<Void> delete(@Min(value = 1, message = "ID phải lớn hơn 0")
                                    @PathVariable Long id){
        orderService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Delete Order By Id")
                .result(null)
                .build();
    }

    @Operation(summary = "Calculate Shipping Fee",
    description = "Api này dùng để tính phí ship của đơn hàng")
    @PostMapping("/shipping/calculate-fee")
    public ApiResponse<ShippingFeeResponse> calculateShippingFee(@RequestBody ShippingRequest request){
        return ApiResponse.<ShippingFeeResponse>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Calculate Shipping Fee")
                .result(shippingService.shippingCost(request))
                .build();
    }

    @Operation(summary = "Payment for Order",
            description = "Api này dùng để thanh toán đơn hàng thông qua VNPAY")
    @GetMapping("/orders/{orderId}/vn-pay")
    public ApiResponse<VNPayResponse> pay(@Min(value = 1, message = "ID phải lớn hơn 0")
                                              @PathVariable Long orderId, HttpServletRequest request) {
        return ApiResponse.<VNPayResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Tạo thành công URL thanh toán VNPay")
                .result(paymentService.processPayment(orderId, request))
                .build();
    }

    @Operation(summary = "Payment CallBack for Order",
            description = "Api này dùng để xử lý sau khi thanh toán đơn hàng")
    @PostMapping("/orders/vn-pay-callback")
    public ApiResponse<OrderResponse> payCallbackHandler(@Valid @RequestBody PaymentCallbackRequest request) {
        String status = request.getResponseCode();
        if (status.equals("00")) {
            return ApiResponse.<OrderResponse>builder()
                    .code(1000)
                    .message("Thanh toán thành công")
                    .result(paymentService.updateOrderAfterPayment(request))
                    .build();
        } else {
            log.error("Thanh toán không thành công với mã phản hồi: " + status);
            paymentService.handleFailedPayment(request);
            return new ApiResponse<>(4000, "Thanh toán thất bại", null);
        }
    }

    @Operation(summary = "Retry Payment for Order",
            description = "Api này dùng để thanh toán lại đơn hàng(khi đang trong phần chờ thanh toán)")
    @GetMapping("/orders/{orderId}/retry-payment")
    public ApiResponse<VNPayResponse> retryPayment(@Min(value = 1, message = "ID phải lớn hơn 0")
                                                       @PathVariable Long orderId, HttpServletRequest request) {
        return ApiResponse.<VNPayResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Tạo thành công URL thanh toán VNPay")
                .result(paymentService.retryPayment(orderId, request))
                .build();
    }

}
