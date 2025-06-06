package Spring_AdamStore.mapper;

import Spring_AdamStore.dto.request.CartItemRequest;
import Spring_AdamStore.dto.request.ColorRequest;
import Spring_AdamStore.dto.response.CartItemResponse;
import Spring_AdamStore.dto.response.ColorResponse;
import Spring_AdamStore.entity.Cart;
import Spring_AdamStore.entity.CartItem;
import Spring_AdamStore.entity.Color;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CartItemMapper {


    @Mapping(target = "productVariantBasic", source = "productVariant")
    CartItemResponse toCartItemResponse(CartItem cartItem);

    List<CartItemResponse> toCartItemResponseList(List<CartItem> cartItemList);

}
