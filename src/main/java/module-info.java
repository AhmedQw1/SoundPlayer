module com.example.soundplayerv1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.prefs;

    // New modules
    requires com.jfoenix;                     // JFoenix (automatic module name)
    requires org.kordamp.ikonli.javafx;       // Ikonli JavaFX binding
    requires org.kordamp.ikonli.fontawesome5; // Ikonli FontAwesome5 pack
    requires org.kordamp.ikonli.materialdesign2; // Ikonli MaterialDesign2 pack
    requires fr.brouillard.oss.cssfx;         // CSSFX (automatic module name)

    opens com.example.soundplayerv1 to javafx.fxml;
    exports com.example.soundplayerv1;
    opens Controller to javafx.fxml;
    exports Controller;
    exports model;
    exports util;
}