package com.example.metroapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mumayank.com.airlocationlibrary.AirLocation;

public class showActivity extends AppCompatActivity implements AirLocation.Callback {
    List<String> route;
    Location loc1 = new Location(""), loc2 = new Location("");
    int lit;
    AirLocation airLocation;
    private TextView routeDetailsText;
    private static final List<String> line1 = Arrays.asList("Helwan", "Ain Helwan", "Helwan University", "Wadi Hof", "Hadayek Helwan", "El Maasara", "Tora El Asmant", "Kozzika", "Tora El Balad", "Sakanat El Maadi", "Maadi", "Hadayek El Maadi", "Dar El Salam", "El Zahraa'", "Mar Girgis", "El Malek El Saleh", "Al-Sayeda Zeinab", "Saad Zaghloul", "Sadat", "Nasser", "Ahmed Orabi", "Shohadaa", "Ghamra", "El Demerdash", "Manshiet El sadr", "Kobri El Qobba", "Hammamat El Qobba", "Saraya El Qobba", "Hadayeq El Zaitoun","Helmyet elzayton", "El Matarreyya", "Ain Shams", "Ezbet Nakhl", "El Marg", "New El Marg");
    private static final List<String> line2 = Arrays.asList("El Mounib", "Sakiat Mekky", "Omm El Misryeen", "Giza", "Faisal", "Cairo University", "El Bohoth", "Dokki", "Opera", "Sadat", "Mohamed Naguib", "Attaba","Shohadaa", "Massara", "Rod El Farag", "St. Teresa", "Khalafawy", "Mezallat", "Kolleyyet El Zeraa", "Shubra El Kheima");
    private static final List<String> line3 = Arrays.asList("Adly Mansour","El Haykesteb","Omar Ebm El Khatab","Quba","Hesham Barakat","El Nozha","El Shams Club","Alf Maskan", "Heliopolis", "Haroun", "Al-Ahram", "Koleyet El Banat", "Cairo Stadium", "Fair Zone", "Abbassia", "Abdou Pasha", "El Geish", "Bab El Shaaria", "Attaba", "Nasser", "Maspero", "safaa hegazy", "Kit Kat", "Sudan", "Imbaba","El Bohy","El Qawmia","Ring Road","Rod El Farag Corridor");
    public static final Set<String> interchangeStations = new HashSet<>(Arrays.asList("Sadat", "Attaba", "Shohadaa", "Nasser"));
    List<Station> locations  = Arrays.asList(
            new Station("Helwan", 29.848985830828955, 31.334226452670784),
            new Station("Ain Helwan", 29.86261031567134, 31.32486845721002),
            new Station("Helwan University", 29.869443794450458, 31.320056055691822),
            new Station("Wadi Hof", 29.879081048289322, 31.313572989167383),
            new Station("Hadayek Helwan", 29.897140855908066, 31.30396205557997),
            new Station("El Maasara", 29.90608081356438, 31.299508418575552),
            new Station("Tora El Asmant", 29.925960543106633, 31.28753729101867),
            new Station("Kozzika", 29.936253846994905, 31.281813134266983),
            new Station("Tora El Balad", 29.94677651550211, 31.27297281172739),
            new Station("Sakanat El Maadi", 29.95330739825872, 31.262947411995547),
            new Station("Maadi", 29.960294167159432, 31.257640201091924),
            new Station("Hadayek El Maadi", 29.970137801844807, 31.250590522067426),
            new Station("Dar El Salam", 29.982078499767873, 31.242166934291753),
            new Station("El Zahraa", 29.99547471287979, 31.231167589610198),
            new Station("Mar Girgis", 30.0061001398057, 31.229611836562803),
            new Station("El Malek El Saleh", 30.017689700519316, 31.23120165781196),
            new Station("Al-Sayeda Zeinab", 30.029273578013974, 31.235418546626892),
            new Station("Saad Zaghloul", 30.037019393330276, 31.238356079150318),
            new Station("Sadat", 30.044133489304492, 31.23440633662588),
            new Station("Nasser", 30.05350546363506, 31.23873139342649),
            new Station("Orabi", 30.056688464050122, 31.242052947390007),
            new Station("Al-Shohadaa", 30.061061823104865, 31.246033662768014),
            new Station("Ghamra", 30.069018707671813, 31.264606226488922),
            new Station("El Demerdash", 30.07731603912765, 31.277791287465718),
            new Station("Manshiet El Sadr", 30.082004447642586, 31.287511612949583),
            new Station("Kobri El Qobba", 30.087196974797383, 31.29410409496931),
            new Station("Hammamat El Qobba", 30.09153318905702, 31.298857927383782),
            new Station("Saray El Qobba", 30.097766644470713, 31.304563094969808),
            new Station("Hadayeq El Zaitoun", 30.105991671394083, 31.310461837298014),
            new Station("Helmeyet El Zaitoun", 30.11333830215214, 31.313964694970696),
            new Station("El Matareyya", 30.121368436674942, 31.313708054821966),
            new Station("Ain Shams", 30.131090572449327, 31.31910726431132),
            new Station("Ezbet El Nakhl", 30.139429315185044, 31.324422194971998),
            new Station("El Marg", 30.15207714128871, 31.335683021718527),
            new Station("New El Marg", 30.163687417465443, 31.33836445250712),

            new Station("El Mounib", 29.981093777265393, 31.21231632203813),
            new Station("Sakiat Mekky", 29.995483255523187, 31.208643410316917),
            new Station("Giza", 30.010658240356626, 31.20707722486442),
            new Station("Faisal", 30.01736183205528, 31.20392927586734),
            new Station("Cairo University", 30.02601123462684, 31.201154249181485),
            new Station("El Bohuth", 30.035782894984603, 31.200160771733053),
            new Station("Dokki", 30.038434268395182, 31.212230138794776),
            new Station("Opera", 30.041941370144535, 31.22497492348373),
            new Station("Sadat", 30.044133489304492, 31.23440633662588),
            new Station("Mohamed Naguib", 30.045321746151014, 31.2441603444723),
            new Station("Attaba", 30.05234532014751, 31.246801227984676),
            new Station("Masaraa", 30.070884174110354, 31.2450973524905),
            new Station("Rod El Farag", 30.080589244675185, 31.24540237635897),
            new Station("St. Teresa", 30.087952258814255, 31.245475796965952),
            new Station("Khalafawy", 30.097884660571072, 31.245390531684933),
            new Station("Mezallat", 30.104175734837547, 31.245647593898806),
            new Station("Kolleyyet El Zeraa", 30.113682656234165, 31.24865801883198),
            new Station("Shubra El Kheima", 30.122437013783863, 31.244535607337426),
            new Station("Adly Mansour", 30.146460891056062, 31.42132009501648),
            new Station("El Haykestep", 30.14384675550377, 31.4046911598909),
            new Station("Omar Ibn El Khattab", 30.140374683852777, 31.394337389936844),
            new Station("Qobaa", 30.13481905601565, 31.383747990314497),
            new Station("Hesham Barakat", 30.13083182413351, 31.37293384310862),
            new Station("El Nozha", 30.12798718978646, 31.360166001286885),
            new Station("Nadi El Shams", 30.125482406939103, 31.348876784170976),
            new Station("Alf Maskan", 30.118998064870205, 31.340184811724036),
            new Station("Heliopolis Square", 30.108419533101188, 31.33830315431158), // Renamed
            new Station("Helmyet El Zaitoun", 30.11333830215214, 31.313964694970696), // Renamed
            new Station("Haroun", 30.101360769314265, 31.332969259154336),
            new Station("Al-Ahram", 30.09171267348972, 31.326312489483023),
            new Station("Koleyet El Banat", 30.084035190321604, 31.329014883953445),
            new Station("Stadium", 30.07290068192294, 31.317103060148366),
            new Station("Fair Zone", 30.07325713229277, 31.300981814209575),
            new Station("Abbassiya", 30.071983536625847, 31.28337426851981),
            new Station("Abdou Pasha", 30.06477439471832, 31.274743278100342),
            new Station("El Geish", 30.061748439319054, 31.26687659582453),
            new Station("Bab El Shaaria", 30.054134586563745, 31.25587055384179),
            new Station("Attaba", 30.05234532014751, 31.246801227984676),
            new Station("Nasser", 30.05350546363506, 31.23873139342649),
            new Station("Maspero", 30.055712204662488, 31.232108390232284),
            new Station("Zamalek", 30.05044510068713, 31.215278184967785),
            new Station("Kit Kat", 30.068424328662826, 31.217844748494457),
            new Station("Sudan", 30.06225547201399, 31.211313583891884),
            new Station("Imbaba", 30.078505207717655, 31.209091425673994),
            new Station("El Bohy", 30.08351128309893, 31.20183162197527),
            new Station("El Qawmia", 30.088705877981723, 31.195526072041598),
            new Station("Ring Road", 30.101327974627734, 31.1876844526973),
            new Station("Rod El Farag", 30.099594248067044, 31.17212883605873)
    );

