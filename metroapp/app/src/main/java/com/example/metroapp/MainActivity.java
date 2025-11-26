package com.example.metroapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.metroapp.data.StationData;
import com.example.metroapp.services.PreferencesManager;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mumayank.com.airlocationlibrary.AirLocation;

public class MainActivity extends AppCompatActivity implements AirLocation.Callback {
    private List<String> stations;
    private AutoCompleteTextView startSpinner, endSpinner;
    private Button languageToggleButton;
    private PreferencesManager preferencesManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Initialize preferences manager
        preferencesManager = new PreferencesManager(this);
        String currentLang = preferencesManager.getLanguage();
        setLocale(currentLang);

        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startSpinner = findViewById(R.id.startSpinner);
        endSpinner = findViewById(R.id.endSpinner);
        languageToggleButton = findViewById(R.id.languageButton);

        // Load stations from centralized data source
        setupStations();
        setupAutoCompleteTextViews();

        updateLanguageButtonText();
        languageToggleButton.setOnClickListener(v -> toggleLanguage());

        MaterialButton nearestStationButton = findViewById(R.id.nearestStationButton);
        nearestStationButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NearestStationActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setupStations() {
        // Use centralized station data
        stations = StationData.getAllStationNames();
    }
    private void setupAutoCompleteTextViews() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, stations);
        startSpinner.setAdapter(adapter);
        endSpinner.setAdapter(adapter);

        startSpinner.setThreshold(1);
        endSpinner.setThreshold(1);
    }

    private void toggleLanguage() {
        String currentLang = preferencesManager.getLanguage();
        String newLang = currentLang.equals("en") ? "ar" : "en";
        setLocale(newLang);
        preferencesManager.setLanguage(newLang);
        updateLanguageButtonText();
        recreate();
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    private void updateLanguageButtonText() {
        String currentLang = preferencesManager.getLanguage();
        languageToggleButton.setText(currentLang.equals("en") ? "AR" : "EN");
    }

    public void calc(View view) {
        String currentStation = startSpinner.getText().toString();
        String arrivalStation = endSpinner.getText().toString();

        if (currentStation.equalsIgnoreCase(arrivalStation)) {
            YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(startSpinner);
            YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(endSpinner);
            return;
        }

        if (currentStation.isEmpty() || arrivalStation.isEmpty()) {
            Toast.makeText(this, R.string.please_select_stations, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!stations.contains(currentStation) || !stations.contains(arrivalStation)) {
            Toast.makeText(this, R.string.invalid_station, Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to recent searches
        preferencesManager.addRecentSearch(currentStation, arrivalStation);

        Intent intent = new Intent(this, showActivity.class);
        intent.putExtra("start", currentStation);
        intent.putExtra("end", arrivalStation);
        startActivity(intent);
    }


    public void map(View view) {
        String selectedStation = startSpinner.getText().toString();
        if(selectedStation.isEmpty()) {
            Toast.makeText(this, "Please choose a station", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake).duration(700).repeat(3).playOn(startSpinner);
            return;
        }
        Intent a = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + selectedStation + " metro station egypt"));
        startActivity(a);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void Switch(View view) {
        if (startSpinner == null || endSpinner == null) {
            Toast.makeText(this, "Error: Spinners not initialized", Toast.LENGTH_SHORT).show();
            return;
        }

        CharSequence startText = startSpinner.getText();
        CharSequence endText = endSpinner.getText();

        if (TextUtils.isEmpty(startText) || TextUtils.isEmpty(endText)) {
            Toast.makeText(this, R.string.please_select_stations, Toast.LENGTH_SHORT).show();
            return;
        }

        // Swap the stations
        startSpinner.setText(endText);
        endSpinner.setText(startText);

        startSpinner.clearFocus();
        endSpinner.clearFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLanguageButtonText();
    }

    @Override
    public void onSuccess(@NonNull ArrayList<Location> arrayList) {
        // Location callback - not used in MainActivity
    }

    @Override
    public void onFailure(@NonNull AirLocation.LocationFailedEnum locationFailedEnum) {
        // Location failure callback - not used in MainActivity
    }
}