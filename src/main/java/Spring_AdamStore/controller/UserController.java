package Spring_AdamStore.controller;

import Spring_AdamStore.dto.request.UserCreationRequest;
import Spring_AdamStore.dto.request.UserUpdateRequest;
import Spring_AdamStore.dto.response.*;
import Spring_AdamStore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "USER-CONTROLLER")
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1")
@RestController
public class UserController {

    private final UserService userService;


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create User with Role",
            description = "API này được sử dụng để tạo user và gán role vào user đó")
    @PostMapping("/users")
    public ApiResponse<UserResponse> create(@Valid @RequestBody UserCreationRequest request){
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create User With Role")
                .result(userService.create(request))
                .build();
    }


    @GetMapping("/users/{id}")
    public ApiResponse<UserResponse> fetchById(@Min(value = 1, message = "ID phải lớn hơn 0")
                                               @PathVariable long id){
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch User By Id")
                .result(userService.fetchUserById(id))
                .build();
    }

    @Operation(summary = "Fetch All Users For Admin")
    @GetMapping("/users")
    public ApiResponse<PageResponse<UserResponse>> fetchAll(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                            @RequestParam(defaultValue = "1") int pageNo,
                                                            @RequestParam(defaultValue = "10") int pageSize,
                                                            @RequestParam(required = false) String sortBy){
        return ApiResponse.<PageResponse<UserResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(userService.fetchAllUsers(pageNo, pageSize, sortBy))
                .message("Fetch All Users For Admin")
                .build();
    }

    @Operation(summary = "Update User (No update Password)",
            description = "API này được sử dụng để update user")
    @PutMapping("/users/{id}")
    public ApiResponse<UserResponse> update(@Min(value = 1, message = "ID phải lớn hơn 0")
                                            @PathVariable long id,@Valid @RequestBody UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update User By Id")
                .result(userService.update(id, request))
                .build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Soft Delete User")
    @DeleteMapping("/users/{id}")
    public ApiResponse<Void> delete(@Min(value = 1, message = "ID phải lớn hơn 0")
                                    @PathVariable long id){
        userService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Soft Delete User By Id")
                .result(null)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Restore User")
    @PatchMapping("/users/{id}/restore")
    public ApiResponse<UserResponse> restore(@Min(value = 1, message = "ID phải lớn hơn 0")
                                    @PathVariable long id){
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Restore User By Id")
                .result(userService.restore(id))
                .build();
    }

    @Operation(summary = "Fetch All Addresses For User",
            description = "Api lấy tất cả địa chỉ của user")
    @GetMapping("/users/addresses")
    public ApiResponse<PageResponse<AddressResponse>> getAddressesByUser(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                                               @RequestParam(defaultValue = "1") int pageNo,
                                                                           @RequestParam(defaultValue = "10") int pageSize,
                                                                           @RequestParam(required = false) String sortBy) {
        return ApiResponse.<PageResponse<AddressResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch Addresses By User Id With Pagination")
                .result(userService.getAllAddressesByUser(pageNo, pageSize, sortBy))
                .build();
    }

    @Operation(summary = "Fetch Promotions By User",
            description = "Api lấy tất cả mã giảm giá mà user có thể sử dụng")
    @GetMapping("/users/promotions/available")
    public ApiResponse<PageResponse<PromotionResponse>> getPromotionsByUser(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                                                  @RequestParam(defaultValue = "1") int pageNo,
                                                                              @RequestParam(defaultValue = "10") int pageSize,
                                                                              @RequestParam(required = false) String sortBy){
        return ApiResponse.<PageResponse<PromotionResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(userService.getPromotionsByUser(pageNo, pageSize, sortBy))
                .message("Fetch Promotions By User With Pagination")
                .build();
    }

}
