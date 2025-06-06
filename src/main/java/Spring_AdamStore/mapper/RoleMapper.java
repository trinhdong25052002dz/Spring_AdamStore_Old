package Spring_AdamStore.mapper;

import Spring_AdamStore.dto.basic.EntityBasic;
import Spring_AdamStore.dto.response.RoleResponse;
import Spring_AdamStore.entity.Role;
import Spring_AdamStore.entity.relationship.UserHasRole;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;


@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = {PermissionMapper.class})
public interface RoleMapper {

    @Mapping(source = "permissions", target = "permissions", qualifiedByName = "roleHasPermissionToEntityBasic")
    RoleResponse toRoleResponse(Role role);

    List<RoleResponse> toRoleResponseList(List<Role> roles);


    @Mapping(target = "id", source = "role.id")
    @Mapping(target = "name", source = "role.name")
    @Named("userHasRoleToEntityBasic")
    EntityBasic userHasRoleToEntityBasic(UserHasRole userHasRole);

    @IterableMapping(qualifiedByName = "userHasRoleToEntityBasic")
    Set<EntityBasic> userHasRoleToEntityBasicSet(Set<UserHasRole> userHasRoleSet);
}
