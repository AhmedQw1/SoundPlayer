package com.example.soundplayerv1;

import Controller.ThemeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.prefs.Preferences;

public class SoundPlayerApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/soundplayerv1/SoundPlayer-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 920, 640); // Set initial size, resizable

        // Apply saved theme preference
        Preferences prefs = Preferences.userNodeForPackage(ThemeController.class);
        boolean isDarkTheme = prefs.getBoolean("theme", true);

        String themePath = isDarkTheme ?
                "/com/example/soundplayerv1/css/dark/dark-theme.css" :
                "/com/example/soundplayerv1/css/light/light-theme.css";

        scene.getStylesheets().add(getClass().getResource(themePath).toExternalForm());

        stage.setTitle("Sound Player");
        stage.setMinWidth(400);
        stage.setMinHeight(250);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}