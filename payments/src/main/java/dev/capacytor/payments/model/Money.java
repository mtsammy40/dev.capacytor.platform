package dev.capacytor.payments.model;

import java.math.BigDecimal;

public class Money extends BigDecimal {
    public Money(String in) {
        super(in.toCharArray(), 0, in.length());
    }
}
