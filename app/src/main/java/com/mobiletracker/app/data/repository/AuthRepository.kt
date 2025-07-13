
package com.mobiletracker.app.data.repository

import com.mobiletracker.app.data.api.ApiService
import com.mobiletracker.app.data.models.*
import com.mobiletracker.app.utils.SecurePreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val securePreferences: SecurePreferences
) {
    
    suspend fun sendOtp(mobileCountryCode: String, mobileNumber: String): Result<String> {
        return try {
            val response = apiService.sendOtp(OtpUser(mobileCountryCode, mobileNumber))
            if (response.isSuccessful) {
                Result.success(response.body()?.message ?: "OTP sent successfully")
            } else {
                Result.failure(Exception("Failed to send OTP"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signin(mobileCountryCode: String, mobileNumber: String, otp: String): Result<RefreshTokenResponse> {
        return try {
            val response = apiService.signin(SigninUser(mobileCountryCode, mobileNumber, otp))
            if (response.isSuccessful) {
                val authResponse = response.body()!!
                saveTokens(authResponse.access_token, authResponse.refresh_token)
                Result.success(authResponse)
            } else {
                Result.failure(Exception("Invalid OTP"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun refreshToken(): Result<RefreshTokenResponse> {
        return try {
            val refreshToken = getRefreshToken()
            if (refreshToken.isNullOrEmpty()) {
                return Result.failure(Exception("No refresh token found"))
            }
            
            val response = apiService.refreshToken(RefreshTokenRequest(refreshToken))
            if (response.isSuccessful) {
                val authResponse = response.body()!!
                saveTokens(authResponse.access_token, authResponse.refresh_token)
                Result.success(authResponse)
            } else {
                Result.failure(Exception("Failed to refresh token"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun saveTokens(accessToken: String, refreshToken: String) {
        securePreferences.saveAccessToken(accessToken)
        securePreferences.saveRefreshToken(refreshToken)
    }
    
    fun getAccessToken(): String? = securePreferences.getAccessToken()
    fun getRefreshToken(): String? = securePreferences.getRefreshToken()
    
    fun logout() {
        securePreferences.clearTokens()
    }
    
    fun isLoggedIn(): Boolean = !getAccessToken().isNullOrEmpty()
}
