package dev.capacytor.payments.commons;

public class Constants {

    public static class Endpoints {
        public static final String API = "/api";
        public static final String WEB = "/web";
        public static final String PLATFORM = "/platform";
        public static final String API_V1 = API + "/v1";
        public static final String WEB_V1 = WEB + "/v1";
        public static final String PAYMENTS = "/payments";
        public static final String METHODS = "/methods";
        public static final String WEBHOOKS = "/webhooks";
        public static final String PLATFORM_API_V1 = PLATFORM + API_V1;
        public static final String API_V1_PAYMENTS = API_V1 + PAYMENTS;
        public static final String PLATFORM_API_V1_PAYMENTS = PLATFORM_API_V1 + PAYMENTS;
        public static final String PLATFORM_API_V1_METHODS = PLATFORM_API_V1 + METHODS;
        public static final String PLATFORM_API_V1_WEBHOOKS = PLATFORM_API_V1 + WEBHOOKS;
        public static final String WEB_V1_PAYMENTS = WEB_V1 + PAYMENTS;
    }
}
