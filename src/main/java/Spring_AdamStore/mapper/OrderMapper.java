
package Spring_AdamStore.mapper;

import Spring_AdamStore.dto.request.OrderRequest;
import Spring_AdamStore.dto.response.OrderResponse;
import Spring_AdamStore.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface OrderMapper {

    @Mapping(target = "address", source = "address")
    @Mapping(target = "orderItems", source = "orderItems")
    @Mapping(target = "customerName", source = "user.email")
    OrderResponse toOrderResponse(Order order);

    List<OrderResponse> toOrderResponseList(List<Order> orderList);

}
