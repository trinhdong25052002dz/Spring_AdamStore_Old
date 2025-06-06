package Spring_AdamStore.service;

import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.dto.response.RoleResponse;

public interface RoleService {

    RoleResponse fetchRoleById(long id);

    PageResponse<RoleResponse> fetchAllRoles(int pageNo, int pageSize, String sortBy);
}
