package dev.capacytor.payments.provider.cash.model;

import dev.capacytor.payments.model.PayData;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class CashPayData extends PayData {
    String phoneNumber;
}