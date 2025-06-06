package Spring_AdamStore.controller;

import Spring_AdamStore.dto.request.ProductRequest;
import Spring_AdamStore.dto.request.PromotionRequest;
import Spring_AdamStore.dto.request.PromotionUpdateRequest;
import Spring_AdamStore.dto.response.*;
import Spring_AdamStore.service.ProductService;
import Spring_AdamStore.service.PromotionService;
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

@Slf4j(topic = "PROMOTION-CONTROLLER")
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1")
@RestController
public class PromotionController {

    private final PromotionService promotionService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/promotions")
    public ApiResponse<PromotionResponse> create(@Valid @RequestBody PromotionRequest request){
        return ApiResponse.<PromotionResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create Promotion")
                .result(promotionService.create(request))
                .build();
    }


    @GetMapping("/promotions/{id}")
    public ApiResponse<PromotionResponse> fetchById(@Min(value = 1, message = "ID phải lớn hơn 0")
                                                  @PathVariable Long id){
        return ApiResponse.<PromotionResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch Promotion By Id")
                .result(promotionService.fetchById(id))
                .build();
    }


    @Operation(summary = "Fetch All Promotions For Admin")
    @GetMapping("/promotions")
    public ApiResponse<PageResponse<PromotionResponse>> fetchAll(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                               @RequestParam(defaultValue = "1") int pageNo,
                                                               @RequestParam(defaultValue = "10") int pageSize,
                                                               @RequestParam(required = false) String sortBy){
        return ApiResponse.<PageResponse<PromotionResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(promotionService.fetchAll(pageNo, pageSize, sortBy))
                .message("Fetch All Promotions For Admin")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/promotions/{id}")
    public ApiResponse<PromotionResponse> update(@Min(value = 1, message = "ID phải lớn hơn 0")
                                               @PathVariable Long id, @Valid @RequestBody PromotionUpdateRequest request){
        return ApiResponse.<PromotionResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update Promotion By Id")
                .result(promotionService.update(id, request))
                .build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Soft delete Promotion")
    @DeleteMapping("/promotions/{id}")
    public ApiResponse<Void> delete(@Min(value = 1, message = "ID phải lớn hơn 0")
                                    @PathVariable Long id){
        promotionService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Soft Delete Promotion By Id")
                .result(null)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Restore Promotion",
    description = "Api này để khôi phục Promotion")
    @PatchMapping("/promotions/{id}/restore")
    public ApiResponse<PromotionResponse> restore(@Min(value = 1, message = "Id phải lớn hơn 0")
                                                 @PathVariable long id) {
        return ApiResponse.<PromotionResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Restore Promotion By Id")
                .result(promotionService.restore(id))
                .build();
    }
}
