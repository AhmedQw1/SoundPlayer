module com.example.soundplayerv1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.media;
    requires java.prefs;

    // Open the main package to FXML
    opens com.example.soundplayerv1 to javafx.fxml;
    exports com.example.soundplayerv1;

    // Open the Controller package to FXML
    opens Controller to javafx.fxml;
    exports Controller;

    // Export model and util packages if needed by other modules
    exports model;
    exports util;
}