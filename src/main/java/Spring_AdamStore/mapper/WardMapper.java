package Spring_AdamStore.mapper;

import Spring_AdamStore.dto.basic.EntityBasic;
import Spring_AdamStore.dto.basic.WardBasic;
import Spring_AdamStore.dto.response.GhnWard;
import Spring_AdamStore.dto.response.WardResponse;
import Spring_AdamStore.entity.Ward;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface WardMapper {

    WardResponse toWardResponse(Ward ward);

    @Mapping(target = "code", source = "wardCode")
    @Mapping(target = "name", source = "wardName")
    Ward ghnWardToWard(GhnWard ghnWard);

    List<Ward> ghnWardListToWardList(List<GhnWard> ghnWardList);

    List<WardResponse> toWardResponseList(List<Ward> wardList);

    WardBasic toWardBasic(Ward ward);
}
