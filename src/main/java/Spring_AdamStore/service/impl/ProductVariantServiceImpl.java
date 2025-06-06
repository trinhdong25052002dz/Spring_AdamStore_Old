package Spring_AdamStore.service.impl;

import Spring_AdamStore.dto.request.ProductVariantUpdateRequest;
import Spring_AdamStore.dto.response.ProductVariantResponse;
import Spring_AdamStore.entity.Color;
import Spring_AdamStore.entity.Product;
import Spring_AdamStore.entity.ProductVariant;
import Spring_AdamStore.entity.Size;
import Spring_AdamStore.exception.AppException;
import Spring_AdamStore.exception.ErrorCode;
import Spring_AdamStore.mapper.ProductVariantMapper;
import Spring_AdamStore.repository.CartItemRepository;
import Spring_AdamStore.repository.OrderItemRepository;
import Spring_AdamStore.repository.ProductVariantRepository;
import Spring_AdamStore.service.ProductVariantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static Spring_AdamStore.constants.EntityStatus.ACTIVE;

@Service
@Slf4j(topic = "PRODUCT-VARIANT-SERVICE")
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductVariantMapper productVariantMapper;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;


    @Override
    public ProductVariantResponse findByProductAndColorAndSize(Long productId, Long colorId, Long sizeId){
        ProductVariant variant = productVariantRepository
                .findByProductIdAndColorIdAndSizeId(productId, colorId, sizeId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_EXISTED));

        return productVariantMapper.toProductVariantResponse(variant);
    }


    @Override
    @Transactional
    public Set<ProductVariant> saveProductVariant(Product product, Set<Size> sizeSet, Set<Color> colorSet, Double price, Integer quantity) {
        Set<ProductVariant> productVariantSet = new HashSet<>();

        for (Color color : colorSet) {
            for (Size size : sizeSet) {
                ProductVariant variant = ProductVariant.builder()
                        .price(price)
                        .quantity(quantity)
                        .isAvailable(true)
                        .product(product)
                        .size(size)
                        .color(color)
                        .build();

                productVariantSet.add(variant);
            }
        }
        return new HashSet<>(productVariantRepository.saveAll(productVariantSet));
    }

    @Override
    public Set<ProductVariant> updateProductVariants(Product product, Set<Size> sizeSet, Set<Color> colorSet, Double price, Integer quantity) {
        List<ProductVariant> oldVariants = productVariantRepository.findAllByProductId(product.getId());
        productVariantRepository.deleteAll(oldVariants);
        Set<ProductVariant> productVariantSet = new HashSet<>();

        for (Color color : colorSet) {
            for (Size size : sizeSet) {
                ProductVariant variant = ProductVariant.builder()
                        .price(price)
                        .quantity(quantity)
                        .product(product)
                        .size(size)
                        .color(color)
                        .build();

                productVariantSet.add(variant);
            }
        }
        return new HashSet<>(productVariantRepository.saveAll(productVariantSet));
    }

    public Set<ProductVariant> updatePriceAndQuantity(Product product, Double price, Integer quantity) {
        List<ProductVariant> oldVariants = productVariantRepository.findAllByProductId(product.getId());

        for (ProductVariant variant : oldVariants) {
            variant.setPrice(price);
            variant.setQuantity(quantity);
        }

        return new HashSet<>(productVariantRepository.saveAll(oldVariants));
    }

    @Override
    @Transactional
    public ProductVariantResponse updatePriceAndQuantity(Long id, ProductVariantUpdateRequest request) {
        ProductVariant productVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_EXISTED));

        productVariant.setPrice(request.getPrice());
        productVariant.setQuantity(request.getQuantity());

        return productVariantMapper.toProductVariantResponse(productVariantRepository.save(productVariant));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ProductVariant productVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_EXISTED));

        if(orderItemRepository.existsByProductVariantId(productVariant.getId())){
            throw new AppException(ErrorCode.PRODUCT_VARIANT_USED_IN_ORDER);
        }

        cartItemRepository.deleteAll(productVariant.getCartItems());

        productVariantRepository.delete(productVariant);
    }

    @Override
    public ProductVariantResponse restore(long id) {
        ProductVariant productVariant = productVariantRepository.findProductVariantById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_EXISTED));

        productVariant.setStatus(ACTIVE);
        return productVariantMapper.toProductVariantResponse(productVariantRepository.save(productVariant));
    }
}
