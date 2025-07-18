package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class SoundPlayerController {

    @FXML private Slider progressSlider;
    @FXML private Label fileNameLabel;
    @FXML private Label currentTimeLabel;
    @FXML private Label totalTimeLabel;
    @FXML private Slider volumeSlider;
    @FXML private ComboBox<String> speedComboBox;
    @FXML private ListView<String> soundListView;
    @FXML private VBox sidePanel;
    @FXML private Button sidePanelToggleButton;
    @FXML private ImageView diskImageView;
    @FXML private Button playPauseButton;
    @FXML private Button themeToggleButton;
    @FXML private BorderPane rootPane;

    private MediaController mediaController;
    private PlaylistController playlistController;
    private ThemeController themeController;
    private UIController uiController;

    @FXML
    private void initialize() {
        // Initialize the controllers
        uiController = new UIController(this);
        mediaController = new MediaController(this, progressSlider, volumeSlider,
                currentTimeLabel, totalTimeLabel,
                fileNameLabel, diskImageView);
        playlistController = new PlaylistController(this, soundListView);
        themeController = new ThemeController(themeToggleButton, sidePanel);

        // Initialize the UI components
        uiController.initializeUI();

        // Sets up theme listener
        themeToggleButton.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                themeController.updateThemeUI();
            }
        });

        // Initialize the play/pause button
        updatePlayPauseButton(false);
    }

    @FXML
    private void toggleTheme() {
        themeController.toggleTheme();
    }

    @FXML
    private void onOpenFile() {
        playlistController.openFile();
    }

    @FXML
    private void onPlayPause() {
        mediaController.togglePlayPause();
    }

    @FXML
    private void onReset() {
        mediaController.resetAndPlay();
    }

    public void updatePlayPauseButton(boolean playing) {
        playPauseButton.setText(playing ? "⏸" : "▶");
    }

    public void updateFileNameLabel(String text) {
        fileNameLabel.setText(text);
    }

    public void resetProgressUI() {
        progressSlider.setValue(0);
        currentTimeLabel.setText("0:00");
        totalTimeLabel.setText("0:00");
    }

    // Getters for UI components
    public Slider getProgressSlider() { return progressSlider; }
    public Label getFileNameLabel() { return fileNameLabel; }
    public Label getCurrentTimeLabel() { return currentTimeLabel; }
    public Label getTotalTimeLabel() { return totalTimeLabel; }
    public Slider getVolumeSlider() { return volumeSlider; }
    public ComboBox<String> getSpeedComboBox() { return speedComboBox; }
    public ListView<String> getSoundListView() { return soundListView; }
    public VBox getSidePanel() { return sidePanel; }
    public Button getSidePanelToggleButton() { return sidePanelToggleButton; }
    public ImageView getDiskImageView() { return diskImageView; }
    public Button getPlayPauseButton() { return playPauseButton; }
    public Button getThemeToggleButton() { return themeToggleButton; }

    // Getters for controllers
    public MediaController getMediaController() { return mediaController; }
    public PlaylistController getPlaylistController() { return playlistController; }
    public ThemeController getThemeController() { return themeController; }
    public UIController getUiController() { return uiController; }
}