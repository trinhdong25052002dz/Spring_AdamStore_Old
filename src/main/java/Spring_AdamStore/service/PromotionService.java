package Spring_AdamStore.service;

import Spring_AdamStore.dto.request.PromotionRequest;
import Spring_AdamStore.dto.request.PromotionUpdateRequest;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.dto.response.PromotionResponse;
import jakarta.validation.constraints.Min;

public interface PromotionService {

    PromotionResponse create(PromotionRequest request);

    PromotionResponse fetchById(Long id);

    PageResponse<PromotionResponse> fetchAll(int pageNo, int pageSize, String sortBy);

    PromotionResponse update(Long id, PromotionUpdateRequest request);

    void delete(Long id);

    PromotionResponse restore(long id);
}
