package com.lokate.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lokate.app.api.ApiClient;
import com.lokate.app.api.ApiService;
import com.lokate.app.models.LoginRequest;
import com.lokate.app.models.LoginResponse;
import com.lokate.app.models.OtpRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etMobileNumber, etOtp;
    private Button btnSendOtp, btnVerifyOtp;
    private ProgressBar progressBar;
    private ApiService apiService;
    private SharedPreferences sharedPrefs;
    private boolean otpSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupClickListeners();
        
        apiService = ApiClient.getClient().create(ApiService.class);
        sharedPrefs = getSharedPreferences("LOKATE_PREFS", MODE_PRIVATE);
    }

    private void initViews() {
        etMobileNumber = findViewById(R.id.etMobileNumber);
        etOtp = findViewById(R.id.etOtp);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        progressBar = findViewById(R.id.progressBar);
        
        // Initially hide OTP fields
        etOtp.setVisibility(View.GONE);
        btnVerifyOtp.setVisibility(View.GONE);
    }

    private void setupClickListeners() {
        btnSendOtp.setOnClickListener(v -> sendOtp());
        btnVerifyOtp.setOnClickListener(v -> verifyOtp());
    }

    private void sendOtp() {
        String mobileNumber = etMobileNumber.getText().toString().trim();
        
        if (mobileNumber.length() != 10) {
            Toast.makeText(this, "Please enter valid 10-digit mobile number", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnSendOtp.setEnabled(false);

        OtpRequest request = new OtpRequest("+91", mobileNumber);
        
        apiService.sendOtp(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                btnSendOtp.setEnabled(true);
                
                if (response.isSuccessful()) {
                    otpSent = true;
                    etOtp.setVisibility(View.VISIBLE);
                    btnVerifyOtp.setVisibility(View.VISIBLE);
                    btnSendOtp.setText("Resend OTP");
                    Toast.makeText(LoginActivity.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnSendOtp.setEnabled(true);
                Toast.makeText(LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyOtp() {
        String mobileNumber = etMobileNumber.getText().toString().trim();
        String otp = etOtp.getText().toString().trim();
        
        if (otp.length() != 6) {
            Toast.makeText(this, "Please enter valid 6-digit OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnVerifyOtp.setEnabled(false);

        LoginRequest request = new LoginRequest("+91", mobileNumber, otp);
        
        apiService.verifyOtp(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnVerifyOtp.setEnabled(true);
                
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    
                    // Save user data
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString("jwt_token", loginResponse.getAccessToken());
                    editor.putString("refresh_token", loginResponse.getRefreshToken());
                    editor.putString("mobile_number", mobileNumber);
                    editor.putString("mobile_country_code", "+91");
                    editor.putInt("user_id", loginResponse.getUserId());
                    editor.apply();
                    
                    // Navigate to home
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnVerifyOtp.setEnabled(true);
                Toast.makeText(LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
