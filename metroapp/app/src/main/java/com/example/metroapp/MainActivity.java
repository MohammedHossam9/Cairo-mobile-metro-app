package com.example.metroapp;

import static android.transition.TransitionManager.go;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import mumayank.com.airlocationlibrary.AirLocation;

public class MainActivity extends AppCompatActivity implements AirLocation.Callback {
    SharedPreferences pref;
    private static final List<String> line1 = Arrays.asList("Helwan", "Ain Helwan", "Helwan University", "Wadi Hof", "Hadayek Helwan", "El Maasara", "Tora El Asmant", "Kozzika", "Tora El Balad", "Sakanat El Maadi", "Maadi", "Hadayek El Maadi", "Dar El Salam", "El Zahraa'", "Mar Girgis", "El Malek El Saleh", "Al-Sayeda Zeinab", "Saad Zaghloul", "Sadat", "Nasser", "Ahmed Orabi", "Shohadaa", "Ghamra", "El Demerdash", "Manshiet El sadr", "Kobri El Qobba", "Hammamat El Qobba", "Saraya El Qobba", "Hadayeq El Zaitoun", "Helmyet elzayton", "El Matarreyya", "Ain Shams", "Ezbet Nakhl", "El Marg", "New El Marg");
    private static final List<String> line2 = Arrays.asList("El Mounib", "Sakiat Mekky", "Omm El Misryeen", "Giza", "Faisal", "Cairo University", "El Bohoth", "Dokki", "Opera", "Sadat", "Mohamed Naguib", "Attaba", "Shohadaa", "Massara", "Rod El Farag", "St. Teresa", "Khalafawy", "Mezallat", "Kolleyyet El Zeraa", "Shubra El Kheima");
    private static final List<String> line3 = Arrays.asList("Adly Mansour", "El Haykesteb", "Omar Ebm El Khatab", "Quba", "Hesham Barakat", "El Nozha", "El Shams Club", "Alf Maskan", "Heliopolis", "Haroun", "Al-Ahram", "Koleyet El Banat", "Cairo Stadium", "Fair Zone", "Abbassia", "Abdou Pasha", "El Geish", "Bab El Shaaria", "Attaba", "Nasser", "Maspero", "safaa hegazy", "Kit Kat", "Sudan", "Imbaba", "El Bohy", "El Qawmia", "Ring Road", "Rod El Farag Corridor");

    List<String> stations = new ArrayList<>();
    String currentstation ,arrivalstation;
    List<String> summaryroute=new ArrayList<>();
    private AutoCompleteTextView startSpinner, endSpinner;
    private Button languageToggleButton;
    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        prefs = getSharedPreferences("LanguagePrefs", MODE_PRIVATE);
        String currentLang = prefs.getString("language", "en");
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
        updateLanguageButtonText();
        languageToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLanguage();
            }
        });

        setupStations();
        setupAutoCompleteTextViews();

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
        stations.addAll(line1);
        stations.addAll(line2);
        stations.addAll(line3);
        Set<String> uniqueStations = new HashSet<>(stations);
        stations = new ArrayList<>(uniqueStations);
        Collections.sort(stations);
    }
    private void setupAutoCompleteTextViews() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, stations);
        startSpinner.setAdapter(adapter);
        endSpinner.setAdapter(adapter);

        startSpinner.setThreshold(1);
        endSpinner.setThreshold(1);
    }

    private void toggleLanguage() {
        String currentLang = prefs.getString("language", "en");
        String newLang = currentLang.equals("en") ? "ar" : "en";
        setLocale(newLang);
        prefs.edit().putString("language", newLang).apply();
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
        String currentLang = prefs.getString("language", "en");
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

        Intent intent = new Intent(this, showActivity.class);
        intent.putExtra("start", currentStation);
        intent.putExtra("end", arrivalStation);
        startActivity(intent);
    }

    private void go() {
        if (currentstation.equalsIgnoreCase(arrivalstation)) {
            YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(startSpinner);
            YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(endSpinner);
            return;
        }

        Intent intent = new Intent(this, showActivity.class);
        intent.putExtra("start", currentstation);
        intent.putExtra("end", arrivalstation);
        startActivityForResult(intent, 1000);
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
            Toast.makeText(this, "Please select both start and end stations", Toast.LENGTH_SHORT).show();
            return;
        }

        startSpinner.setText(endText);
        endSpinner.setText(startText);

        startSpinner.clearFocus();
        endSpinner.clearFocus();

        currentstation = startSpinner.getText().toString();
        arrivalstation = endSpinner.getText().toString();

        if (!stations.contains(currentstation) || !stations.contains(arrivalstation)) {
            Toast.makeText(this, "Invalid station selected", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            go();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void setAppLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        }
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLanguageButtonText();
    }


    @Override
    public void onSuccess(@NonNull ArrayList<Location> arrayList) {

    }

    @Override
    public void onFailure(@NonNull AirLocation.LocationFailedEnum locationFailedEnum) {

    }
}