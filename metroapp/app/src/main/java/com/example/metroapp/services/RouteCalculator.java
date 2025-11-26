package com.example.metroapp.services;

import com.example.metroapp.data.MetroLine;
import com.example.metroapp.data.RouteResult;
import com.example.metroapp.data.StationData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Service class for calculating routes between metro stations
 * Handles single-line and multi-line (transfer) routes
 */
public class RouteCalculator {

    /**
     * Calculate the optimal route between two stations
     */
    public static RouteResult calculateRoute(String startStation, String endStation) {
        RouteResult result = new RouteResult();

        if (startStation == null || endStation == null ||
            startStation.trim().isEmpty() || endStation.trim().isEmpty()) {
            return result;
        }

        if (startStation.equalsIgnoreCase(endStation)) {
            return result;
        }

        Map<String, List<String>> lineMap = StationData.getLineMap();

        // First, try to find a direct route on a single line
        RouteResult directRoute = findDirectRoute(startStation, endStation, lineMap);
        if (directRoute.isValid()) {
            return directRoute;
        }

        // If no direct route, find route with one transfer
        RouteResult transferRoute = findTransferRoute(startStation, endStation, lineMap);
        return transferRoute;
    }

    /**
     * Find direct route on a single line (no transfers)
     */
    private static RouteResult findDirectRoute(String start, String end, Map<String, List<String>> lineMap) {
        RouteResult result = new RouteResult();

        for (List<String> line : lineMap.values()) {
            if (line.contains(start) && line.contains(end)) {
                int startIdx = line.indexOf(start);
                int endIdx = line.indexOf(end);

                List<String> route = getSubRoute(start, end, line);
                result.setStations(route);

                // Set direction to terminal station
                if (startIdx < endIdx) {
                    result.setDirection(line.get(line.size() - 1));
                } else {
                    result.setDirection(line.get(0));
                }

                result.setStartLine(StationData.getStationLine(start));
                result.setEndLine(StationData.getStationLine(end));

                return result;
            }
        }

        return result;
    }

    /**
     * Find route with one transfer at an interchange station
     */
    private static RouteResult findTransferRoute(String start, String end, Map<String, List<String>> lineMap) {
        RouteResult result = new RouteResult();

        // Try each interchange station
        for (String interchange : StationData.INTERCHANGE_STATIONS) {
            // Check if we can reach interchange from start AND end from interchange
            if (canReach(start, interchange, lineMap) && canReach(interchange, end, lineMap)) {

                // Get first part of journey (start -> interchange)
                List<String> firstPart = null;
                List<String> startLine = null;
                for (List<String> line : lineMap.values()) {
                    if (line.contains(start) && line.contains(interchange)) {
                        firstPart = getSubRoute(start, interchange, line);
                        startLine = line;
                        break;
                    }
                }

                // Get second part of journey (interchange -> end)
                List<String> secondPart = null;
                List<String> endLine = null;
                for (List<String> line : lineMap.values()) {
                    if (line.contains(interchange) && line.contains(end)) {
                        secondPart = getSubRoute(interchange, end, line);
                        endLine = line;
                        break;
                    }
                }

                if (firstPart != null && secondPart != null && startLine != endLine) {
                    // Combine routes, removing duplicate interchange station
                    List<String> fullRoute = new ArrayList<>(firstPart);
                    secondPart.remove(0); // Remove duplicate interchange
                    fullRoute.addAll(secondPart);

                    result.setStations(fullRoute);
                    result.setTransferStation(interchange);
                    result.setDirection(endLine.get(endLine.size() - 1)); // Direction toward final destination
                    result.setStartLine(StationData.getStationLine(start));
                    result.setEndLine(StationData.getStationLine(end));

                    return result;
                }
            }
        }

        return result;
    }

    /**
     * Check if two stations are connected on any line
     */
    private static boolean canReach(String start, String end, Map<String, List<String>> lineMap) {
        for (List<String> line : lineMap.values()) {
            if (line.contains(start) && line.contains(end)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get ordered list of stations between start and end on a given line
     */
    private static List<String> getSubRoute(String start, String end, List<String> line) {
        if (line == null || !line.contains(start) || !line.contains(end)) {
            return new ArrayList<>();
        }

        int startIdx = line.indexOf(start);
        int endIdx = line.indexOf(end);

        int minIdx = Math.min(startIdx, endIdx);
        int maxIdx = Math.max(startIdx, endIdx);

        List<String> subRoute = new ArrayList<>(line.subList(minIdx, maxIdx + 1));

        // Reverse if traveling in opposite direction
        if (startIdx > endIdx) {
            Collections.reverse(subRoute);
        }

        return subRoute;
    }

    /**
     * Calculate remaining stations from current location to destination
     */
    public static int calculateRemainingStations(List<String> route, String currentStation) {
        if (route == null || currentStation == null || !route.contains(currentStation)) {
            return -1;
        }

        int currentIndex = route.indexOf(currentStation);
        return route.size() - 1 - currentIndex;
    }
}

