package Controller;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import java.io.File;

public class MediaInfoController {

    public static VBox createEnhancedFileDisplay(File file, boolean isVideo) {
        VBox container = new VBox(5);
        container.setAlignment(Pos.CENTER);

        // Main file name
        Label fileName = new Label(file.getName());
        fileName.getStyleClass().add("enhanced-filename");

        // Media type indicator with icon
        HBox mediaInfo = new HBox(8);
        mediaInfo.setAlignment(Pos.CENTER);
        mediaInfo.getStyleClass().add("media-info-container");

        FontIcon mediaIcon = new FontIcon(isVideo ?
                FontAwesomeSolid.VIDEO : FontAwesomeSolid.MUSIC);
        mediaIcon.setIconSize(14);
        mediaIcon.setStyle("-fx-icon-color: " + (isVideo ? "#4a9eff" : "#5dcf65") + ";");

        Label mediaType = new Label(isVideo ? "Video" : "Audio");
        mediaType.getStyleClass().add("media-type-label");
        if (!isVideo) {
            mediaType.setStyle("-fx-text-fill: #5dcf65; -fx-font-size: 12px; -fx-font-weight: 600;");
        }

        // File size info
        long fileSize = file.length();
        String sizeText = formatFileSize(fileSize);
        Label sizeLabel = new Label(sizeText);
        sizeLabel.getStyleClass().add("file-size-label");

        Label separator = new Label("•");
        separator.setStyle("-fx-text-fill: #888888; -fx-font-size: 12px;");

        mediaInfo.getChildren().addAll(mediaIcon, mediaType, separator, sizeLabel);
        container.getChildren().addAll(fileName, mediaInfo);

        return container;
    }

    private static String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
        return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }

    public static void updateFileNameWithMediaType(Label fileNameLabel, File file, boolean isVideo) {
        fileNameLabel.setText(file.getName());

        // Add visual indicator for video/audio
        if (isVideo) {
            fileNameLabel.setStyle(
                    "-fx-font-size: 16px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-text-fill: #4a9eff; " +
                            "-fx-effect: dropshadow(gaussian, rgba(74,158,255,0.3), 2, 0, 0, 1);"
            );
        } else {
            fileNameLabel.setStyle(
                    "-fx-font-size: 16px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-text-fill: #5dcf65; " +
                            "-fx-effect: dropshadow(gaussian, rgba(93,207,101,0.3), 2, 0, 0, 1);"
            );
        }
    }
}