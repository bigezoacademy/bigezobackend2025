package bigezo.code.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionStatusResponse {

    @JsonProperty("created_date")
    private String createdDate;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("amount")
    private double amount;

    @JsonProperty("confirmation_code")
    private String confirmationCode;

    @JsonProperty("order_tracking_id")
    private String orderTrackingId;

    @JsonProperty("payment_status_description")
    private String paymentStatusDescription;

    @JsonProperty("description")
    private String description;

    @JsonProperty("message")
    private String message;

    @JsonProperty("payment_account")
    private String paymentAccount;

    @JsonProperty("call_back_url")
    private String callbackUrl;

    @JsonProperty("status_code")
    private int statusCode;

    @JsonProperty("merchant_reference")
    private String merchantReference;

    @JsonProperty("account_number")
    private String accountNumber;

    @JsonProperty("payment_status_code")
    private String paymentStatusCode;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("error")
    private ErrorDetails error;

    @JsonProperty("status")
    private String status;

    // Getters and Setters
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public String getOrderTrackingId() {
        return orderTrackingId;
    }

    public void setOrderTrackingId(String orderTrackingId) {
        this.orderTrackingId = orderTrackingId;
    }

    public String getPaymentStatusDescription() {
        return paymentStatusDescription;
    }

    public void setPaymentStatusDescription(String paymentStatusDescription) {
        this.paymentStatusDescription = paymentStatusDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMerchantReference() {
        return merchantReference;
    }

    public void setMerchantReference(String merchantReference) {
        this.merchantReference = merchantReference;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPaymentStatusCode() {
        return paymentStatusCode;
    }

    public void setPaymentStatusCode(String paymentStatusCode) {
        this.paymentStatusCode = paymentStatusCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public ErrorDetails getError() {
        return error;
    }

    public void setError(ErrorDetails error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class ErrorDetails {
        @JsonProperty("error_type")
        private String errorType;

        @JsonProperty("code")
        private String code;

        @JsonProperty("message")
        private String message;

        // Getters and Setters
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
