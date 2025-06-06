package Spring_AdamStore.service.impl;

import Spring_AdamStore.dto.response.*;
import Spring_AdamStore.entity.District;
import Spring_AdamStore.entity.Ward;
import Spring_AdamStore.exception.AppException;
import Spring_AdamStore.exception.ErrorCode;
import Spring_AdamStore.mapper.DistrictMapper;
import Spring_AdamStore.mapper.WardMapper;
import Spring_AdamStore.repository.DistrictRepository;
import Spring_AdamStore.repository.WardRepository;
import Spring_AdamStore.service.DistrictService;
import Spring_AdamStore.service.PageableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j(topic = "DISTRICT-SERVICE")
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {

    private final DistrictMapper districtMapper;
    private final DistrictRepository districtRepository;
    private final PageableService pageableService;
    private final WardMapper wardMapper;
    private final WardRepository wardRepository;
    private final RestTemplate restTemplate;

    @Value("${ghn.district-url}")
    private String districtUrl;

    @Value("${ghn.token}")
    private String ghnToken;

    @Override
    public DistrictResponse fetchById(Long id) {
        District district = findDistrictById(id);

        return districtMapper.toDistrictResponse(district);
    }

    @Override
    public PageResponse<DistrictResponse> fetchAll(int pageNo, int pageSize, String sortBy) {
        pageNo = pageNo - 1;

        Pageable pageable = pageableService.createPageable(pageNo, pageSize, sortBy, District.class);

        Page<District> districtPage = districtRepository.findAll(pageable);

        return PageResponse.<DistrictResponse>builder()
                .page(districtPage.getNumber() + 1)
                .size(districtPage.getSize())
                .totalPages(districtPage.getTotalPages())
                .totalItems(districtPage.getTotalElements())
                .items(districtMapper.toDistrictResponseList(districtPage.getContent()))
                .build();
    }


    private District findDistrictById(Long id) {
        return districtRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DISTRICT_NOT_EXISTED));
    }

    public List<District> loadDistrictsFromGhn(int provinceId){
        log.info("Starting API call to GHN to fetch list of districts for ProvinceId : {}", provinceId);
        // Set header token
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", ghnToken);

        // Tạo body với province_id dưới dạng Map
        Map<String, Integer> body = new HashMap<>();
        body.put("province_id", provinceId);

        // Tạo HttpEntity với body là Map và headers
        HttpEntity<Map<String, Integer>> requestEntity = new HttpEntity<>(body, headers);

        try{
            ResponseEntity<GhnDistrictResponse> response = restTemplate.exchange(
                    districtUrl,
                    HttpMethod.POST,
                    requestEntity,
                    GhnDistrictResponse.class
            );
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<GhnDistrict> ghnDistrictList = response.getBody().getData();

                if (!CollectionUtils.isEmpty(ghnDistrictList)) {
                    return districtMapper.GhnDistrictListToDistrictList(ghnDistrictList);
                }
            }
        }
        catch (Exception e) {
            log.error("Lỗi khi gọi API GHN để lấy Quận/Huyện: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi khi gọi API GHN (Quận/Huyện) : " + e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public PageResponse<WardResponse> fetchWardsByDistrictId(int pageNo, int pageSize, String sortBy, Integer districtId) {
        pageNo = pageNo - 1;

        Pageable pageable = pageableService.createPageable(pageNo, pageSize, sortBy, Ward.class);

        Page<Ward> wardPage = wardRepository.findByDistrictId(districtId, pageable);

        return PageResponse.<WardResponse>builder()
                .page(wardPage.getNumber() + 1)
                .size(wardPage.getSize())
                .totalPages(wardPage.getTotalPages())
                .totalItems(wardPage.getTotalElements())
                .items(wardMapper.toWardResponseList(wardPage.getContent()))
                .build();
    }
}
