package Controller;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import javafx.scene.control.OverrunStyle;

public class UIController {
    private final SoundPlayerController mainController;

    public UIController(SoundPlayerController mainController) {
        this.mainController = mainController;
    }

    public void initializeUI() {
        JFXSlider progressSlider = mainController.getProgressSlider();
        JFXSlider volumeSlider = mainController.getVolumeSlider();
        JFXComboBox<String> speedComboBox = mainController.getSpeedComboBox();
        ListView<String> soundListView = mainController.getSoundListView();
        Label currentTimeLabel = mainController.getCurrentTimeLabel();
        Label totalTimeLabel = mainController.getTotalTimeLabel();

        // Style classes
        soundListView.getStyleClass().add("sound-list-view");
        mainController.getFileNameLabel().getStyleClass().add("file-name-label");
        progressSlider.getStyleClass().add("progress-slider");
        mainController.getSidePanelToggleButton().getStyleClass().add("side-panel-toggle-button");
        VBox.setVgrow(soundListView, Priority.ALWAYS);

        // Initial labels
        progressSlider.setValue(0);
        currentTimeLabel.setText("0:00");
        totalTimeLabel.setText("0:00");

        // Volume slider (0-100 visible; mapped to 0.0–1.0 elsewhere)
        volumeSlider.setMin(0);
        volumeSlider.setMax(100);
        volumeSlider.setValue(80);
        volumeSlider.getStyleClass().add("volume-slider");

        // Speed combo
        setupJFXSpeedComboBox(speedComboBox);

        // Disk image
        Image diskImage = new Image(getClass().getResourceAsStream("/com/example/soundplayerv1/disk.png"));
        mainController.getDiskImageView().setImage(diskImage);
        mainController.getDiskImageView().setPreserveRatio(true);
        mainController.getDiskImageView().setFitWidth(120);
        mainController.getDiskImageView().setFitHeight(120);
        mainController.getDiskImageView().getStyleClass().add("disk-image");

        // Side panel
        mainController.getSidePanel().managedProperty().bind(mainController.getSidePanel().visibleProperty());
        mainController.getSidePanel().setVisible(false);

        mainController.getSidePanelToggleButton().setOnAction(e -> {
            boolean vis = !mainController.getSidePanel().isVisible();
            mainController.getSidePanel().setVisible(vis);
            mainController.getSidePanelToggleButton().setText(vis ? "Hide Sounds" : "Show Sounds");
        });
    }

    private void setupJFXSpeedComboBox(JFXComboBox<String> speedComboBox) {
        speedComboBox.getItems().setAll("0.5x", "1x", "1.5x", "2x");
        if (!speedComboBox.getStyleClass().contains("speed-combo")) {
            speedComboBox.getStyleClass().add("speed-combo");
        }
        speedComboBox.setEditable(false);
        speedComboBox.setVisibleRowCount(4);

        // Button cell (the visible selected value)
        ListCell<String> buttonCell = new ListCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                setTextOverrun(OverrunStyle.CLIP);
                setStyle("-fx-alignment: CENTER;");
            }
        };
        speedComboBox.setButtonCell(buttonCell);

        // Cells in popup list
        speedComboBox.setCellFactory(listView -> new ListCell<>() {
            {
                setTextOverrun(OverrunStyle.CLIP);
                setStyle("-fx-alignment: CENTER;");
            }
            @Override protected void updateItem(String val, boolean empty) {
                super.updateItem(val, empty);
                setText(empty || val == null ? null : val);
            }
        });

        // Set default after layout to avoid race with skin creation
        Platform.runLater(() -> {
            if (speedComboBox.getValue() == null) {
                speedComboBox.getSelectionModel().select("1x");
            }
        });

        speedComboBox.setOnAction(e -> {
            String selectedValue = speedComboBox.getValue();
            if (selectedValue != null) {
                mainController.getMediaController().updatePlaybackRate();
            }
        });
    }
}