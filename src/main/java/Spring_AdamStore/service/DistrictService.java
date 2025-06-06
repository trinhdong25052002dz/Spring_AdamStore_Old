package Spring_AdamStore.service;

import Spring_AdamStore.dto.response.DistrictResponse;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.dto.response.ProvinceResponse;
import Spring_AdamStore.dto.response.WardResponse;
import Spring_AdamStore.entity.District;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface DistrictService {

    DistrictResponse fetchById(Long id);

    PageResponse<DistrictResponse> fetchAll(int pageNo, int pageSize, String sortBy);

    List<District> loadDistrictsFromGhn(int provinceId);

    PageResponse<WardResponse> fetchWardsByDistrictId(int pageNo, int pageSize, String sortBy, Integer districtId);
}
