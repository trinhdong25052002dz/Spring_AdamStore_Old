package Spring_AdamStore.service.impl;

import Spring_AdamStore.dto.request.CartItemRequest;
import Spring_AdamStore.dto.request.CartItemUpdateRequest;
import Spring_AdamStore.dto.response.CartItemResponse;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.entity.*;
import Spring_AdamStore.exception.AppException;
import Spring_AdamStore.exception.ErrorCode;
import Spring_AdamStore.mapper.CartItemMapper;
import Spring_AdamStore.repository.CartItemRepository;
import Spring_AdamStore.repository.CartRepository;
import Spring_AdamStore.repository.ProductVariantRepository;
import Spring_AdamStore.service.CartItemService;
import Spring_AdamStore.service.CurrentUserService;
import Spring_AdamStore.service.PageableService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j(topic = "CART-ITEM-SERVICE")
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CartRepository cartRepository;
    private final PageableService pageableService;
    private final CartItemMapper cartItemMapper;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public CartItemResponse create(CartItemRequest request) {
        User user = currentUserService.getCurrentUser();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(()->new AppException(ErrorCode.CART_NOT_EXISTED));

        ProductVariant productVariant = productVariantRepository.findById(request.getProductVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_EXISTED));

        // check sl hang con
        if (productVariant.getQuantity() < request.getQuantity()) {
            throw new AppException(ErrorCode.OUT_OF_STOCK);
        }

        Optional<CartItem> existingCartItem = cartItemRepository
                .findByCartIdAndProductVariantId(cart.getId(), request.getProductVariantId());

        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else{
            cartItem = CartItem.builder()
                    .quantity(request.getQuantity())
                    .price(productVariant.getPrice())
                    .productVariant(productVariant)
                    .cart(cart)
                    .build();
        }
        return cartItemMapper.toCartItemResponse(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemResponse fetchById(Long id) {
        CartItem cartItem = findCartItemById(id);
        return cartItemMapper.toCartItemResponse(cartItem);
    }

    @Override
    public PageResponse<CartItemResponse> fetchAll(int pageNo, int pageSize, String sortBy) {
        pageNo = pageNo - 1;

        Pageable pageable = pageableService.createPageable(pageNo, pageSize, sortBy, CartItem.class);

        Page<CartItem> cartItemPage = cartItemRepository.findAll(pageable);

        return PageResponse.<CartItemResponse>builder()
                .page(cartItemPage.getNumber() + 1)
                .size(cartItemPage.getSize())
                .totalPages(cartItemPage.getTotalPages())
                .totalItems(cartItemPage.getTotalElements())
                .items(cartItemMapper.toCartItemResponseList(cartItemPage.getContent()))
                .build();
    }

    @Override
    @Transactional
    public CartItemResponse update(Long id, CartItemUpdateRequest request) {
        CartItem cartItem = findCartItemById(id);

        cartItem.setQuantity(request.getQuantity());
        return cartItemMapper.toCartItemResponse(cartItemRepository.save(cartItem));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        CartItem cartItem = findCartItemById(id);

        cartItemRepository.delete(cartItem);
    }

    private CartItem findCartItemById(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_EXISTED));
    }

}
