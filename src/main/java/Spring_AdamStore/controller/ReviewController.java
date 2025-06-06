package Spring_AdamStore.controller;

import Spring_AdamStore.dto.request.ReviewRequest;
import Spring_AdamStore.dto.request.ReviewUpdateRequest;
import Spring_AdamStore.dto.response.ApiResponse;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.dto.response.ReviewResponse;
import Spring_AdamStore.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "REVIEW-CONTROLLER")
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1")
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Product Review",
    description = "API này dùng để đánh giá sản phẩm")
    @PostMapping("/reviews")
    public ApiResponse<ReviewResponse> create(@Valid @RequestBody ReviewRequest request){
        return ApiResponse.<ReviewResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create Review")
                .result(reviewService.create(request))
                .build();
    }


    @GetMapping("/reviews/{id}")
    public ApiResponse<ReviewResponse> fetchById(@Min(value = 1, message = "ID phải lớn hơn 0")
                                                    @PathVariable Long id){
        return ApiResponse.<ReviewResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch Review By Id")
                .result(reviewService.fetchById(id))
                .build();
    }

    @GetMapping("/reviews")
    public ApiResponse<PageResponse<ReviewResponse>> fetchAll(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                                 @RequestParam(defaultValue = "1") int pageNo,
                                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                                 @RequestParam(required = false) String sortBy){
        return ApiResponse.<PageResponse<ReviewResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(reviewService.fetchAll(pageNo, pageSize, sortBy))
                .message("Fetch All Reviews With Pagination")
                .build();
    }


    @PutMapping("/reviews/{id}")
    public ApiResponse<ReviewResponse> update(@Min(value = 1, message = "ID phải lớn hơn 0")
                                                 @PathVariable Long id, @Valid @RequestBody ReviewUpdateRequest request){
        return ApiResponse.<ReviewResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update Review By Id")
                .result(reviewService.update(id, request))
                .build();
    }


    @DeleteMapping("/reviews/{id}")
    public ApiResponse<Void> delete(@Min(value = 1, message = "ID phải lớn hơn 0")
                                    @PathVariable Long id){
        reviewService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Delete Review By Id")
                .result(null)
                .build();
    }
}
