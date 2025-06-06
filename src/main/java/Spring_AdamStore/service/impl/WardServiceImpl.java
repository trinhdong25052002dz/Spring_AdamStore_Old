package Spring_AdamStore.service.impl;

import Spring_AdamStore.dto.response.*;
import Spring_AdamStore.entity.Ward;
import Spring_AdamStore.exception.AppException;
import Spring_AdamStore.exception.ErrorCode;
import Spring_AdamStore.mapper.WardMapper;
import Spring_AdamStore.repository.WardRepository;
import Spring_AdamStore.service.PageableService;
import Spring_AdamStore.service.WardService;
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
@Slf4j(topic = "WARD-SERVICE")
@RequiredArgsConstructor
public class WardServiceImpl implements WardService {

    private final WardRepository wardRepository;
    private final WardMapper wardMapper;
    private final PageableService pageableService;
    private final RestTemplate restTemplate;

    @Value("${ghn.ward-url}")
    private String wardUrl;

    @Value("${ghn.token}")
    private String ghnToken;


    @Override
    public WardResponse fetchById(String code) {
        Ward ward = findWardById(code);

        return wardMapper.toWardResponse(ward);
    }

    @Override
    public PageResponse<WardResponse> fetchAll(int pageNo, int pageSize, String sortBy) {
        pageNo = pageNo - 1;

        Pageable pageable = pageableService.createPageable(pageNo, pageSize, sortBy, Ward.class);

        Page<Ward> wardPage = wardRepository.findAll(pageable);

        return PageResponse.<WardResponse>builder()
                .page(wardPage.getNumber() + 1)
                .size(wardPage.getSize())
                .totalPages(wardPage.getTotalPages())
                .totalItems(wardPage.getTotalElements())
                .items(wardMapper.toWardResponseList(wardPage.getContent()))
                .build();
    }

    @Override
    public List<Ward> loadWardsFromGhn(int districtId) {
        log.info("Starting API call to GHN to fetch list of wards for District Id : {}", districtId);

        // Set header token
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", ghnToken);

        // Tao body với district_id
        Map<String, Integer> body = new HashMap<>();
        body.put("district_id", districtId);

        // Tao HttpEntity voi body và headers
        HttpEntity<Map<String, Integer>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<GhnWardResponse> response = restTemplate.exchange(
                    wardUrl,
                    HttpMethod.POST,
                    requestEntity,
                    GhnWardResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<GhnWard> ghnWardList = response.getBody().getData();

                if (!CollectionUtils.isEmpty(ghnWardList)) {
                    return wardMapper.ghnWardListToWardList(ghnWardList);
                }
            }

        } catch (Exception e) {
            log.error("Lỗi khi gọi API GHN để lấy phường/xã: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi khi gọi API GHN (Phường/Xã) : " + e.getMessage(), e);
        }
        return List.of();
    }

    private Ward findWardById(String code) {
        return wardRepository.findById(code)
                .orElseThrow(() -> new AppException(ErrorCode.WARD_NOT_EXISTED));
    }
}
