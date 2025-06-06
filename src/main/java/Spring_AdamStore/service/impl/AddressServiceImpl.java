package Spring_AdamStore.service.impl;

import Spring_AdamStore.constants.EntityStatus;
import Spring_AdamStore.constants.OrderStatus;
import Spring_AdamStore.dto.request.AddressRequest;
import Spring_AdamStore.dto.response.AddressResponse;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.entity.*;
import Spring_AdamStore.exception.AppException;
import Spring_AdamStore.exception.ErrorCode;
import Spring_AdamStore.mapper.AddressMapper;
import Spring_AdamStore.repository.*;
import Spring_AdamStore.service.AddressService;
import Spring_AdamStore.service.CurrentUserService;
import Spring_AdamStore.service.PageableService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j(topic = "ADDRESS-SERVICE")
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final PageableService pageableService;
    private final DistrictRepository districtRepository;
    private final ProvinceRepository provinceRepository;
    private final WardRepository wardRepository;
    private final CurrentUserService currentUserService;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public AddressResponse create(AddressRequest request) {
        Address address = addressMapper.toAddress(request);

        Ward ward = findWardByCode(request.getWardCode());

        District district = findDistrictById(request.getDistrictId());

        if (!ward.getDistrict().getId().equals(district.getId())) {
            throw new AppException(ErrorCode.INVALID_DISTRICT_FOR_WARD);
        }

        Province province = findProvinceById(request.getProvinceId());

        if (!district.getProvince().getId().equals(province.getId())) {
            throw new AppException(ErrorCode.INVALID_PROVINCE_FOR_DISTRICT);
        }

        address.setWard(ward);
        address.setProvince(province);
        address.setDistrict(district);

        User user = currentUserService.getCurrentUser();
        address.setUser(user);

        if (Boolean.TRUE.equals(request.getIsDefault())) {
            List<Address> addressList = addressRepository.findAllByUser(user);
            addressList.stream()
                    .filter(Address::getIsDefault)
                    .forEach(userAddress -> {
                        userAddress.setIsDefault(false);
                        addressRepository.save(userAddress);
                    });
        }

        return addressMapper.toAddressResponse(addressRepository.save(address));
    }

    @Override
    public AddressResponse fetchById(Long id) {
        Address address = findAddressById(id);

        return addressMapper.toAddressResponse(address);
    }

    @Override
    public PageResponse<AddressResponse> fetchAllForAdmin(int pageNo, int pageSize, String sortBy) {
        pageNo = pageNo - 1;

        Pageable pageable = pageableService.createPageable(pageNo, pageSize, sortBy, Address.class);

        Page<Address> addressPage = addressRepository.findAllAddresses(pageable);

        return PageResponse.<AddressResponse>builder()
                .page(addressPage.getNumber() + 1)
                .size(addressPage.getSize())
                .totalPages(addressPage.getTotalPages())
                .totalItems(addressPage.getTotalElements())
                .items(addressMapper.toAddressResponseList(addressPage.getContent()))
                .build();
    }

    @Override
    @Transactional
    public AddressResponse update(Long id, AddressRequest request) {
        Address address = findAddressById(id);

        addressMapper.update(address, request);

        Ward ward = findWardByCode(request.getWardCode());

        District district = findDistrictById(request.getDistrictId());

        if (!ward.getDistrict().getId().equals(district.getId())) {
            throw new AppException(ErrorCode.INVALID_DISTRICT_FOR_WARD);
        }

        Province province = findProvinceById(request.getProvinceId());

        if (!district.getProvince().getId().equals(province.getId())) {
            throw new AppException(ErrorCode.INVALID_PROVINCE_FOR_DISTRICT);
        }

        address.setWard(ward);
        address.setProvince(province);
        address.setDistrict(district);

        if (Boolean.TRUE.equals(request.getIsDefault())) {
            List<Address> addressList = addressRepository.findAllByUser(address.getUser());
            addressList.stream()
                    .filter(Address::getIsDefault)
                    .forEach(userAddress -> {
                        userAddress.setIsDefault(false);
                        addressRepository.save(userAddress);
                    });
            address.setIsDefault(true);
        }

        return addressMapper.toAddressResponse(addressRepository.save(address));
    }

    @Override
    @Transactional
    public void hideAddress(Long id) {
        Address address = findAddressById(id);
        address.setIsVisible(false);

        addressRepository.save(address);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Address address = findAddressById(id);

        if (Boolean.TRUE.equals(address.getIsDefault())) {
            throw new AppException(ErrorCode.DEFAULT_ADDRESS_CANNOT_BE_DELETED);
        }

        if(orderRepository.existsByAddressIdAndStatusIn(address.getId(),
                List.of(OrderStatus.PENDING, OrderStatus.PROCESSING, OrderStatus.SHIPPED,
                        OrderStatus.DELIVERED, OrderStatus.CANCELLED))){
            throw new AppException(ErrorCode.ADDRESS_USED_IN_ORDER);
        }

        addressRepository.delete(address);
    }

    @Override
    @Transactional
    public AddressResponse restore(long id) {
        Address address = addressRepository.findAddressById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));

        address.setStatus(EntityStatus.ACTIVE);
        return addressMapper.toAddressResponse(addressRepository.save(address));
    }


    private Address findAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));
    }

    private Ward findWardByCode(String code) {
        return wardRepository.findByCode(code)
                .orElseThrow(() -> new AppException(ErrorCode.WARD_NOT_EXISTED));
    }

    private District findDistrictById(Long id) {
        return districtRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DISTRICT_NOT_EXISTED));
    }

    private Province findProvinceById(Long id) {
        return provinceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROVINCE_NOT_EXISTED));
    }
}


