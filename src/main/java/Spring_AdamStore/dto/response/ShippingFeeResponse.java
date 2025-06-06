package Spring_AdamStore.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingFeeResponse {

     @JsonProperty("total")
     Integer total;
     @JsonProperty("service_fee")
     Integer serviceFee;
     @JsonProperty("insurance_fee")
     Integer insuranceFee;
     @JsonProperty("r2s_fee")
     Integer pickStationFee;
     @JsonProperty("coupon_value")
     Integer couponValue;
     @JsonProperty("pick_station_fee")
     Integer r2sFee;
}
