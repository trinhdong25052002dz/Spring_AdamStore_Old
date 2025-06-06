package Spring_AdamStore.controller;

import Spring_AdamStore.dto.response.*;
import Spring_AdamStore.service.DistrictService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "DISTRICT-CONTROLLER")
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1")
@RestController
public class DistrictController {

    private final DistrictService districtService;

    @GetMapping("/districts/{id}")
    public ApiResponse<DistrictResponse> fetchById(@Min(value = 1, message = "ID phải lớn hơn 0")
                                                   @PathVariable Long id){
        return ApiResponse.<DistrictResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch District By Id")
                .result(districtService.fetchById(id))
                .build();
    }

    @GetMapping("/districts")
    public ApiResponse<PageResponse<DistrictResponse>> fetchAll(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                                @RequestParam(defaultValue = "1") int pageNo,
                                                                @RequestParam(defaultValue = "10") int pageSize,
                                                                @RequestParam(required = false) String sortBy){
        return ApiResponse.<PageResponse<DistrictResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(districtService.fetchAll(pageNo, pageSize, sortBy))
                .message("Fetch All Districts With Pagination")
                .build();
    }

    @Operation(description = "API dùng để lấy danh sách wards theo district")
    @GetMapping("/districts/{districtId}/wards")
    public ApiResponse<PageResponse<WardResponse>> fetchWardsByDistrict(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                                            @RequestParam(defaultValue = "1") int pageNo,
                                                                        @RequestParam(defaultValue = "10") int pageSize,
                                                                        @RequestParam(required = false) String sortBy,
                                                                        @Min(value = 1, message = "provinceId phải lớn hơn 0")
                                                                            @PathVariable Integer districtId) {
        return ApiResponse.<PageResponse<WardResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch All Wards for District")
                .result(districtService.fetchWardsByDistrictId(pageNo, pageSize, sortBy, districtId))
                .build();
    }

}
