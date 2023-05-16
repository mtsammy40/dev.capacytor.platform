package dev.capacytor.payments.provider.mpesa;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MpesaApiException extends Exception {

    public enum ErrorCode {
        NO_RESPONSE("E002"),
        // STK PUSH ERRORS ARE E2...
        PUSH_FAILED("E202");

        private final String code;

        ErrorCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
    private ErrorCode errorCode;
    public MpesaApiException(String message, ErrorCode errorCode) {
        super(message);
    }
}
