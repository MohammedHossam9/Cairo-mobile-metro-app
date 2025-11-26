package com.example.metroapp.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Route calculation result with journey details
 */
public class RouteResult {
    private List<String> stations;
    private String transferStation;
    private String direction;
    private int stationCount;
    private int estimatedMinutes;
    private int ticketPrice;
    private MetroLine startLine;
    private MetroLine endLine;
    private boolean hasTransfer;

    public RouteResult() {
        this.stations = new ArrayList<>();
        this.transferStation = "";
        this.direction = "";
        this.hasTransfer = false;
    }

    public List<String> getStations() {
        return stations;
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
        this.stationCount = Math.max(0, stations.size() - 1);
        this.estimatedMinutes = this.stationCount * 2; // 2 minutes per station average
        this.ticketPrice = calculateTicketPrice(this.stationCount);
    }

    public String getTransferStation() {
        return transferStation;
    }

    public void setTransferStation(String transferStation) {
        this.transferStation = transferStation;
        this.hasTransfer = transferStation != null && !transferStation.isEmpty();
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getStationCount() {
        return stationCount;
    }

    public int getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public MetroLine getStartLine() {
        return startLine;
    }

    public void setStartLine(MetroLine startLine) {
        this.startLine = startLine;
    }

    public MetroLine getEndLine() {
        return endLine;
    }

    public void setEndLine(MetroLine endLine) {
        this.endLine = endLine;
    }

    public boolean hasTransfer() {
        return hasTransfer;
    }

    public String getFormattedTime() {
        int hours = estimatedMinutes / 60;
        int mins = estimatedMinutes % 60;
        if (hours > 0) {
            return hours + " hr " + mins + " min";
        } else {
            return mins + " min";
        }
    }

    /**
     * Calculate ticket price based on Cairo Metro pricing tiers (2024)
     */
    private int calculateTicketPrice(int stations) {
        if (stations <= 9) return 8;      // Up to 9 stations: 8 EGP
        if (stations <= 16) return 10;    // 10-16 stations: 10 EGP
        return 15;                         // 17+ stations: 15 EGP
    }

    public boolean isValid() {
        return stations != null && stations.size() >= 2;
    }
}

