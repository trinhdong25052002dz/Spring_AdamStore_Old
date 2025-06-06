package Spring_AdamStore.mapper;

import Spring_AdamStore.dto.request.AddressRequest;
import Spring_AdamStore.dto.request.BranchRequest;
import Spring_AdamStore.dto.response.AddressResponse;
import Spring_AdamStore.dto.response.BranchResponse;
import Spring_AdamStore.entity.Address;
import Spring_AdamStore.entity.Branch;
import org.mapstruct.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AddressMapper {

    Address toAddress(AddressRequest request);

    @Mapping(target = "userName", source = "user.email")
    AddressResponse toAddressResponse(Address address);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Address address, AddressRequest request);

    List<AddressResponse> toAddressResponseList(List<Address> addressList);
}
