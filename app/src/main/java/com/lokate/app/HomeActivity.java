
package com.lokate.app;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lokate.app.adapters.GroupAdapter;
import com.lokate.app.api.ApiClient;
import com.lokate.app.api.ApiService;
import com.lokate.app.models.Group;
import com.lokate.app.models.LocationUpdate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerGroups;
    private GroupAdapter groupAdapter;
    private FloatingActionButton fabCreateGroup;
    private FusedLocationProviderClient fusedLocationClient;
    private ApiService apiService;
    private SharedPreferences sharedPrefs;
    private static final int LOCATION_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupRecyclerView();
        setupClickListeners();
        
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        apiService = ApiClient.getClient().create(ApiService.class);
        sharedPrefs = getSharedPreferences("LOKATE_PREFS", MODE_PRIVATE);
        
        checkLocationPermissions();
        loadGroups();
    }

    private void initViews() {
        recyclerGroups = findViewById(R.id.recyclerGroups);
        fabCreateGroup = findViewById(R.id.fabCreateGroup);
        setSupportActionBar(findViewById(R.id.toolbar));
    }

    private void setupRecyclerView() {
        groupAdapter = new GroupAdapter(new ArrayList<>(), this::onGroupClick);
        recyclerGroups.setLayoutManager(new LinearLayoutManager(this));
        recyclerGroups.setAdapter(groupAdapter);
    }

    private void setupClickListeners() {
        fabCreateGroup.setOnClickListener(v -> {
            // TODO: Implement create group functionality
            Toast.makeText(this, "Create group functionality coming soon", Toast.LENGTH_SHORT).show();
        });
    }

    private void checkLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 
                LOCATION_PERMISSION_REQUEST);
        } else {
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission is required for this app", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                updateLocationOnServer(location.getLatitude(), location.getLongitude());
            }
        });
    }

    private void updateLocationOnServer(double latitude, double longitude) {
        String token = sharedPrefs.getString("jwt_token", "");
        LocationUpdate locationUpdate = new LocationUpdate(latitude, longitude);
        
        // TODO: Implement location update API call
        // This would require adding the endpoint to your API service
    }

    private void loadGroups() {
        String token = sharedPrefs.getString("jwt_token", "");
        
        apiService.getGroups("Bearer " + token).enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    groupAdapter.updateGroups(response.body());
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to load groups", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onGroupClick(Group group) {
        // TODO: Navigate to group details
        Toast.makeText(this, "Clicked on group: " + group.getGroupDisplayName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();
        
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
