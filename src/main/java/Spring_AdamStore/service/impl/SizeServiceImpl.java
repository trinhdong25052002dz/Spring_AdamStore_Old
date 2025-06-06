package Spring_AdamStore.service.impl;

import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.dto.response.SizeResponse;
import Spring_AdamStore.entity.Size;
import Spring_AdamStore.exception.AppException;
import Spring_AdamStore.exception.ErrorCode;
import Spring_AdamStore.mapper.SizeMapper;
import Spring_AdamStore.repository.SizeRepository;
import Spring_AdamStore.service.PageableService;
import Spring_AdamStore.service.SizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "SIZE-SERVICE")
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {

    private final SizeRepository sizeRepository;
    private final SizeMapper sizeMapper;
    private final PageableService pageableService;

    @Override
    public SizeResponse fetchById(Long id) {
        Size size = findSizeById(id);

        return sizeMapper.toSizeResponse(size);
    }

    @Override
    public PageResponse<SizeResponse> fetchAll(int pageNo, int pageSize, String sortBy) {
        pageNo = pageNo - 1;

        Pageable pageable = pageableService.createPageable(pageNo, pageSize, sortBy, Size.class);

        Page<Size> sizePage = sizeRepository.findAll(pageable);

        return PageResponse.<SizeResponse>builder()
                .page(sizePage.getNumber() + 1)
                .size(sizePage.getSize())
                .totalPages(sizePage.getTotalPages())
                .totalItems(sizePage.getTotalElements())
                .items(sizeMapper.toSizeResponseList(sizePage.getContent()))
                .build();
    }


    private Size findSizeById(Long id) {
        return sizeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_EXISTED));
    }
}
