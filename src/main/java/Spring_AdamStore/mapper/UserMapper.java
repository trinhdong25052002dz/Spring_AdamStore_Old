package Spring_AdamStore.mapper;

import Spring_AdamStore.dto.basic.UserBasic;
import Spring_AdamStore.dto.request.RegisterRequest;
import Spring_AdamStore.dto.request.UserCreationRequest;
import Spring_AdamStore.dto.request.UserUpdateRequest;
import Spring_AdamStore.dto.response.UserResponse;
import Spring_AdamStore.entity.RedisPendingUser;
import Spring_AdamStore.entity.User;
import org.mapstruct.*;

import javax.naming.Name;
import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = {RoleMapper.class})
public interface UserMapper {

    User userCreationToUser(UserCreationRequest request);

    RedisPendingUser registerToRedis(RegisterRequest request);

    User redisToUser(RedisPendingUser redisPendingUser);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "userHasRoleToEntityBasic")
    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> users);

    UserBasic toUserBasic(User user);
}
