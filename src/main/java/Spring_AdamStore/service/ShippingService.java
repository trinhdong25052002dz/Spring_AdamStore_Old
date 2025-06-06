package Spring_AdamStore.service;

import Spring_AdamStore.dto.request.ShippingRequest;
import Spring_AdamStore.dto.response.ShippingFeeResponse;

public interface ShippingService {

    ShippingFeeResponse shippingCost(ShippingRequest request);

}
