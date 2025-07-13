
package com.mobiletracker.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobiletracker.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _otpSent = MutableSharedFlow<Boolean>()
    val otpSent: SharedFlow<Boolean> = _otpSent
    
    private val _loginSuccess = MutableSharedFlow<Boolean>()
    val loginSuccess: SharedFlow<Boolean> = _loginSuccess
    
    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage
    
    fun sendOtp(countryCode: String, mobileNumber: String) {
        viewModelScope.launch {
            authRepository.sendOtp(countryCode, mobileNumber)
                .onSuccess {
                    _otpSent.emit(true)
                }
                .onFailure { error ->
                    _errorMessage.emit(error.message ?: "Failed to send OTP")
                }
        }
    }
    
    fun signin(countryCode: String, mobileNumber: String, otp: String) {
        viewModelScope.launch {
            authRepository.signin(countryCode, mobileNumber, otp)
                .onSuccess {
                    _loginSuccess.emit(true)
                }
                .onFailure { error ->
                    _errorMessage.emit(error.message ?: "Login failed")
                }
        }
    }
}
