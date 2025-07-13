
package com.mobiletracker.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mobiletracker.app.databinding.ActivityLoginBinding
import com.mobiletracker.app.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        observeViewModel()
    }
    
    private fun setupUI() {
        binding.btnSendOtp.setOnClickListener {
            val mobile = binding.etMobileNumber.text.toString().trim()
            if (mobile.length == 10) {
                viewModel.sendOtp("+91", mobile)
            } else {
                Toast.makeText(this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show()
            }
        }
        
        binding.btnVerifyOtp.setOnClickListener {
            val mobile = binding.etMobileNumber.text.toString().trim()
            val otp = binding.etOtp.text.toString().trim()
            if (mobile.length == 10 && otp.length == 6) {
                viewModel.signin("+91", mobile, otp)
            } else {
                Toast.makeText(this, "Please enter valid mobile number and OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.otpSent.collect { sent ->
                if (sent) {
                    binding.etOtp.isEnabled = true
                    binding.btnVerifyOtp.isEnabled = true
                    Toast.makeText(this@LoginActivity, "OTP sent successfully", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        lifecycleScope.launch {
            viewModel.loginSuccess.collect { success ->
                if (success) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
        
        lifecycleScope.launch {
            viewModel.errorMessage.collect { error ->
                if (error.isNotEmpty()) {
                    Toast.makeText(this@LoginActivity, error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
