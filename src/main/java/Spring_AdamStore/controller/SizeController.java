package Spring_AdamStore.controller;

import Spring_AdamStore.dto.request.ColorRequest;
import Spring_AdamStore.dto.request.SizeRequest;
import Spring_AdamStore.dto.response.ApiResponse;
import Spring_AdamStore.dto.response.ColorResponse;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.dto.response.SizeResponse;
import Spring_AdamStore.service.ColorService;
import Spring_AdamStore.service.SizeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "SIZE-CONTROLLER")
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1")
@RestController
public class SizeController {

    private final SizeService sizeService;

    @GetMapping("/sizes/{id}")
    public ApiResponse<SizeResponse> fetchById(@Min(value = 1, message = "ID phải lớn hơn 0")
                                                @PathVariable Long id){
        return ApiResponse.<SizeResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch Size By Id")
                .result(sizeService.fetchById(id))
                .build();
    }

    @GetMapping("/sizes")
    public ApiResponse<PageResponse<SizeResponse>> fetchAll(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                             @RequestParam(defaultValue = "1") int pageNo,
                                                             @RequestParam(defaultValue = "10") int pageSize,
                                                             @RequestParam(required = false) String sortBy){
        return ApiResponse.<PageResponse<SizeResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(sizeService.fetchAll(pageNo, pageSize, sortBy))
                .message("Fetch All Sizes With Pagination")
                .build();
    }

}
