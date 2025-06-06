package Spring_AdamStore.controller;

import Spring_AdamStore.dto.request.CategoryRequest;
import Spring_AdamStore.dto.request.ProductRequest;
import Spring_AdamStore.dto.response.*;
import Spring_AdamStore.service.CategoryService;
import Spring_AdamStore.service.ProductService;
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

@Slf4j(topic = "CATEGORY-CONTROLLER")
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1")
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/categories")
    public ApiResponse<CategoryResponse> create(@Valid @RequestBody CategoryRequest request){
        return ApiResponse.<CategoryResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create Category")
                .result(categoryService.create(request))
                .build();
    }


    @GetMapping("/categories/{id}")
    public ApiResponse<CategoryResponse> fetchById(@Min(value = 1, message = "ID phải lớn hơn 0")
                                                  @PathVariable Long id){
        return ApiResponse.<CategoryResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch Category By Id")
                .result(categoryService.fetchById(id))
                .build();
    }

    @Operation(summary = "Fetch All Categories For User",
    description = "Api này để lấy tất cả Categories (ACTIVE) cho User")
    @GetMapping("/categories")
    public ApiResponse<PageResponse<CategoryResponse>> fetchAll(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                               @RequestParam(defaultValue = "1") int pageNo,
                                                               @RequestParam(defaultValue = "10") int pageSize,
                                                               @RequestParam(required = false) String sortBy){
        return ApiResponse.<PageResponse<CategoryResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(categoryService.fetchAll(pageNo, pageSize, sortBy))
                .message("Fetch All Categories For User")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Fetch All Categories For Admin",
            description = "Api này để lấy tất cả Categories (cả ACTIVE và INACTIVE) cho Admin")
    @GetMapping("/categories/admin")
    public ApiResponse<PageResponse<CategoryResponse>> fetchAllCategoriesForAdmin(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                                              @RequestParam(defaultValue = "1") int pageNo,
                                                                              @RequestParam(defaultValue = "10") int pageSize,
                                                                              @RequestParam(required = false) String sortBy){
        return ApiResponse.<PageResponse<CategoryResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(categoryService.fetchAllCategoriesForAdmin(pageNo, pageSize, sortBy))
                .message("Fetch All Categories For Admin")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/categories/{id}")
    public ApiResponse<CategoryResponse> update(@Min(value = 1, message = "ID phải lớn hơn 0")
                                               @PathVariable Long id, @Valid @RequestBody CategoryRequest request){
        return ApiResponse.<CategoryResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update Category By Id")
                .result(categoryService.update(id, request))
                .build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Soft Delete Category")
    @DeleteMapping("/categories/{id}")
    public ApiResponse<Void> delete(@Min(value = 1, message = "ID phải lớn hơn 0")
                                    @PathVariable Long id){
        categoryService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Soft Delete Category By Id")
                .result(null)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Restore Category")
    @PatchMapping("/categories/{id}/restore")
    public ApiResponse<CategoryResponse> restore(@Min(value = 1, message = "Id phải lớn hơn 0")
                                               @PathVariable long id) {
        return ApiResponse.<CategoryResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Restore Category By Id")
                .result(categoryService.restore(id))
                .build();
    }

    @Operation(summary = "Fetch All Products By Category For User",
            description = "API dùng để lấy danh sách products của category cho user")
    @GetMapping("/categories/{categoryId}/products")
    public ApiResponse<PageResponse<ProductResponse>> fetchByCategoryId(@RequestParam(defaultValue = "1") int pageNo,
                                                                         @RequestParam(defaultValue = "10") int pageSize,
                                                                         @RequestParam(required = false) String sortBy,
                                                                         @Min(value = 1, message = "categoryId phải lớn hơn 0")
                                                                         @PathVariable Long categoryId){
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch All Products By Category For User")
                .result(categoryService.fetchByCategoryId(pageNo, pageSize, sortBy, categoryId))
                .build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Fetch All Products By Category For Admin",
            description = "API dùng để lấy danh sách products của category cho admin")
    @GetMapping("/categories/{categoryId}/products/admin")
    public ApiResponse<PageResponse<ProductResponse>> fetchByCategoryIdForAdmin(@RequestParam(defaultValue = "1") int pageNo,
                                                                        @RequestParam(defaultValue = "10") int pageSize,
                                                                        @RequestParam(required = false) String sortBy,
                                                                        @Min(value = 1, message = "categoryId phải lớn hơn 0")
                                                                        @PathVariable Long categoryId){
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch All Products By Category For Admin")
                .result(categoryService.fetchByCategoryIdForAdmin(pageNo, pageSize, sortBy, categoryId))
                .build();
    }

}
