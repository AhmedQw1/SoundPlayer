package Controller;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class UIController {
    private SoundPlayerController mainController;

    public UIController(SoundPlayerController mainController) {
        this.mainController = mainController;
    }

    public void initializeUI() {
        // Sets up UI components
        Slider progressSlider = mainController.getProgressSlider();
        Slider volumeSlider = mainController.getVolumeSlider();
        ComboBox<String> speedComboBox = mainController.getSpeedComboBox();
        ListView<String> soundListView = mainController.getSoundListView(); // FIXED: Changed VBox to ListView<String>
        Label currentTimeLabel = mainController.getCurrentTimeLabel();
        Label totalTimeLabel = mainController.getTotalTimeLabel();

        // Apply styles
        mainController.getSoundListView().getStyleClass().add("sound-list-view");
        mainController.getFileNameLabel().getStyleClass().add("file-name-label");
        progressSlider.getStyleClass().add("progress-slider");
        mainController.getSidePanelToggleButton().getStyleClass().add("side-panel-toggle-button");
        VBox.setVgrow(mainController.getSoundListView(), Priority.ALWAYS);

        // Initialize values
        progressSlider.setValue(0);
        currentTimeLabel.setText("0:00");
        totalTimeLabel.setText("0:00");

        // Sets up volume slider
        volumeSlider.setMin(0);
        volumeSlider.setMax(1);
        volumeSlider.setValue(0.8);
        volumeSlider.getStyleClass().add("volume-slider");
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (mainController.getMediaController().getMediaPlayer() != null) {
                mainController.getMediaController().getMediaPlayer().setVolume(newVal.doubleValue());
            }
        });

        // Sets up speed combo box
        speedComboBox.setItems(FXCollections.observableArrayList("0.5x", "1x", "1.5x", "2x"));
        speedComboBox.setValue("1x");
        speedComboBox.getStyleClass().add("speed-combo");
        speedComboBox.setOnAction(e -> mainController.getMediaController().updatePlaybackRate());

        // Sets up disk image
        Image diskImage = new Image(getClass().getResourceAsStream("/com/example/soundplayerv1/disk.png"));
        mainController.getDiskImageView().setImage(diskImage);
        mainController.getDiskImageView().setPreserveRatio(true);
        mainController.getDiskImageView().setFitWidth(120);
        mainController.getDiskImageView().setFitHeight(120);
        mainController.getDiskImageView().getStyleClass().add("disk-image");

        // Sets up the side panel
        mainController.getSidePanel().managedProperty().bind(mainController.getSidePanel().visibleProperty());
        mainController.getSidePanel().setVisible(false);

        mainController.getSidePanelToggleButton().setOnAction(e -> {
            mainController.getSidePanel().setVisible(!mainController.getSidePanel().isVisible());
            mainController.getSidePanelToggleButton().setText(
                    mainController.getSidePanel().isVisible() ? "Hide Sounds" : "Show Sounds");
        });
    }
}