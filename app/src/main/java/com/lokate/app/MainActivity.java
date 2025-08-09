package com.lokate.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPrefs;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        sharedPrefs = getSharedPreferences("LOKATE_PREFS", MODE_PRIVATE);
        
        // Check if user is already logged in
        String token = sharedPrefs.getString("jwt_token", null);
        
        if (token != null && !token.isEmpty()) {
            // User is logged in, go to home
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            // User is not logged in, go to login
            startActivity(new Intent(this, LoginActivity.class));
        }
        
        finish();
    }
}
