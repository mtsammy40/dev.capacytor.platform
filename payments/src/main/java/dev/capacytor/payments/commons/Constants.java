package dev.capacytor.payments.commons;

public class Constants {

    public static class Endpoints {
        public static final String PLATFORM = "/platform";
        public static final String V1 = "/v1";
        public static final String PAYMENTS = "/payments";
        public static final String METHODS = "/methods";
        public static final String WEBHOOKS = "/webhooks";
        public static final String PLATFORM_V1 = PLATFORM + V1;
        public static final String V1_PAYMENTS = V1 + PAYMENTS;
        public static final String PLATFORM_V1_PAYMENTS = PLATFORM_V1 + PAYMENTS;
        public static final String PLATFORM_V1_METHODS = PLATFORM_V1 + METHODS;
        public static final String PLATFORM_V1_WEBHOOKS = PLATFORM_V1 + WEBHOOKS;
    }
}
