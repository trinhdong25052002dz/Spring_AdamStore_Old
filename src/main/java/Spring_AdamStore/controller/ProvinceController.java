package Spring_AdamStore.controller;

import Spring_AdamStore.dto.response.*;
import Spring_AdamStore.service.ProvinceService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "PROVINCE-CONTROLLER")
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1")
@RestController
public class ProvinceController {

    private final ProvinceService provinceService;

    @GetMapping("/provinces/{id}")
    public ApiResponse<ProvinceResponse> fetchById(@Min(value = 1, message = "ID phải lớn hơn 0")
                                                    @PathVariable Long id){
        return ApiResponse.<ProvinceResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch Province By Id")
                .result(provinceService.fetchById(id))
                .build();
    }

    @GetMapping("/provinces")
    public ApiResponse<PageResponse<ProvinceResponse>> fetchAll(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                                 @RequestParam(defaultValue = "1") int pageNo,
                                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                                 @RequestParam(required = false) String sortBy){
        return ApiResponse.<PageResponse<ProvinceResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(provinceService.fetchAll(pageNo, pageSize, sortBy))
                .message("Fetch All Provinces With Pagination")
                .build();
    }

    @Operation(description = "API dùng để lấy danh sách districts theo province")
    @GetMapping("/provinces/{provinceId}/districts")
    public ApiResponse<PageResponse<DistrictResponse>> fetchByProvinceId(@RequestParam(defaultValue = "1") int pageNo,
                                                                         @RequestParam(defaultValue = "10") int pageSize,
                                                                         @RequestParam(required = false) String sortBy,
                                                                         @Min(value = 1, message = "provinceId phải lớn hơn 0")
                                                                         @PathVariable Integer provinceId){
        return ApiResponse.<PageResponse<DistrictResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch Districts By Province ID With Pagination")
                .result(provinceService.fetchDistrictsByProvinceId(pageNo, pageSize, sortBy, provinceId))
                .build();
    }

}
