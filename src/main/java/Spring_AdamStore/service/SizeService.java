package Spring_AdamStore.service;

import Spring_AdamStore.dto.request.ColorRequest;
import Spring_AdamStore.dto.request.SizeRequest;
import Spring_AdamStore.dto.response.ColorResponse;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.dto.response.SizeResponse;

public interface SizeService {

    SizeResponse fetchById(Long id);

    PageResponse<SizeResponse> fetchAll(int pageNo, int pageSize, String sortBy);
}
