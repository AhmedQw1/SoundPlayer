module com.example.soundplayerv1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;
    requires javafx.base;
    requires java.prefs;
    requires java.desktop;

    // JFoenix and Ikonli modules
    requires com.jfoenix;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.materialdesign2;

    // Open packages for reflection
    opens com.example.soundplayerv1 to javafx.fxml;
    opens Controller to javafx.fxml;

    // Export packages
    exports com.example.soundplayerv1;
    exports Controller;
    exports model;
    exports util;
}