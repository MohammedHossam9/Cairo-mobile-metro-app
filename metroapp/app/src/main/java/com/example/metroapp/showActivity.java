package com.example.metroapp;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.metroapp.data.RouteResult;
import com.example.metroapp.data.StationData;
import com.example.metroapp.services.RouteCalculator;
import com.example.metroapp.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import mumayank.com.airlocationlibrary.AirLocation;

public class showActivity extends AppCompatActivity implements AirLocation.Callback {
    private RouteResult currentRoute;
    private Location loc1 = new Location("");
    private Location loc2 = new Location("");
    private AirLocation airLocation;
    private TextView routeDetailsText;
    private String currentRouteText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        routeDetailsText = findViewById(R.id.routeDetailsText);
        String currentStation = getIntent().getStringExtra("start");
        String arrivalStation = getIntent().getStringExtra("end");

        calculateRoute(currentStation, arrivalStation);
    }

    private void calculateRoute(String currentStation, String arrivalStation) {
        if (currentStation == null || arrivalStation == null ||
            currentStation.equalsIgnoreCase(arrivalStation)) {
            Toast.makeText(this, "Invalid route", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Use the new RouteCalculator service
        currentRoute = RouteCalculator.calculateRoute(currentStation, arrivalStation);

        if (!currentRoute.isValid()) {
            Toast.makeText(this, "No route found", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            displayRouteDetails(currentRoute);
            YoYo.with(Techniques.FadeIn).duration(1000).playOn(routeDetailsText);
        }
    }

    private void displayRouteDetails(RouteResult route) {
        StringBuilder result = new StringBuilder();

        // Header with line info
        result.append("━━━━━━━━━━━━━━━━━━━━━━━━\n");
        result.append("📍 ROUTE DETAILS\n");
        result.append("━━━━━━━━━━━━━━━━━━━━━━━━\n\n");

        // Journey info
        result.append("🚉 Stations: ").append(route.getStationCount()).append("\n");
        result.append("⏱️ Time: ").append(route.getFormattedTime()).append("\n");
        result.append("🎫 Ticket: ").append(route.getTicketPrice()).append(" EGP\n");

        // Transfer info
        if (route.hasTransfer()) {
            result.append("🔄 Transfer at: ").append(route.getTransferStation()).append("\n");
        }

        result.append("➡️ Direction: ").append(route.getDirection()).append("\n\n");

        // Route visualization
        result.append("━━━━━━━━━━━━━━━━━━━━━━━━\n");
        result.append("🛤️ YOUR ROUTE\n");
        result.append("━━━━━━━━━━━━━━━━━━━━━━━━\n\n");

        List<String> stations = route.getStations();
        for (int i = 0; i < stations.size(); i++) {
            String station = stations.get(i);

            if (i == 0) {
                result.append("🟢 ").append(station).append(" (Start)\n");
            } else if (i == stations.size() - 1) {
                result.append("🔴 ").append(station).append(" (Destination)\n");
            } else if (station.equals(route.getTransferStation())) {
                result.append("🔄 ").append(station).append(" (Transfer)\n");
            } else {
                result.append("   ⬇️\n");
                result.append("   ").append(station).append("\n");
            }
        }

        currentRouteText = result.toString();
        routeDetailsText.setText(currentRouteText);
    }

    public void shareRoute(View view) {
        if (currentRouteText.isEmpty()) {
            Toast.makeText(this, "No route to share", Toast.LENGTH_SHORT).show();
            return;
        }
        UIUtils.shareRoute(this, currentRouteText);
    }

    public void remainingDistance(View view) {
        if (currentRoute == null || !currentRoute.isValid()) {
            Toast.makeText(this, "No active route", Toast.LENGTH_SHORT).show();
            return;
        }

        airLocation = new AirLocation(this, this, true, 0, "");
        airLocation.start();
    }

    @Override
    public void onFailure(@NonNull AirLocation.LocationFailedEnum locationFailedEnum) {
        Toast.makeText(this, "Location error. Please enable GPS.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(@NonNull ArrayList<Location> arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            Toast.makeText(this, "Could not get location", Toast.LENGTH_SHORT).show();
            return;
        }

        double latitude = arrayList.get(0).getLatitude();
        double longitude = arrayList.get(0).getLongitude();
        loc1.setLatitude(latitude);
        loc1.setLongitude(longitude);

        // Find nearest station to current location
        Station nearestStation = null;
        float minDistance = Float.MAX_VALUE;

        for (Station station : StationData.ALL_STATIONS_WITH_COORDINATES) {
            loc2.setLatitude(station.getLatitude());
            loc2.setLongitude(station.getLongitude());
            float distance = loc1.distanceTo(loc2);

            if (distance < minDistance) {
                minDistance = distance;
                nearestStation = station;
            }
        }

        if (nearestStation != null) {
            // Calculate remaining stations
            int remaining = RouteCalculator.calculateRemainingStations(
                currentRoute.getStations(),
                nearestStation.getName()
            );

            if (remaining >= 0) {
                int remainingTime = remaining * 2;
                String message = String.format(
                    "📍 Current: %s\n🚉 Remaining: %d stations\n⏱️ Time: %d min",
                    nearestStation.getName(),
                    remaining,
                    remainingTime
                );
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "You are not on the current route", Toast.LENGTH_SHORT).show();
            }
        }
    }
}