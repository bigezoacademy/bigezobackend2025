package bigezo.code.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentResponse {
    @JsonProperty("order_tracking_id")
    private String orderTrackingId;
    
    @JsonProperty("merchant_reference")
    private String merchantReference;
    
    @JsonProperty("redirect_url")
    private String redirectUrl;
    
    private Error error;
    
    @JsonProperty("status")
    private String status;

    public String getOrderTrackingId() {
        return orderTrackingId;
    }

    public void setOrderTrackingId(String orderTrackingId) {
        this.orderTrackingId = orderTrackingId;
    }

    public String getMerchantReference() {
        return merchantReference;
    }

    public void setMerchantReference(String merchantReference) {
        this.merchantReference = merchantReference;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Error {
        @JsonProperty("error_type")
        private String errorType;
        
        @JsonProperty("code")
        private String code;
        
        @JsonProperty("message")
        private String message;

        public String getErrorType() {
            return errorType;
        }

        public void setErrorType(String errorType) {
            this.errorType = errorType;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
