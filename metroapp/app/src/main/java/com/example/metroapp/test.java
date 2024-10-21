//package com.example.metroapp;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.res.Configuration;
//import android.content.res.Resources;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.daimajia.androidanimations.library.Techniques;
//import com.daimajia.androidanimations.library.YoYo;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Locale;
//import java.util.Set;
//
//import mumayank.com.airlocationlibrary.AirLocation;
//
//public class MainActivity extends AppCompatActivity implements AirLocation.Callback {
//    int lit, lit1;
//    boolean check=true;
//    Location loc1 = new Location(""), loc2 = new Location("");
//    AirLocation airLocation;
//    SharedPreferences pref;
//    private static final List<String> line1 = Arrays.asList("Helwan", "Ain Helwan", "Helwan University", "Wadi Hof", "Hadayek Helwan", "El-Maasara", "Tora El-Asmant", "Kozzika", "Tora El-Balad", "Sakanat El-Maadi", "Maadi", "Hadayek El-Maadi", "Dar El-Salam", "El-Zahraa'", "Mar Girgis", "El-Malek El-Saleh", "Al-Sayeda Zeinab", "Saad Zaghloul", "Sadat", "Nasser", "Ahmed Orabi", "Shohadaa", "Ghamra", "El-Demerdash", "Manshiet El-sadr", "Kobri El-Qobba", "Hammamat El-Qobba", "Saraya El-Qobba", "Hadayeq El-Zaitoun", "Helmyet elzayton", "El-Matarreyya", "Ain Shams", "Ezbet Nakhl", "El-Marg", "New El-Marg");
//    private static final List<String> line2 = Arrays.asList("El-Mounib", "Sakiat Mekky", "Omm El-Misryeen", "Giza", "Faisal", "Cairo University", "El Bohoth", "Dokki", "Opera", "Sadat", "Mohamed Naguib", "Attaba", "Shohadaa", "Massara", "Rod El-Farag", "St. Teresa", "Khalafawy", "Mezallat", "Kolleyyet El-Zeraa", "Shubra El-Kheima");
//    private static final List<String> line3 = Arrays.asList("Adly Mansour", "El Haykesteb", "Omar Ebm El-Khatab", "Quba", "Hesham Barakat", "El Nozha", "El Shams Club", "Alf Maskan", "Heliopolis", "Haroun", "Al-Ahram", "Koleyet El-Banat", "Cairo Stadium", "Fair Zone", "Abbassia", "Abdou Pasha", "El-Geish", "Bab El Shaaria", "Attaba", "Nasser", "Maspero", "safaa hegazy", "Kit Kat", "Sudan", "Imbaba", "El Bohy", "El Qawmia", "Ring Road", "Rod El-Farag Corridor");
//    private static final List<String> line1_ar = Arrays.asList("حلوان", "عين حلوان", "جامعة حلوان", "وادي حوف", "حدائق حلوان", "المعصرة", "طرة الأسمنت", "كوتسيكا", "طرة البلد", "ثكنات المعادي", "المعادي", "حدائق المعادي", "دار السلام", "الزهراء", "مار جرجس", "الملك الصالح", "السيدة زينب", "سعد زغلول", "السادات", "جمال عبد الناصر", "أحمد عرابي", "الشهداء", "غمرة", "الدمرداش", "منشية الصدر", "كوبري القبة", "حمامات القبة", "سراي القبة", "حدائق الزيتون", "حلمية الزيتون", "المطرية", "عين شمس", "عزبة النخل", "المرج", "المرج الجديدة");
//    private static final List<String> line2_ar = Arrays.asList("المنيب", "ساقية مكي", "أم المصريين", "الجيزة", "فيصل", "جامعة القاهرة", "البحوث", "الدقي", "الأوبرا", "السادات", "محمد نجيب", "العتبة", "الشهداء", "مسرة", "روض الفرج", "سانت تريزا", "الخلفاوي", "المظلات", "كلية الزراعة", "شبرا الخيمة");
//    private static final List<String> line3_ar = Arrays.asList("عدلي منصور", "الهايكستب", "عمر بن الخطاب", "قباء", "هشام بركات", "النزهة", "نادي الشمس", "ألف مسكن", "هليوبوليس", "هارون", "الأهرام", "كلية البنات", "الاستاد", "أرض المعارض", "العباسية", "عبده باشا", "الجيش", "باب الشعرية", "العتبة", "جمال عبد الناصر", "ماسبيرو", "صفاء حجازي", "كيت كات", "السودان", "إمبابة", "البوهي", "القومية", "الطريق الدائري", "محور روض الفرج");
//    List<Station> locations = Arrays.asList(
//            // Line 1
//            new Station("Helwan", 29.8414, 31.3007),
//            new Station("Ain Helwan", 29.8551, 31.3094),
//            new Station("Helwan University", 29.8659, 31.3182),
//            new Station("Wadi Hof", 29.8862, 31.3218),
//            new Station("Hadayek Helwan", 29.8971, 31.3202),
//            new Station("El-Maasara", 29.9067, 31.3167),
//            new Station("Tora El-Asmant", 29.9314, 31.3042),
//            new Station("Kozzika", 29.9471, 31.2975),
//            new Station("Tora El-Balad", 29.9613, 31.2909),
//            new Station("Sakanat El-Maadi", 29.9714, 31.2877),
//            new Station("Maadi", 29.9805, 31.2826),
//            new Station("Hadayek El-Maadi", 29.9921, 31.2810),
//            new Station("Dar El-Salam", 30.0114, 31.2766),
//            new Station("El-Zahraa'", 30.0311, 31.2682),
//            new Station("Mar Girgis", 30.0427, 31.2438),
//            new Station("El-Malek El-Saleh", 30.0462, 31.2394),
//            new Station("Al-Sayeda Zeinab", 30.0493, 31.2339),
//            new Station("Saad Zaghloul", 30.0476, 31.2283),
//            new Station("Sadat", 30.0445, 31.2333),
//            new Station("Nasser", 30.0537, 31.2386),
//            new Station("Ahmed Orabi", 30.0564, 31.2409),
//            new Station("Shohadaa", 30.0604, 31.2460),
//            new Station("Ghamra", 30.0691, 31.2573),
//            new Station("El-Demerdash", 30.0785, 31.2647),
//            new Station("Manshiet El-Sadr", 30.0885, 31.2705),
//            new Station("Kobri El-Qobba", 30.0991, 31.2828),
//            new Station("Hammamat El-Qobba", 30.1057, 31.2902),
//            new Station("Saraya El-Qobba", 30.1095, 31.2949),
//            new Station("Hadayeq El-Zaitoun", 30.1143, 31.3032),
//            new Station("Helmyet El-Zaitoun", 30.1197, 31.3097),
//            new Station("El-Matarreyya", 30.1242, 31.3171),
//            new Station("Ain Shams", 30.1301, 31.3252),
//            new Station("Ezbet Nakhl", 30.1412, 31.3312),
//            new Station("El-Marg", 30.1534, 31.3378),
//            new Station("New El-Marg", 30.1664, 31.3425),
//
//            // Line 2
//            new Station("El-Mounib", 29.9963, 31.2138),
//            new Station("Sakiat Mekky", 30.0047, 31.2117),
//            new Station("Omm El-Misryeen", 30.0144, 31.2085),
//            new Station("Giza", 30.0219, 31.2063),
//            new Station("Faisal", 30.0276, 31.2013),
//            new Station("Cairo University", 30.0364, 31.2001),
//            new Station("El Bohoth", 30.0445, 31.1978),
//            new Station("Dokki", 30.0479, 31.2115),
//            new Station("Opera", 30.0447, 31.2239),
//            new Station("Sadat", 30.0445, 31.2333),
//            new Station("Mohamed Naguib", 30.0482, 31.2431),
//            new Station("Attaba", 30.0521, 31.2467),
//            new Station("Shohadaa", 30.0604, 31.2460),
//            new Station("Massara", 30.0690, 31.2480),
//            new Station("Rod El-Farag", 30.0767, 31.2485),
//            new Station("St. Teresa", 30.0865, 31.2464),
//            new Station("Khalafawy", 30.0935, 31.2427),
//            new Station("Mezallat", 30.1041, 31.2371),
//            new Station("Kolleyyet El-Zeraa", 30.1121, 31.2307),
//            new Station("Shubra El-Kheima", 30.1207, 31.2270),
//
//            // Line 3
//            new Station("Adly Mansour", 30.1343, 31.4243),
//            new Station("El Haykesteb", 30.1235, 31.4008),
//            new Station("Omar Ebm El-Khatab", 30.1181, 31.3888),
//            new Station("Quba", 30.1126, 31.3763),
//            new Station("Hesham Barakat", 30.1070, 31.3642),
//            new Station("El Nozha", 30.1015, 31.3535),
//            new Station("El Shams Club", 30.0939, 31.3426),
//            new Station("Alf Maskan", 30.0870, 31.3353),
//            new Station("Heliopolis", 30.0828, 31.3298),
//            new Station("Haroun", 30.0742, 31.3183),
//            new Station("Al-Ahram", 30.0693, 31.3115),
//            new Station("Koleyet El-Banat", 30.0635, 31.3026),
//            new Station("Cairo Stadium", 30.0567, 31.2927),
//            new Station("Fair Zone", 30.0486, 31.2807),
//            new Station("Abbassia", 30.0456, 31.2730),
//            new Station("Abdou Pasha", 30.0427, 31.2666),
//            new Station("El-Geish", 30.0408, 31.2608),
//            new Station("Bab El Shaaria", 30.0440, 31.2534),
//            new Station("Attaba", 30.0521, 31.2467),
//            new Station("Nasser", 30.0537, 31.2386),
//            new Station("Maspero", 30.0607, 31.2261),
//            new Station("Safaa Hegazy", 30.0647, 31.2177),
//            new Station("Kit Kat", 30.0705, 31.2133),
//            new Station("Sudan", 30.0754, 31.2069),
//            new Station("Imbaba", 30.0851, 31.1967),
//            new Station("El Bohy", 30.0909, 31.1912),
//            new Station("El Qawmia", 30.0961, 31.1859),
//            new Station("Ring Road", 30.1014, 31.1807),
//            new Station("Rod El Farag Corridor", 30.1067, 31.1757)
//    );
//    List<Station> locations1 = Arrays.asList(
//            // Line 1
//            new Station("Helwan", 29.8414, 31.3007),
//            new Station("Ain Helwan", 29.8551, 31.3094),
//            new Station("Helwan University", 29.8659, 31.3182),
//            new Station("Wadi Hof", 29.8862, 31.3218),
//            new Station("Hadayek Helwan", 29.8971, 31.3202),
//            new Station("El-Maasara", 29.9067, 31.3167),
//            new Station("Tora El-Asmant", 29.9314, 31.3042),
//            new Station("Kozzika", 29.9471, 31.2975),
//            new Station("Tora El-Balad", 29.9613, 31.2909),
//            new Station("Sakanat El-Maadi", 29.9714, 31.2877),
//            new Station("Maadi", 29.9805, 31.2826),
//            new Station("Hadayek El-Maadi", 29.9921, 31.2810),
//            new Station("Dar El-Salam", 30.0114, 31.2766),
//            new Station("El-Zahraa'", 30.0311, 31.2682),
//            new Station("Mar Girgis", 30.0427, 31.2438),
//            new Station("El-Malek El-Saleh", 30.0462, 31.2394),
//            new Station("Al-Sayeda Zeinab", 30.0493, 31.2339),
//            new Station("Saad Zaghloul", 30.0476, 31.2283),
//            new Station("Sadat", 30.0445, 31.2333),
//            new Station("Nasser", 30.0537, 31.2386),
//            new Station("Ahmed Orabi", 30.0564, 31.2409),
//            new Station("Shohadaa", 30.0604, 31.2460),
//            new Station("Ghamra", 30.0691, 31.2573),
//            new Station("El-Demerdash", 30.0785, 31.2647),
//            new Station("Manshiet El-Sadr", 30.0885, 31.2705),
//            new Station("Kobri El-Qobba", 30.0991, 31.2828),
//            new Station("Hammamat El-Qobba", 30.1057, 31.2902),
//            new Station("Saraya El-Qobba", 30.1095, 31.2949),
//            new Station("Hadayeq El-Zaitoun", 30.1143, 31.3032),
//            new Station("Helmyet El-Zaitoun", 30.1197, 31.3097),
//            new Station("El-Matarreyya", 30.1242, 31.3171),
//            new Station("Ain Shams", 30.1301, 31.3252),
//            new Station("Ezbet Nakhl", 30.1412, 31.3312),
//            new Station("El-Marg", 30.1534, 31.3378),
//            new Station("New El-Marg", 30.1664, 31.3425)
//    );
//    List<Station> locations2 = Arrays.asList(
//            // Line 2
//            new Station("El-Mounib", 29.9963, 31.2138),
//            new Station("Sakiat Mekky", 30.0047, 31.2117),
//            new Station("Omm El-Misryeen", 30.0144, 31.2085),
//            new Station("Giza", 30.0219, 31.2063),
//            new Station("Faisal", 30.0276, 31.2013),
//            new Station("Cairo University", 30.0364, 31.2001),
//            new Station("El Bohoth", 30.0445, 31.1978),
//            new Station("Dokki", 30.0479, 31.2115),
//            new Station("Opera", 30.0447, 31.2239),
//            new Station("Sadat", 30.0445, 31.2333),
//            new Station("Mohamed Naguib", 30.0482, 31.2431),
//            new Station("Attaba", 30.0521, 31.2467),
//            new Station("Shohadaa", 30.0604, 31.2460),
//            new Station("Massara", 30.0690, 31.2480),
//            new Station("Rod El-Farag", 30.0767, 31.2485),
//            new Station("St. Teresa", 30.0865, 31.2464),
//            new Station("Khalafawy", 30.0935, 31.2427),
//            new Station("Mezallat", 30.1041, 31.2371),
//            new Station("Kolleyyet El-Zeraa", 30.1121, 31.2307),
//            new Station("Shubra El-Kheima", 30.1207, 31.2270)
//
//    );
//    List<Station> locations3 = Arrays.asList(
//            new Station("Adly Mansour", 30.1343, 31.4243),
//            new Station("El Haykesteb", 30.1235, 31.4008),
//            new Station("Omar Ebm El-Khatab", 30.1181, 31.3888),
//            new Station("Quba", 30.1126, 31.3763),
//            new Station("Hesham Barakat", 30.1070, 31.3642),
//            new Station("El Nozha", 30.1015, 31.3535),
//            new Station("El Shams Club", 30.0939, 31.3426),
//            new Station("Alf Maskan", 30.0870, 31.3353),
//            new Station("Heliopolis", 30.0828, 31.3298),
//            new Station("Haroun", 30.0742, 31.3183),
//            new Station("Al-Ahram", 30.0693, 31.3115),
//            new Station("Koleyet El-Banat", 30.0635, 31.3026),
//            new Station("Cairo Stadium", 30.0567, 31.2927),
//            new Station("Fair Zone", 30.0486, 31.2807),
//            new Station("Abbassia", 30.0456, 31.2730),
//            new Station("Abdou Pasha", 30.0427, 31.2666),
//            new Station("El-Geish", 30.0408, 31.2608),
//            new Station("Bab El Shaaria", 30.0440, 31.2534),
//            new Station("Attaba", 30.0521, 31.2467),
//            new Station("Nasser", 30.0537, 31.2386),
//            new Station("Maspero", 30.0607, 31.2261),
//            new Station("Safaa Hegazy", 30.0647, 31.2177),
//            new Station("Kit Kat", 30.0705, 31.2133),
//            new Station("Sudan", 30.0754, 31.2069),
//            new Station("Imbaba", 30.0851, 31.1967),
//            new Station("El Bohy", 30.0909, 31.1912),
//            new Station("El Qawmia", 30.0961, 31.1859),
//            new Station("Ring Road", 30.1014, 31.1807),
//            new Station("Rod El Farag Corridor", 30.1067, 31.1757)
//
//    );
//
//
//    List<String> stations = new ArrayList<>();
//    EditText placeText;
//    String currentstation;
//    String arrivalstation;
//    List<String> summaryroute=new ArrayList<>();
//    TextView summaryText, nssText, nasText,distText;
//    Intent a = new Intent(String.valueOf(this));
//    private Spinner startSpinner, endSpinner;
//    private Button languageToggleButton;
//    private boolean isEnglish;
//    private SharedPreferences prefs;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        prefs = getSharedPreferences("LanguagePrefs", MODE_PRIVATE);
//        isEnglish = prefs.getBoolean("isEnglish", true);
//        setAppLanguage(isEnglish ? "en" : "ar");
//
//        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        placeText = findViewById(R.id.placeText);
//        startSpinner = findViewById(R.id.startspinner);
//        endSpinner = findViewById(R.id.endspinner);
//        summaryText = findViewById(R.id.summaryText);
//        nssText=findViewById(R.id.nssText);
//        nasText = findViewById(R.id.nasText);
//        distText=findViewById(R.id.distText);
//
//        languageToggleButton = findViewById(R.id.languageToggleButton);
//        updateLanguageButtonText();
//        languageToggleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toggleLanguage();
//            }
//        });
//
//        stations.addAll(line1);
//        stations.addAll(line2);
//        stations.addAll(line3);
//        Set<String> uniqueStations = new HashSet<>(stations);
//        stations = new ArrayList<>(uniqueStations);
//        Collections.sort(stations);
//        updateSpinners();
//
//        pref = getSharedPreferences("summar", MODE_PRIVATE);
//        String summ=summaryroute.toString();
//        summ = pref.getString("summary", "");
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
//
//    private void updateSpinners() {
//        List<String> stations = new ArrayList<>();
//        if (isArabic()) {
//            stations.addAll(line1_ar);
//            stations.addAll(line2_ar);
//            stations.addAll(line3_ar);
//        } else {
//            stations.addAll(line1);
//            stations.addAll(line2);
//            stations.addAll(line3);
//        }
//        Set<String> uniqueStations = new HashSet<>(stations);
//        stations = new ArrayList<>(uniqueStations);
//        Collections.sort(stations);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stations);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        startSpinner.setAdapter(adapter);
//        endSpinner.setAdapter(adapter);
//
//        startSpinner.setPrompt(getString(R.string.enter_current_station));
//        endSpinner.setPrompt(getString(R.string.enter_arrival_station));
//    }
//
//    private boolean isArabic() {
//        return getResources().getConfiguration().locale.getLanguage().equals("ar");
//    }
//
//    public void calc(View view) {
//        if (startSpinner.getSelectedItem() == null || endSpinner.getSelectedItem() == null) {
//            Toast.makeText(this, R.string.please_select_stations, Toast.LENGTH_SHORT).show();
//            return;
//        }
//        currentstation = startSpinner.getSelectedItem().toString();
//        arrivalstation = endSpinner.getSelectedItem().toString();
//        go();
//    }
//
//    private void go() {
//        summaryText.setText("");
//        if (currentstation.equalsIgnoreCase(arrivalstation)) {
//            YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(startSpinner);
//            YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(endSpinner);
//            return;
//        }
//        a = new Intent(this, showActivity.class);
//        a.putExtra("start", currentstation);
//        a.putExtra("end", arrivalstation);
//        startActivityForResult(a, 1000);
//    }
//
//    public void map(View view) {
//        if(startSpinner.getSelectedItem()==null)
//        {
//            Toast.makeText(this, "please choose station", Toast.LENGTH_SHORT).show();
//            YoYo.with(Techniques.Shake).duration(700).repeat(3).playOn(startSpinner);
//            return;
//        }
//        Intent a=new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+startSpinner.getSelectedItem()+" metro"+" station"+" egypt"));
//        startActivity(a);
//
//    }
//
//
//
//    public void near(View view) {
//        airLocation = new AirLocation(this, this, true, 0, "");
//        airLocation.start();
//
//        String place=placeText.getText().toString();
//        Geocoder geocoder=new Geocoder(this);
//        try {
//            List<Address> addressList = geocoder.getFromLocationName(place, 1);
//            if(addressList.isEmpty()||place.length()==0)
//            {
//                Toast.makeText(this, "enter correct place", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            Location location=new Location(""),location2=new Location("");
//            location.setLatitude(addressList.get(0).getLatitude());
//            location.setLongitude(addressList.get(0).getLongitude());
//            double min=Double.MAX_VALUE;
//            for ( int i=0,j=1;i<locations1.size();i++) {
//                location2.setLatitude(locations1.get(i).getLatitude());
//                location2.setLongitude(locations1.get(i).getLongitude());
//                float v = location.distanceTo(location2);
//                if(v<min) {
//                    min = v;
//                    lit1=i;
//                }
//            }
//           // Toast.makeText(this, "the nearest station is"+locations.get(lit1).getName().toString(), Toast.LENGTH_LONG).show();
//            nasText.setText("the nearest station to line 1 is "+locations1.get(lit1).getName().toString());
//            double min2=Double.MAX_VALUE;
//            for ( int i=0,j=1;i<locations2.size();i++) {
//                location2.setLatitude(locations2.get(i).getLatitude());
//                location2.setLongitude(locations2.get(i).getLongitude());
//                float v = location.distanceTo(location2);
//                if(v<min2) {
//                    min2 = v;
//                    lit1=i;
//                }
//            }
//            // Toast.makeText(this, "the nearest station is"+locations.get(lit1).getName().toString(), Toast.LENGTH_LONG).show();
//            nasText.append("\n the nearest station to line 2 is "+locations2.get(lit1).getName().toString());
//            double min3=Double.MAX_VALUE;
//            for ( int i=0,j=1;i<locations3.size();i++) {
//                location2.setLatitude(locations3.get(i).getLatitude());
//                location2.setLongitude(locations3.get(i).getLongitude());
//                float v = location.distanceTo(location2);
//                if(v<min3) {
//                    min3 = v;
//                    lit1=i;
//                }
//            }
//            // Toast.makeText(this, "the nearest station is"+locations.get(lit1).getName().toString(), Toast.LENGTH_LONG).show();
//            nasText.append("\n the nearest station to line 3 is "+locations3.get(lit1).getName().toString());
//
//
//        } catch (IOException e) {
//            Toast.makeText(this, "no internet connection", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==1000)
//        {
//            if(data!=null)
//            {
//                summaryroute=data.getStringArrayListExtra("route");
//            }
//
//        }
//        try {
//            airLocation.onActivityResult(requestCode, resultCode, data);
//        } catch (Exception e) {
//            return;
//        }
//    }
//
//
//
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//
//    @Override
//    public void onFailure(@NonNull AirLocation.LocationFailedEnum locationFailedEnum) {
//        Toast.makeText(this, "sorry connectionnn error", Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void onSuccess(@NonNull ArrayList<Location> arrayList) {
//        double latitude = arrayList.get(0).getLatitude();
//        double longitude = arrayList.get(0).getLongitude();
//        //Toast.makeText(this, latitude+" -"+longitude, Toast.LENGTH_SHORT).show();
//        loc1.setLatitude(latitude);
//        loc1.setLongitude(longitude);
//        double min=1000000000000.000;
//        for ( int i=0,j=1;i<locations.size();i++) {
//            loc2.setLatitude(locations.get(i).getLatitude());
//            loc2.setLongitude(locations.get(i).getLongitude());
//            float v = loc1.distanceTo(loc2);
//            if(v<min) {
//                min = v;
//                lit=i;
//            }
//        }
//
//        if(!check)
//        {
//            latitude = arrayList.get(0).getLatitude();
//            longitude = arrayList.get(0).getLongitude();
//            //Toast.makeText(this, latitude+" -"+longitude, Toast.LENGTH_SHORT).show();
//            loc1.setLatitude(latitude);
//            loc1.setLongitude(longitude);
//            min=1000000000000.000;
//            for ( int i=0,j=1;i<locations.size();i++) {
//                loc2.setLatitude(locations.get(i).getLatitude());
//                loc2.setLongitude(locations.get(i).getLongitude());
//                float v = loc1.distanceTo(loc2);
//                if(v<min) {
//                    min = v;
//                    lit=i;
//                }
//            }
//
//            distText.setText("the nearest station is " + locations.get(lit).getName().toString());
//            int i = summaryroute.indexOf(locations.get(lit).getName().toString())+1;
//            if(summaryroute.isEmpty()) {
//                distText.setText("choose your arrival first");
//                return;
//            }
//            int lenght = (summaryroute.size()-1)-i ;
//            distText.setText("remaining stations = "+lenght);
//            distText.append("\n remaining time = "+lenght*2+" min");
//
//        }
//
//        //Toast.makeText(this, "the nearest station is"+locations.get(lit).getName().toString(), Toast.LENGTH_SHORT).show();
//        if(check) {
//            nssText.setText("the nearest station is " + locations.get(lit).getName().toString());
//            check=false;
//        }
//
////        if(stations.contains(locations.get(lit).getName().toString()))
////        {
////            startspinner.setSelection(stations.indexOf(locations.get(lit).getName()));
////        }
////        else Toast.makeText(this, "not found station", Toast.LENGTH_SHORT).show();
//    }
//
//    public void distnace(View view) {
//        AirLocation airLocationd = new AirLocation(this, this, true, 0, "");
//        airLocationd.start();
//
//    }
//
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        updateSpinners();
//    }
//
//    public void Switch(View view) {
//        int startPosition = startSpinner.getSelectedItemPosition();
//        int endPosition = endSpinner.getSelectedItemPosition();
//
//        // Check if both spinners have valid selections
//        if (startPosition >= 0 && endPosition >= 0) {
//            // Swap the selections
//            startSpinner.setSelection(endPosition);
//            endSpinner.setSelection(startPosition);
//        } else {
//            // Handle the case where one or both spinners don't have a selection
//            Toast.makeText(this, R.string.please_select_stations, Toast.LENGTH_SHORT).show();
//        }
//        go();
//    }
//
//    private void toggleLanguage() {
//        isEnglish = !isEnglish;
//        prefs.edit().putBoolean("isEnglish", isEnglish).apply();
//        setAppLanguage(isEnglish ? "en" : "ar");
//        recreate();
//    }
//
//    private void setAppLanguage(String languageCode) {
//        Locale locale = new Locale(languageCode);
//        Locale.setDefault(locale);
//        Resources resources = getResources();
//        Configuration config = resources.getConfiguration();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            config.setLocale(locale);
//        }
//        resources.updateConfiguration(config, resources.getDisplayMetrics());
//    }
//
//    private void updateLanguageButtonText() {
//        if (isEnglish) {
//            languageToggleButton.setText(getString(R.string.language_toggle_ar));
//        } else {
//            languageToggleButton.setText(getString(R.string.language_toggle_en));
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        updateLanguageButtonText();
//    }
//}