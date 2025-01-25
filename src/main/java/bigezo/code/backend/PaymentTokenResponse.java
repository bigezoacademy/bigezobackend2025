package bigezo.code.backend;

public class PaymentTokenResponse {
    private String token;
    private String expiryDate;
    private String error;
    private String status;
    private String message;

    // Constructor
    public PaymentTokenResponse(String token, String expiryDate, String error, String status, String message) {
        this.token = token;
        this.expiryDate = expiryDate;
        this.error = error;
        this.status = status;
        this.message = message;
    }

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

