
package com.lokate.app.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("access_token")
    private String accessToken;
    
    @SerializedName("refresh_token")
    private String refreshToken;
    
    @SerializedName("user_id")
    private int userId;
    
    @SerializedName("token_type")
    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public int getUserId() {
        return userId;
    }

    public String getTokenType() {
        return tokenType;
    }
}
