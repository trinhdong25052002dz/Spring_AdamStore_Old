package Spring_AdamStore.service.impl;

import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.dto.response.RoleResponse;
import Spring_AdamStore.entity.Role;
import Spring_AdamStore.exception.AppException;
import Spring_AdamStore.exception.ErrorCode;
import Spring_AdamStore.mapper.RoleMapper;
import Spring_AdamStore.repository.RoleRepository;
import Spring_AdamStore.service.PageableService;
import Spring_AdamStore.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "ROLE-SERVICE")
@RequiredArgsConstructor
public class RoleServiceimpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PageableService pageableService;

    @Override
    public RoleResponse fetchRoleById(long id) {
        Role roleDB = findRoleById(id);

        return roleMapper.toRoleResponse(roleDB);
    }

    @Override
    public PageResponse<RoleResponse> fetchAllRoles(int pageNo, int pageSize, String sortBy) {
        pageNo = pageNo - 1;

        Pageable pageable = pageableService.createPageable(pageNo, pageSize, sortBy, Role.class);

        Page<Role> rolePage = roleRepository.findAll(pageable);

        return PageResponse.<RoleResponse>builder()
                .page(rolePage.getNumber() + 1)
                .size(rolePage.getSize())
                .totalPages(rolePage.getTotalPages())
                .totalItems(rolePage.getTotalElements())
                .items(roleMapper.toRoleResponseList(rolePage.getContent()))
                .build();
    }

    private Role findRoleById(long id) {
        return roleRepository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
    }
}
