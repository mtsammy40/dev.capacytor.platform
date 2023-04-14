package dev.capacytor.forms.entity.shared;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Getter
public class Amount {
    private BigDecimal amount;
    private Currency currency;

    public Amount(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = Currency.valueOf(currency);
    }

    public static Amount from(String amount, String currency) {
        var amountValue = new BigDecimal(amount).setScale(2, RoundingMode.HALF_EVEN);
        return new Amount(amountValue, currency);
    }

    @Override
    public String toString() {
        if(Objects.nonNull(amount) && Objects.nonNull(currency)) {
            return currency.toString().concat(" ").concat(amount.toString());
        } else {
            return "Amount[null]";
        }
    }
}
