package Spring_AdamStore.service.impl;

import Spring_AdamStore.dto.response.*;
import Spring_AdamStore.entity.District;
import Spring_AdamStore.entity.Province;
import Spring_AdamStore.exception.AppException;
import Spring_AdamStore.exception.ErrorCode;
import Spring_AdamStore.mapper.DistrictMapper;
import Spring_AdamStore.mapper.ProvinceMapper;
import Spring_AdamStore.repository.DistrictRepository;
import Spring_AdamStore.repository.ProvinceRepository;
import Spring_AdamStore.service.PageableService;
import Spring_AdamStore.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j(topic = "PROVINCE-SERVICE")
@RequiredArgsConstructor
public class ProvinceServiceImpl implements ProvinceService {

    private final ProvinceRepository provinceRepository;
    private final ProvinceMapper provinceMapper;
    private final PageableService pageableService;
    private final DistrictMapper districtMapper;
    private final DistrictRepository districtRepository;
    private final RestTemplate restTemplate;

    @Value("${ghn.province-url}")
    private String provinceUrl;

    @Value("${ghn.token}")
    private String ghnToken;


    @Override
    public ProvinceResponse fetchById(Long id) {
        Province province = findProvinceById(id);

        return provinceMapper.toProvinceResponse(province);
    }

    @Override
    public PageResponse<ProvinceResponse> fetchAll(int pageNo, int pageSize, String sortBy) {
        pageNo = pageNo - 1;

        Pageable pageable = pageableService.createPageable(pageNo, pageSize, sortBy, Province.class);

        Page<Province> provincePage = provinceRepository.findAll(pageable);

        return PageResponse.<ProvinceResponse>builder()
                .page(provincePage.getNumber() + 1)
                .size(provincePage.getSize())
                .totalPages(provincePage.getTotalPages())
                .totalItems(provincePage.getTotalElements())
                .items(provinceMapper.toProvinceResponseList(provincePage.getContent()))
                .build();
    }

    @Override
    public PageResponse<DistrictResponse> fetchDistrictsByProvinceId(int pageNo, int pageSize, String sortBy, Integer provinceId) {
        pageNo = pageNo - 1;

        Pageable pageable = pageableService.createPageable(pageNo, pageSize, sortBy, District.class);

        Page<District> districtPage = districtRepository.findByProvinceId(provinceId, pageable);

        return PageResponse.<DistrictResponse>builder()
                .page(districtPage.getNumber() + 1)
                .size(districtPage.getSize())
                .totalPages(districtPage.getTotalPages())
                .totalItems(districtPage.getTotalElements())
                .items(districtMapper.toDistrictResponseList(districtPage.getContent()))
                .build();
    }


    private Province findProvinceById(Long id) {
        return provinceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROVINCE_NOT_EXISTED));
    }

    public List<Province> loadProvincesFromGhn(){
        log.info("Starting API call to GHN to fetch list of provinces");

        // Set header token
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", ghnToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<GhnProvinceResponse> response = restTemplate.exchange(
                    provinceUrl,
                    HttpMethod.POST,
                    requestEntity,
                    GhnProvinceResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<GhnProvince> ghnProvinceList = response.getBody().getData();

                return provinceMapper.GhnProvinceListToProvinceList(ghnProvinceList);
            }

        } catch (Exception e) {
            log.error("Lỗi khi gọi API GHN để lấy Tỉnh: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi khi gọi API GHN (Tỉnh) : " + e.getMessage(), e);
        }
        return List.of();
    }
}
