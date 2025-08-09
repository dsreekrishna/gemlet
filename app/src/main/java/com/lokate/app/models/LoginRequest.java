
package com.lokate.app.models;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("mobile_country_code")
    private String mobileCountryCode;
    
    @SerializedName("mobile_number")
    private String mobileNumber;
    
    @SerializedName("otp")
    private String otp;

    public LoginRequest(String mobileCountryCode, String mobileNumber, String otp) {
        this.mobileCountryCode = mobileCountryCode;
        this.mobileNumber = mobileNumber;
        this.otp = otp;
    }

    public String getMobileCountryCode() {
        return mobileCountryCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getOtp() {
        return otp;
    }
}
