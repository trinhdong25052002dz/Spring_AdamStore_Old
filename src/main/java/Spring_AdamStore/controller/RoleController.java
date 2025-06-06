package Spring_AdamStore.controller;

import Spring_AdamStore.dto.response.ApiResponse;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.dto.response.RoleResponse;
import Spring_AdamStore.service.RoleService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "ROLE-CONTROLLER")
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1")
@RestController
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/roles/{id}")
    public ApiResponse<RoleResponse> fetchRoleById(@Min(value = 1, message = "ID phải lớn hơn 0")
                                                   @PathVariable long id){
        return ApiResponse.<RoleResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch Role By Id")
                .result(roleService.fetchRoleById(id))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/roles")
    public ApiResponse<PageResponse<RoleResponse>> fetchAll(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                            @RequestParam(defaultValue = "1") int pageNo,
                                                            @RequestParam(defaultValue = "10") int pageSize,
                                                            @RequestParam(required = false) String sortBy){
        return ApiResponse.<PageResponse<RoleResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(roleService.fetchAllRoles(pageNo, pageSize, sortBy))
                .message("Fetch All Roles With Pagination")
                .build();
    }
}
