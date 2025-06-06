package Spring_AdamStore.mapper;

import Spring_AdamStore.dto.basic.EntityBasic;
import Spring_AdamStore.dto.response.GhnProvince;
import Spring_AdamStore.dto.response.ProvinceResponse;
import Spring_AdamStore.entity.District;
import Spring_AdamStore.entity.Province;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ProvinceMapper {

    ProvinceResponse toProvinceResponse(Province province);

    @Mapping(target = "id", source = "provinceId")
    @Mapping(target = "name", source = "provinceName")
    Province GhnProvinceToProvince(GhnProvince ghnProvince);

    List<Province> GhnProvinceListToProvinceList(List<GhnProvince> ghnProvinceList);

    List<ProvinceResponse> toProvinceResponseList(List<Province> provinceList);

    EntityBasic toEntityBasic(Province province);
}
