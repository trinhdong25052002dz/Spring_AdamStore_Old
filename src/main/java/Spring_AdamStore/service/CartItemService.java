
package Spring_AdamStore.service;

import Spring_AdamStore.dto.request.CartItemRequest;
import Spring_AdamStore.dto.request.CartItemUpdateRequest;
import Spring_AdamStore.dto.request.ColorRequest;
import Spring_AdamStore.dto.response.CartItemResponse;
import Spring_AdamStore.dto.response.ColorResponse;
import Spring_AdamStore.dto.response.PageResponse;

public interface CartItemService {

    CartItemResponse create(CartItemRequest request);

    CartItemResponse fetchById(Long id);

    PageResponse<CartItemResponse> fetchAll(int pageNo, int pageSize, String sortBy);

    CartItemResponse update(Long id, CartItemUpdateRequest request);

    void delete(Long id);
}
