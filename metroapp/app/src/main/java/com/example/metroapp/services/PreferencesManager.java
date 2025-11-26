package com.example.metroapp.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Service for managing favorite routes and search history
 */
public class PreferencesManager {
    private static final String PREFS_NAME = "MetroAppPrefs";
    private static final String KEY_FAVORITES = "favorite_routes";
    private static final String KEY_RECENT_SEARCHES = "recent_searches";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_THEME = "theme";

    private static final int MAX_RECENT_SEARCHES = 10;
    private static final int MAX_FAVORITES = 20;

    private final SharedPreferences prefs;
    private final Gson gson;

    public PreferencesManager(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    // Language preferences
    public String getLanguage() {
        return prefs.getString(KEY_LANGUAGE, "en");
    }

    public void setLanguage(String languageCode) {
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply();
    }

    // Theme preferences
    public boolean isDarkMode() {
        return prefs.getBoolean(KEY_THEME, false);
    }

    public void setDarkMode(boolean enabled) {
        prefs.edit().putBoolean(KEY_THEME, enabled).apply();
    }

    // Favorite routes
    public List<SavedRoute> getFavorites() {
        String json = prefs.getString(KEY_FAVORITES, "[]");
        Type type = new TypeToken<List<SavedRoute>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public void addFavorite(String startStation, String endStation, String nickname) {
        List<SavedRoute> favorites = getFavorites();

        // Check if already exists
        for (SavedRoute route : favorites) {
            if (route.getStartStation().equals(startStation) &&
                route.getEndStation().equals(endStation)) {
                return; // Already favorited
            }
        }

        // Add new favorite
        SavedRoute newRoute = new SavedRoute(startStation, endStation, nickname);
        favorites.add(0, newRoute); // Add to beginning

        // Limit size
        if (favorites.size() > MAX_FAVORITES) {
            favorites = favorites.subList(0, MAX_FAVORITES);
        }

        saveFavorites(favorites);
    }

    public void removeFavorite(SavedRoute route) {
        List<SavedRoute> favorites = getFavorites();
        favorites.remove(route);
        saveFavorites(favorites);
    }

    private void saveFavorites(List<SavedRoute> favorites) {
        String json = gson.toJson(favorites);
        prefs.edit().putString(KEY_FAVORITES, json).apply();
    }

    public boolean isFavorite(String startStation, String endStation) {
        List<SavedRoute> favorites = getFavorites();
        for (SavedRoute route : favorites) {
            if (route.getStartStation().equals(startStation) &&
                route.getEndStation().equals(endStation)) {
                return true;
            }
        }
        return false;
    }

    // Recent searches
    public List<SavedRoute> getRecentSearches() {
        String json = prefs.getString(KEY_RECENT_SEARCHES, "[]");
        Type type = new TypeToken<List<SavedRoute>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public void addRecentSearch(String startStation, String endStation) {
        List<SavedRoute> recent = getRecentSearches();

        // Remove if already exists (to move to top)
        SavedRoute existing = null;
        for (SavedRoute route : recent) {
            if (route.getStartStation().equals(startStation) &&
                route.getEndStation().equals(endStation)) {
                existing = route;
                break;
            }
        }
        if (existing != null) {
            recent.remove(existing);
        }

        // Add to beginning
        recent.add(0, new SavedRoute(startStation, endStation, ""));

        // Limit size
        if (recent.size() > MAX_RECENT_SEARCHES) {
            recent = recent.subList(0, MAX_RECENT_SEARCHES);
        }

        saveRecentSearches(recent);
    }

    public void clearRecentSearches() {
        prefs.edit().putString(KEY_RECENT_SEARCHES, "[]").apply();
    }

    private void saveRecentSearches(List<SavedRoute> searches) {
        String json = gson.toJson(searches);
        prefs.edit().putString(KEY_RECENT_SEARCHES, json).apply();
    }

    /**
     * Saved route model
     */
    public static class SavedRoute {
        private String startStation;
        private String endStation;
        private String nickname;
        private long timestamp;

        public SavedRoute(String startStation, String endStation, String nickname) {
            this.startStation = startStation;
            this.endStation = endStation;
            this.nickname = nickname != null ? nickname : "";
            this.timestamp = System.currentTimeMillis();
        }

        public String getStartStation() {
            return startStation;
        }

        public String getEndStation() {
            return endStation;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public String getDisplayName() {
            if (nickname != null && !nickname.isEmpty()) {
                return nickname;
            }
            return startStation + " → " + endStation;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof SavedRoute)) return false;
            SavedRoute other = (SavedRoute) obj;
            return this.startStation.equals(other.startStation) &&
                   this.endStation.equals(other.endStation);
        }

        @Override
        public int hashCode() {
            return (startStation + endStation).hashCode();
        }
    }
}