    public static String direction = "";
    public static boolean enter = false;
    public static String trans = "";

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
        if (currentStation.equalsIgnoreCase(arrivalStation)) {

            return;
        }
        Map<String, List<String>> stationMap = new HashMap<>();
        stationMap.put("line1", line1);
        stationMap.put("line2", line2);
        stationMap.put("line3", line3);
        route = new ArrayList<>();

        boolean routeFound = false;

        // Check for route within a single line
        for (List<String> line : stationMap.values()) {
            if (line.contains(currentStation) && line.contains(arrivalStation)) {
                int indexs = line.indexOf(currentStation);
                int indexe = line.indexOf(arrivalStation);
                route = getSubRoute(currentStation, arrivalStation, line);
                if (indexs > indexe)
                    direction = line.get(0);
                else
                    direction = line.get(line.size() - 1);
                routeFound = true;
                break;
            }
        }
        if (!routeFound) {
            for (String interchange : interchangeStations) {
                if (found(currentStation, interchange, stationMap) && found(interchange, arrivalStation, stationMap)) {
                    List<String> firstPart = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        firstPart = getSubRoute(currentStation, interchange, stationMap.values().stream().filter(line -> line.contains(currentStation)).findFirst().orElse(null));
                    }
                    List<String> secondPart = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        secondPart = getSubRoute(interchange, arrivalStation, stationMap.values().stream().filter(line -> line.contains(arrivalStation)).findFirst().orElse(null));
                    }
                    if (firstPart != null && secondPart != null) {
                        route.addAll(firstPart);
                        secondPart.remove(0);  // Remove the interchange to avoid repetition
                        route.addAll(secondPart);
                        trans = interchange;
                        routeFound = true;
                        break;
                    }
                }
            }
        }

        if (route.isEmpty()) {
            Toast.makeText(this, "No route found", Toast.LENGTH_SHORT).show();
        } else {
            displayRouteDetails(route);
        }

        YoYo.with(Techniques.FadeIn).duration(1000).playOn(routeDetailsText);
    }

    private static boolean found(String start, String end, Map<String, List<String>> metroMap) {
        for (List<String> line : metroMap.values()) {
            if (line.contains(start) && line.contains(end)) {
                return true;
            }
        }
        return false;
    }

    private static List<String> getSubRoute(String start, String end, List<String> line) {
        if (line == null) return new ArrayList<>();
        int startIndex = line.indexOf(start);
        int endIndex = line.indexOf(end);
        List<String> subRoute = new ArrayList<>(line.subList(Math.min(startIndex, endIndex),
                Math.max(startIndex, endIndex) + 1));
        if (startIndex > endIndex) {
            Collections.reverse(subRoute);
            direction = line.get(0);
        } else if (!enter) {
            direction = line.get(line.size() - 1);
        }
        enter = true;
        return subRoute;
    }

    private void displayRouteDetails(List<String> route) {
        StringBuilder result = new StringBuilder();
        int stationCount = route.size() - 1;
        int time = stationCount * 2;
        int ticket = stationCount <= 8 ? 6 : stationCount <= 15 ? 8 : 12;

        result.append("Number of stations: ").append(stationCount).append("\n\n");
        result.append("Time: ").append(time / 60).append(" hours, ").append(time % 60).append(" minutes\n\n");
        result.append("Ticket price: ").append(ticket).append(" EGP\n\n");
        if (!trans.isEmpty()) {
            result.append("Transfer at: ").append(trans).append("\n\n");
        }
        result.append("Direction: ").append(direction).append("\n\n");
        result.append("Stations:\n").append(String.join(" -> ", route));

        routeDetailsText.setText(result.toString());
    }

    public void remainingDistance(View view) {
        airLocation = new AirLocation(this, this, true, 0, "");
        airLocation.start();
    }

    @Override
    public void onFailure(@NonNull AirLocation.LocationFailedEnum locationFailedEnum) {
        Toast.makeText(this, "conniction error", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onSuccess(@NonNull ArrayList<Location> arrayList) {
        double latitude = arrayList.get(0).getLatitude();
        double longitude = arrayList.get(0).getLongitude();
        loc1.setLatitude(latitude);
        loc1.setLongitude(longitude);
        double min=1000000000000.000;
        for ( int i=0;i<locations.size();i++) {
            loc2.setLatitude(locations.get(i).getLatitude());
            loc2.setLongitude(locations.get(i).getLongitude());
            float v = loc1.distanceTo(loc2);
            if(v<min) {
                min = v;
                lit=i;
            }
        }
        int in = route.indexOf(locations.get(lit).getName().toString())+1;
        int lenght = (route.size()-1)-in ;
        Toast.makeText(this, "remaining stations = "+lenght+"\n"+"remaining time = "+lenght*2+" mins", Toast.LENGTH_SHORT).show();


    }
}