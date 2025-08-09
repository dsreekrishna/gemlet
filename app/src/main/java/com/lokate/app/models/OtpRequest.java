
package com.lokate.app.models;

import com.google.gson.annotations.SerializedName;

public class OtpRequest {
    @SerializedName("mobile_country_code")
    private String mobileCountryCode;
    
    @SerializedName("mobile_number")
    private String mobileNumber;

    public OtpRequest(String mobileCountryCode, String mobileNumber) {
        this.mobileCountryCode = mobileCountryCode;
        this.mobileNumber = mobileNumber;
    }

    public String getMobileCountryCode() {
        return mobileCountryCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }
}
