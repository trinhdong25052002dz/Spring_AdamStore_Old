package Spring_AdamStore.service;

import Spring_AdamStore.dto.request.ColorRequest;
import Spring_AdamStore.dto.request.PromotionRequest;
import Spring_AdamStore.dto.response.ColorResponse;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.dto.response.PromotionResponse;

public interface ColorService {

    ColorResponse create(ColorRequest request);

    ColorResponse fetchById(Long id);

    PageResponse<ColorResponse> fetchAll(int pageNo, int pageSize, String sortBy);

    ColorResponse update(Long id, ColorRequest request);

    void delete(Long id);
}
