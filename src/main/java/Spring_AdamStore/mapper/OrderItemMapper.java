package Spring_AdamStore.mapper;

import Spring_AdamStore.dto.response.CartItemResponse;
import Spring_AdamStore.dto.response.OrderItemResponse;
import Spring_AdamStore.entity.CartItem;
import Spring_AdamStore.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface OrderItemMapper {

    @Mapping(target = "productVariant", source = "productVariant")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    List<OrderItemResponse> toOrderItemResponseList(List<OrderItem> orderItemList);

}
