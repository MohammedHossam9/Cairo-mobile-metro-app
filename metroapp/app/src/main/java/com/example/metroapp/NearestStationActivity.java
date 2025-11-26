package com.example.metroapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.metroapp.data.StationData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;

public class NearestStationActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private EditText nearestStationEditText;
    private EditText destinationEditText;
    private TextView nearestToDestinationResultTextView;
    private Button findNearestStationButton;
    private Button findNearestToDestinationButton;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_station);

        nearestStationEditText = findViewById(R.id.nearestStationEditText);
        destinationEditText = findViewById(R.id.destinationEditText);
        nearestToDestinationResultTextView = findViewById(R.id.nearestToDestinationResultTextView);
        findNearestStationButton = findViewById(R.id.findNearestStationButton);
        findNearestToDestinationButton = findViewById(R.id.findNearestToDestinationButton);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        findNearestStationButton.setOnClickListener(v -> checkLocationPermissionAndFindNearest());
        findNearestToDestinationButton.setOnClickListener(v -> findNearestToDestination());
    }

    private void checkLocationPermissionAndFindNearest() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocationAndFindNearest();
        }
    }

    private void getCurrentLocationAndFindNearest() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        findNearestStation(location);
                    } else {
                        Toast.makeText(this, getString(R.string.no_location_permission), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void findNearestStation(Location currentLocation) {
        Station nearestStation = null;
        float minDistance = Float.MAX_VALUE;

        for (Station station : StationData.ALL_STATIONS_WITH_COORDINATES) {
            float[] results = new float[1];
            Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(),
                                         station.getLatitude(), station.getLongitude(), results);
            float distance = results[0];

            if (distance < minDistance) {
                minDistance = distance;
                nearestStation = station;
            }
        }

        if (nearestStation != null) {
            String result = String.format("📍 %s\n📏 %.2f km away",
                                          nearestStation.getName(), minDistance / 1000);
            nearestStationEditText.setText(result);
        } else {
            nearestStationEditText.setText("No station found");
        }
    }

    private void findNearestToDestination() {
        String destination = destinationEditText.getText().toString();
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter a destination", Toast.LENGTH_SHORT).show();
            return;
        }

        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocationName(destination, 1);
            if (addressList == null || addressList.isEmpty()) {
                Toast.makeText(this, "Could not find location", Toast.LENGTH_SHORT).show();
                return;
            }

            Location destinationLocation = new Location("");
            destinationLocation.setLatitude(addressList.get(0).getLatitude());
            destinationLocation.setLongitude(addressList.get(0).getLongitude());

            Station nearestStation = null;
            float minDistance = Float.MAX_VALUE;

            for (Station station : StationData.ALL_STATIONS_WITH_COORDINATES) {
                Location stationLocation = new Location("");
                stationLocation.setLatitude(station.getLatitude());
                stationLocation.setLongitude(station.getLongitude());

                float distance = destinationLocation.distanceTo(stationLocation);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestStation = station;
                }
            }

            if (nearestStation != null) {
                String result = String.format("📍 Nearest to %s:\n🚉 %s\n📏 %.2f km away",
                                              destination, nearestStation.getName(), minDistance / 1000);
                nearestToDestinationResultTextView.setText(result);
                nearestToDestinationResultTextView.setVisibility(View.VISIBLE);
            }

        } catch (IOException e) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }
}
