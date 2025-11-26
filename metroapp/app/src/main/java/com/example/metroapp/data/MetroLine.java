package com.example.metroapp.data;

import androidx.annotation.ColorRes;
import com.example.metroapp.R;

public enum MetroLine {
    LINE_1("Line 1", "#E91E63", 1),
    LINE_2("Line 2", "#FFC107", 2),
    LINE_3("Line 3", "#4CAF50", 3);

    private final String displayName;
    private final String colorHex;
    private final int lineNumber;

    MetroLine(String displayName, String colorHex, int lineNumber) {
        this.displayName = displayName;
        this.colorHex = colorHex;
        this.lineNumber = lineNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColorHex() {
        return colorHex;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}

