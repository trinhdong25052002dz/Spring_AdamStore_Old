package Spring_AdamStore.controller;

import Spring_AdamStore.dto.request.CartItemRequest;
import Spring_AdamStore.dto.request.CartItemUpdateRequest;
import Spring_AdamStore.dto.response.ApiResponse;
import Spring_AdamStore.dto.response.CartItemResponse;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.service.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "CART-ITEM-CONTROLLER")
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1")
@RestController
public class CartItemController {

    private final CartItemService cartItemService;

    @Operation(description = "API thêm sản phẩm vào giỏ hàng")
    @PostMapping("/cart-items")
    public ApiResponse<CartItemResponse> create(@Valid @RequestBody CartItemRequest request){
        return ApiResponse.<CartItemResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create CartItem")
                .result(cartItemService.create(request))
                .build();
    }


    @GetMapping("/cart-items/{id}")
    public ApiResponse<CartItemResponse> fetchById(@Min(value = 1, message = "ID phải lớn hơn 0")
                                                   @PathVariable Long id){
        return ApiResponse.<CartItemResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch CartItem By Id")
                .result(cartItemService.fetchById(id))
                .build();
    }

    @GetMapping("/cart-items")
    public ApiResponse<PageResponse<CartItemResponse>> fetchAll(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                                @RequestParam(defaultValue = "1") int pageNo,
                                                                @RequestParam(defaultValue = "10") int pageSize,
                                                                @RequestParam(required = false) String sortBy){
        return ApiResponse.<PageResponse<CartItemResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(cartItemService.fetchAll(pageNo, pageSize, sortBy))
                .message("Fetch All CartItems With Pagination")
                .build();
    }

    @PutMapping("/cart-items/{id}")
    public ApiResponse<CartItemResponse> update(@Min(value = 1, message = "ID phải lớn hơn 0")
                                                @PathVariable Long id, @Valid @RequestBody CartItemUpdateRequest request){
        return ApiResponse.<CartItemResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update CartItem By Id")
                .result(cartItemService.update(id, request))
                .build();
    }


    @DeleteMapping("/cart-items/{id}")
    public ApiResponse<Void> delete(@Min(value = 1, message = "ID phải lớn hơn 0")
                                    @PathVariable Long id){
        cartItemService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Delete CartItem By Id")
                .result(null)
                .build();
    }
}
