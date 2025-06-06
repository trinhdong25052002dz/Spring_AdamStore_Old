package Spring_AdamStore.service;

import Spring_AdamStore.dto.request.ProductVariantUpdateRequest;
import Spring_AdamStore.dto.response.ProductVariantResponse;
import Spring_AdamStore.entity.Color;
import Spring_AdamStore.entity.Product;
import Spring_AdamStore.entity.ProductVariant;
import Spring_AdamStore.entity.Size;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.TSFBuilder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import java.util.Set;

public interface ProductVariantService {

    ProductVariantResponse findByProductAndColorAndSize(Long productId, Long colorId, Long sizeId);

    Set<ProductVariant> saveProductVariant(Product product, Set<Size> sizeSet, Set<Color> colorSet, Double price, Integer quantity);

    Set<ProductVariant> updateProductVariants(Product product, Set<Size> sizeSet, Set<Color> colorSet, Double price, Integer quantity);

    Set<ProductVariant> updatePriceAndQuantity(Product product, Double price, Integer quantity);

    ProductVariantResponse updatePriceAndQuantity(Long id, ProductVariantUpdateRequest request);

    void delete(Long id);

    ProductVariantResponse restore(long id);
}
