package Spring_AdamStore.service;

import Spring_AdamStore.dto.request.PromotionRequest;
import Spring_AdamStore.dto.request.ReviewRequest;
import Spring_AdamStore.dto.request.ReviewUpdateRequest;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.dto.response.PromotionResponse;
import Spring_AdamStore.dto.response.ReviewResponse;

public interface ReviewService {

    ReviewResponse create(ReviewRequest request);

    ReviewResponse fetchById(Long id);

    PageResponse<ReviewResponse> fetchAll(int pageNo, int pageSize, String sortBy);

    ReviewResponse update(Long id, ReviewUpdateRequest request);

    void delete(Long id);
}
