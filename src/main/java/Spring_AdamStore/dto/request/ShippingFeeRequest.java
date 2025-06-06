package Spring_AdamStore.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShippingFeeRequest {

     Integer serviceTypeId;
     @NotNull(message = "insuranceValue không được để trống")
     @Min(value = 0, message = "insuranceValue phải lớn hơn hoặc bằng 0")
     Integer insuranceValue;
     String coupon;
     @NotBlank(message = "toWardCode không được để trống")
     String toWardCode;
     @NotNull(message = "toDistrictId không được để trống")
     @Min(value = 0, message = "toDistrictId phải lớn hơn hoặc bằng 0")
     Integer toDistrictId;
     @NotNull(message = "fromDistrictId không được để trống")
     @Min(value = 0, message = "fromDistrictId phải lớn hơn hoặc bằng 0")
     Integer fromDistrictId;
     Integer weight;
     Integer length;
     Integer width;
     Integer height;
}
