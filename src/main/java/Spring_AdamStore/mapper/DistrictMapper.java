package Spring_AdamStore.mapper;

import Spring_AdamStore.dto.basic.EntityBasic;
import Spring_AdamStore.dto.response.DistrictResponse;
import Spring_AdamStore.dto.response.GhnDistrict;
import Spring_AdamStore.dto.response.GhnProvince;
import Spring_AdamStore.dto.response.ProvinceResponse;
import Spring_AdamStore.entity.District;
import Spring_AdamStore.entity.Province;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface DistrictMapper {

    DistrictResponse toDistrictResponse(District district);

    @Mapping(target = "id", source = "districtId")
    @Mapping(target = "name", source = "districtName")
    District GhnDistrictToDistrict(GhnDistrict ghnDistrict);

    List<District> GhnDistrictListToDistrictList(List<GhnDistrict> ghnDistrictList);

    List<DistrictResponse> toDistrictResponseList(List<District> districtList);

    EntityBasic toEntityBasic(District district);
}
