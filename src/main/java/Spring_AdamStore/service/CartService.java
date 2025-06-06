package Spring_AdamStore.service;

import Spring_AdamStore.dto.response.CartItemResponse;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.entity.User;

public interface CartService {

    void createCartForUser(User user);

    PageResponse<CartItemResponse> getCartItemsOfCurrentUser(int pageNo, int pageSize, String sortBy);
}
