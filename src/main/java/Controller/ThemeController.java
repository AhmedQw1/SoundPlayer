package Controller;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.prefs.Preferences;

public class ThemeController {
    private boolean isDarkTheme = true;
    private Button themeToggleButton;
    private VBox sidePanel;

    private static final String DARK_THEME_PATH = "/com/example/soundplayerv1/css/dark/dark-theme.css";
    private static final String LIGHT_THEME_PATH = "/com/example/soundplayerv1/css/light/light-theme.css";
    private static final String PREF_THEME = "theme";
    private Preferences prefs = Preferences.userNodeForPackage(ThemeController.class);

    public ThemeController(Button themeToggleButton, VBox sidePanel) {
        this.themeToggleButton = themeToggleButton;
        this.sidePanel = sidePanel;

        // Loads the theme preference
        isDarkTheme = prefs.getBoolean(PREF_THEME, true);
        updateButtonIcon();
    }

    public void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        prefs.putBoolean(PREF_THEME, isDarkTheme);
        updateButtonIcon();
        updateThemeUI();
    }

    private void updateButtonIcon() {
        themeToggleButton.setText(isDarkTheme ? "🌙" : "☀");
    }

    public void updateThemeUI() {
        Scene scene = themeToggleButton.getScene();
        if (scene != null) {
            // Remove any existing theme stylesheets
            scene.getStylesheets().removeIf(stylesheet ->
                    stylesheet.contains("dark-theme.css") ||
                            stylesheet.contains("light-theme.css"));

            // Add the theme stylesheet
            String themePath = isDarkTheme ? DARK_THEME_PATH : LIGHT_THEME_PATH;
            scene.getStylesheets().add(getClass().getResource(themePath).toExternalForm());

            // Update side panel color for light theme
            if (isDarkTheme) {
                sidePanel.setStyle("-fx-background-color: linear-gradient(to bottom, #23272a, #1b1f22);");
            } else {
                sidePanel.setStyle("-fx-background-color: linear-gradient(to bottom, #e6e9f0, #dce0e8);");
            }
        }
    }

    public boolean isDarkTheme() {
        return isDarkTheme;
    }
}