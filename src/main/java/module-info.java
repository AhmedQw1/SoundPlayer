module com.example.soundplayerv1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.prefs;

    opens com.example.soundplayerv1 to javafx.fxml;
    exports com.example.soundplayerv1;
    opens Controller to javafx.fxml;
    exports Controller;
    exports model;
    exports util;
}