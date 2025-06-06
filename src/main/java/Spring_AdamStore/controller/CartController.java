package Spring_AdamStore.controller;

import Spring_AdamStore.dto.response.ApiResponse;
import Spring_AdamStore.dto.response.CartItemResponse;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "CART-CONTROLLER")
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1")
@RestController
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Fetched paginated cart items for current user",
    description = "API để lấy tất cả sản phẩm đã thêm vào giỏ hàng")
    @GetMapping("/carts/cart-items")
    public ApiResponse<PageResponse<CartItemResponse>> getCartItemsOfCurrentUser(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                                                     @RequestParam(defaultValue = "1") int pageNo,
                                                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                                                 @RequestParam(required = false) String sortBy) {
        return ApiResponse.<PageResponse<CartItemResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Fetched paginated cart items for current user")
                .result(cartService.getCartItemsOfCurrentUser(pageNo, pageSize, sortBy))
                .build();
    }
}
