package Spring_AdamStore.service;

import Spring_AdamStore.dto.response.DistrictResponse;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.dto.response.ProvinceResponse;
import Spring_AdamStore.entity.Province;

import java.util.List;

public interface ProvinceService {

    ProvinceResponse fetchById(Long id);

    PageResponse<ProvinceResponse> fetchAll(int pageNo, int pageSize, String sortBy);

    PageResponse<DistrictResponse> fetchDistrictsByProvinceId(int pageNo, int pageSize, String sortBy, Integer provinceId);

    List<Province> loadProvincesFromGhn();
}
