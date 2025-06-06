package Spring_AdamStore.mapper;

import Spring_AdamStore.dto.basic.EntityBasic;
import Spring_AdamStore.dto.basic.ProductVariantBasic;
import Spring_AdamStore.dto.response.ProductVariantResponse;
import Spring_AdamStore.entity.ProductVariant;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ProductVariantMapper {

    ProductVariantResponse toProductVariantResponse(ProductVariant productVariant);

    List<ProductVariantResponse> toProductVariantResponseList(List<ProductVariant> productVariantList);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "product", source = "product")
    @Mapping(target = "color", source = "color")
    @Mapping(target = "size", source = "size")
    ProductVariantBasic toProductVariantBasic(ProductVariant productVariant);

    @Named("getPriceFromVariant")
    default Double getPriceFromFirstVariant(Set<ProductVariant> variants) {
        return variants.stream()
                .findFirst()
                .map(ProductVariant::getPrice)
                .orElse(0.0);
    }

    @Named("getQuantityFromVariant")
    default Integer getQuantityFromFirstVariant(Set<ProductVariant> variants) {
        return variants.stream()
                .findFirst()
                .map(ProductVariant::getQuantity)
                .orElse(0);
    }


    @Named("toSizeSet")
    default Set<EntityBasic> toSizeSet(Set<ProductVariant> productVariants) {
        return new HashSet<>(productVariants.stream()
                .map(pv -> new EntityBasic(pv.getSize().getId(), pv.getSize().getName()))
                .collect(Collectors.toMap(
                        EntityBasic::getId,
                        size -> size,
                        (existing, replacement) -> existing))
                .values());
    }

    @Named("toColorSet")
    default Set<EntityBasic> toColorSet(Set<ProductVariant> productVariants) {
        return new HashSet<>(productVariants.stream()
                .map(pv -> new EntityBasic(pv.getColor().getId(), pv.getColor().getName()))
                .collect(Collectors.toMap(
                        EntityBasic::getId,
                        color -> color,
                        (existing, replacement) -> existing))
                .values());
    }
}
