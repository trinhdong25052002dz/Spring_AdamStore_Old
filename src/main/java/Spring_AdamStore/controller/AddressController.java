package Spring_AdamStore.controller;

import Spring_AdamStore.dto.request.AddressRequest;
import Spring_AdamStore.dto.response.AddressResponse;
import Spring_AdamStore.dto.response.ApiResponse;
import Spring_AdamStore.dto.response.BranchResponse;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "ADDRESS-CONTROLLER")
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1")
@RestController
public class AddressController {

    private final AddressService addressService;

    @Operation(summary = "Create New Address",
            description = "Api dùng để tạo địa chỉ mới")
    @PostMapping("/addresses")
    public ApiResponse<AddressResponse> create(@Valid @RequestBody AddressRequest request){
        return ApiResponse.<AddressResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create Address")
                .result(addressService.create(request))
                .build();
    }


    @GetMapping("/addresses/{id}")
    public ApiResponse<AddressResponse> fetchById(@Min(value = 1, message = "ID phải lớn hơn 0")
                                                 @PathVariable Long id){
        return ApiResponse.<AddressResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch Address By Id")
                .result(addressService.fetchById(id))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Fetch All Addresses For Admin",
    description = "API này để lấy tất cả address bên trong hệ thống")
    @GetMapping("/addresses")
    public ApiResponse<PageResponse<AddressResponse>> fetchAllForAdmin(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                              @RequestParam(defaultValue = "1") int pageNo,
                                                              @RequestParam(defaultValue = "10") int pageSize,
                                                              @RequestParam(required = false) String sortBy){
        return ApiResponse.<PageResponse<AddressResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(addressService.fetchAllForAdmin(pageNo, pageSize, sortBy))
                .message("Fetch All Addresses For Admin")
                .build();
    }

    @Operation(description = "API để update địa chỉ")
    @PutMapping("/addresses/{id}")
    public ApiResponse<AddressResponse> update(@Min(value = 1, message = "ID phải lớn hơn 0")
                                              @PathVariable Long id, @Valid @RequestBody AddressRequest request){
        return ApiResponse.<AddressResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update Address By Id")
                .result(addressService.update(id, request))
                .build();
    }

    @Operation(summary = "Hide User Address",
    description = "Api này cho phép user xóa địa chỉ của chính user đó trong giao diện người dùng")
    @PatchMapping("/addresses/{id}/hide")
    public ApiResponse<Void> hideAddress(@Min(value = 1, message = "ID phải lớn hơn 0")
                                               @PathVariable Long id){
        addressService.hideAddress(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Address has been successfully hidden")
                .result(null)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Soft Delete Address",
    description = "API để Admin xóa mềm address")
    @DeleteMapping("/addresses/{id}")
    public ApiResponse<Void> delete(@Min(value = 1, message = "ID phải lớn hơn 0")
                                    @PathVariable Long id){
        addressService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Delete Address By Id")
                .result(null)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Restore Address",
            description = "Api này để khôi phục Address")
    @PatchMapping("/addresses/{id}/restore")
    public ApiResponse<AddressResponse> restore(@Min(value = 1, message = "Id phải lớn hơn 0")
                                               @PathVariable long id) {
        return ApiResponse.<AddressResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Restore Address By Id")
                .result(addressService.restore(id))
                .build();
    }
}
