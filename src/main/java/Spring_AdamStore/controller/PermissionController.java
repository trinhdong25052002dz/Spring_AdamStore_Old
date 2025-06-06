package Spring_AdamStore.controller;

import Spring_AdamStore.dto.response.ApiResponse;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.dto.response.PermissionResponse;
import Spring_AdamStore.service.PermissionService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1")
@RestController
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping("/permissions/{id}")
    public ApiResponse<PermissionResponse> fetchRoleById(@Min(value = 1, message = "ID phải lớn hơn 0")
                                                             @PathVariable long id){
        return ApiResponse.<PermissionResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch Permission By Id")
                .result(permissionService.fetchPermissionById(id))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/permissions")
    public ApiResponse<PageResponse<PermissionResponse>> fetchAll(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                                      @RequestParam(defaultValue = "1") int pageNo,
                                                                  @RequestParam(defaultValue = "10") int pageSize,
                                                                      @RequestParam(required = false) String sortBy){
        return ApiResponse.<PageResponse<PermissionResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(permissionService.fetchAllPermissions(pageNo, pageSize, sortBy))
                .message("Fetch All Permissions With Pagination")
                .build();
    }

}
