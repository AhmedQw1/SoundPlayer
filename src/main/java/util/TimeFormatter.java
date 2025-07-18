package util;

import javafx.util.Duration;

public class TimeFormatter {

    public static String formatTime(Duration elapsed, Duration total) {
        int intElapsed = (int)Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed % 60;

        return String.format("%d:%02d", elapsedMinutes, elapsedSeconds);
    }
}