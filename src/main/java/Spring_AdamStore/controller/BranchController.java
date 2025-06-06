package Spring_AdamStore.controller;

import Spring_AdamStore.dto.request.BranchRequest;
import Spring_AdamStore.dto.request.BranchUpdateRequest;
import Spring_AdamStore.dto.response.ApiResponse;
import Spring_AdamStore.dto.response.BranchResponse;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.service.BranchService;
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

@Slf4j(topic = "BRANCH-CONTROLLER")
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1")
@RestController
public class BranchController {

    private final BranchService branchService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/branches")
    public ApiResponse<BranchResponse> create(@Valid @RequestBody BranchRequest request){
        return ApiResponse.<BranchResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create Branch")
                .result(branchService.create(request))
                .build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/branches/{id}")
    public ApiResponse<BranchResponse> fetchById(@Min(value = 1, message = "ID phải lớn hơn 0")
                                                   @PathVariable Long id){
        return ApiResponse.<BranchResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch Branch By Id")
                .result(branchService.fetchById(id))
                .build();
    }


    @Operation(summary = "Fetch All Branches For User",
    description = "API để lấy tất cả Branch (ACTIVE) cho user")
    @GetMapping("/branches")
    public ApiResponse<PageResponse<BranchResponse>> fetchAll(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                                @RequestParam(defaultValue = "1") int pageNo,
                                                                @RequestParam(defaultValue = "10") int pageSize,
                                                                @RequestParam(required = false) String sortBy){
        return ApiResponse.<PageResponse<BranchResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(branchService.fetchAll(pageNo, pageSize, sortBy))
                .message("Fetch All Branches For User")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Fetch All Branches For Admin",
    description = "API này để lấy tất cả Branch (cả ACTIVE và INACTIVE) cho admin quản lý")
    @GetMapping("/branches/admin")
    public ApiResponse<PageResponse<BranchResponse>> fetchAllBranchesForAdmin(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                                                  @RequestParam(defaultValue = "1") int pageNo,
                                                                              @RequestParam(defaultValue = "10") int pageSize,
                                                                              @RequestParam(required = false) String sortBy){
        return ApiResponse.<PageResponse<BranchResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(branchService.fetchAllBranchesForAdmin(pageNo, pageSize, sortBy))
                .message("Fetch All Branches For Admin")
                .build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/branches/{id}")
    public ApiResponse<BranchResponse> update(@Min(value = 1, message = "ID phải lớn hơn 0")
                                                @PathVariable Long id, @Valid @RequestBody BranchUpdateRequest request){
        return ApiResponse.<BranchResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update Branch By Id")
                .result(branchService.update(id, request))
                .build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Soft Delete Branch",
    description = "Api này để soft delete branch")
    @DeleteMapping("/branches/{id}")
    public ApiResponse<Void> delete(@Min(value = 1, message = "ID phải lớn hơn 0")
                                    @PathVariable Long id){
        branchService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Soft Delete Branch By Id")
                .result(null)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Restore Branch",
    description = "Api này để khôi phục Branch")
    @PatchMapping("/branches/{id}/restore")
    public ApiResponse<BranchResponse> restore(@Min(value = 1, message = "Id phải lớn hơn 0")
                                             @PathVariable long id) {
        return ApiResponse.<BranchResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Restore Branch By Id")
                .result(branchService.restore(id))
                .build();
    }
}
