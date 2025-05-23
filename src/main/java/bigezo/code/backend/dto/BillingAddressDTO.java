package bigezo.code.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BillingAddressDTO {
    @JsonProperty("email_address")
    private String emailAddress;
    
    @JsonProperty("phone_number")
    private String phoneNumber;
    
    @JsonProperty("country_code")
    private String countryCode;
    
    @JsonProperty("first_name")
    private String firstName;
    
    @JsonProperty("middle_name")
    private String middleName;
    
    @JsonProperty("last_name")
    private String lastName;
    
    @JsonProperty("line_1")
    private String line1;
    
    @JsonProperty("line_2")
    private String line2;
    
    @JsonProperty("city")
    private String city;
    
    @JsonProperty("state")
    private String state;
    
    @JsonProperty("postal_code")
    private String postalCode;
    
    @JsonProperty("zip_code")
    private String zipCode;

    // Getters and Setters
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}