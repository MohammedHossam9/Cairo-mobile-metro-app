package com.example.metroapp.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.example.metroapp.data.MetroLine;

/**
 * Utility class for UI-related helper methods
 */
public class UIUtils {

    /**
     * Get color for a metro line
     */
    public static int getLineColor(MetroLine line) {
        return Color.parseColor(line.getColorHex());
    }

    /**
     * Share route via any sharing app
     */
    public static void shareRoute(Context context, String routeDetails) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "🚇 Cairo Metro Route:\n\n" + routeDetails);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share route via");
        context.startActivity(shareIntent);
    }

    /**
     * Format station name with line color badge
     */
    public static SpannableString formatStationWithLine(String stationName, MetroLine line) {
        String formatted = "● " + stationName;
        SpannableString spannable = new SpannableString(formatted);

        // Color the bullet point
        spannable.setSpan(
            new ForegroundColorSpan(getLineColor(line)),
            0, 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        return spannable;
    }

    /**
     * Create bold spannable text
     */
    public static SpannableString makeBold(String text) {
        SpannableString spannable = new SpannableString(text);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * Convert distance to human readable format
     */
    public static String formatDistance(float meters) {
        if (meters < 1000) {
            return String.format("%.0f m", meters);
        } else {
            return String.format("%.2f km", meters / 1000);
        }
    }

    /**
     * Convert time to human readable format
     */
    public static String formatTime(int minutes) {
        if (minutes < 60) {
            return minutes + " min";
        } else {
            int hours = minutes / 60;
            int mins = minutes % 60;
            if (mins == 0) {
                return hours + " hr";
            }
            return hours + " hr " + mins + " min";
        }
    }
}

