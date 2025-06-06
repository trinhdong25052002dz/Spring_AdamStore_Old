package Spring_AdamStore.service;

import Spring_AdamStore.dto.request.ProductRequest;
import Spring_AdamStore.dto.request.ProductUpdateRequest;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.dto.response.ProductResponse;
import Spring_AdamStore.dto.response.ProductVariantResponse;
import Spring_AdamStore.dto.response.ReviewResponse;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest request);

    ProductResponse fetchById(Long id);

    PageResponse<ProductResponse> fetchAll(int pageNo, int pageSize, String sortBy);

    ProductResponse update(Long id, ProductUpdateRequest request);

    void delete(Long id);

    PageResponse<ReviewResponse> fetchReviewsByProductId(int pageNo, int pageSize, String sortBy,Long productId);

    PageResponse<ProductResponse> searchProduct(int pageNo, int pageSize, String sortBy, List<String> search);

    PageResponse<ProductVariantResponse> getVariantsByProductId(int pageNo, int pageSize, String sortBy, Long productId);

    PageResponse<ProductResponse> fetchAllProductsForAdmin(int pageNo, int pageSize, String sortBy);

    ProductResponse restore(long id);

    PageResponse<ProductVariantResponse> getVariantsByProductIdForAdmin(int pageNo, int pageSize, String sortBy, Long productId);
}
