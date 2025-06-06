package Spring_AdamStore.mapper;

import Spring_AdamStore.dto.basic.EntityBasic;
import Spring_AdamStore.dto.response.PermissionResponse;
import Spring_AdamStore.entity.Permission;
import Spring_AdamStore.entity.relationship.RoleHasPermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PermissionMapper {

    PermissionResponse toPermissionResponse(Permission permission);

    List<PermissionResponse> toPermissionResponseList(List<Permission> permissions);

    @Mapping(target = "id", source = "roleHasPermission.permission.id")
    @Mapping(target = "name", source = "roleHasPermission.permission.name")
    @Named("roleHasPermissionToEntityBasic")
    EntityBasic roleHasPermissionToEntityBasic(RoleHasPermission roleHasPermission);

}
