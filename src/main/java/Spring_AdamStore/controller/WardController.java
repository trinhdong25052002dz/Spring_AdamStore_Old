package Spring_AdamStore.controller;

import Spring_AdamStore.dto.response.ApiResponse;
import Spring_AdamStore.dto.response.DistrictResponse;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.dto.response.WardResponse;
import Spring_AdamStore.service.DistrictService;
import Spring_AdamStore.service.WardService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "WARD-CONTROLLER")
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1")
@RestController
public class WardController {

    private final WardService wardService;

    @GetMapping("/wards/{code}")
    public ApiResponse<WardResponse> fetchById(@PathVariable String code){
        return ApiResponse.<WardResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch Ward By Id")
                .result(wardService.fetchById(code))
                .build();
    }

    @GetMapping("/wards")
    public ApiResponse<PageResponse<WardResponse>> fetchAll(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                                @RequestParam(defaultValue = "1") int pageNo,
                                                                @RequestParam(defaultValue = "10") int pageSize,
                                                                @RequestParam(required = false) String sortBy){
        return ApiResponse.<PageResponse<WardResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(wardService.fetchAll(pageNo, pageSize, sortBy))
                .message("Fetch All Wards With Pagination")
                .build();
    }


}
