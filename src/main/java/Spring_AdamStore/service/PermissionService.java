package Spring_AdamStore.service;

import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.dto.response.PermissionResponse;

public interface PermissionService {

    PermissionResponse fetchPermissionById(Long id);

    PageResponse<PermissionResponse> fetchAllPermissions(int pageNo, int pageSize, String sortBy);
}
