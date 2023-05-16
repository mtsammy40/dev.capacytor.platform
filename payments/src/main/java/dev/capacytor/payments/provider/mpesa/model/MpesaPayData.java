package dev.capacytor.payments.provider.mpesa.model;

import dev.capacytor.payments.model.PayData;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class MpesaPayData extends PayData {
    String phoneNumber;
}
