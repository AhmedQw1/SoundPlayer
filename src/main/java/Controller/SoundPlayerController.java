package Controller;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaView;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.geometry.Bounds;
import javafx.application.Platform;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

public class SoundPlayerController {

    @FXML private JFXSlider progressSlider;
    @FXML private Label fileNameLabel;
    @FXML private Label currentTimeLabel;
    @FXML private Label totalTimeLabel;
    @FXML private JFXSlider volumeSlider;
    @FXML private JFXComboBox<String> speedComboBox;
    @FXML private ListView<String> soundListView;
    @FXML private VBox sidePanel;
    @FXML private Button sidePanelToggleButton;
    @FXML private ImageView diskImageView;
    @FXML private Button playPauseButton;
    @FXML private Button resetButton;
    @FXML private Button previousButton;
    @FXML private Button nextButton;
    @FXML private Button themeToggleButton;
    @FXML private BorderPane rootPane;
    @FXML private StackPane mediaContainer;
    @FXML private MediaView mediaView;

    private MediaController mediaController;
    private PlaylistController playlistController;
    private ThemeController themeController;
    private UIController uiController;

    // Custom tooltip system
    private Popup progressTooltip;
    private Popup volumeTooltip;
    private Label progressTooltipLabel;
    private Label volumeTooltipLabel;

    @FXML
    private void initialize() {
        System.out.println("SoundPlayerController initialize() called");

        // Initialize the controllers
        uiController = new UIController(this);
        mediaController = new MediaController(this, progressSlider, volumeSlider,
                currentTimeLabel, totalTimeLabel,
                fileNameLabel, diskImageView, mediaView);
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

        // Add listener to refresh button colors when scene stylesheets change
        playPauseButton.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().addListener((javafx.collections.ListChangeListener.Change<? extends String> c) -> {
                    refreshPlayPauseButtonTheme();
                });
            }
        });

        resetButton.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().addListener((javafx.collections.ListChangeListener.Change<? extends String> c) -> {
                    refreshPlayPauseButtonTheme();
                });
            }
        });

        // Initialize all control buttons
        updatePlayPauseButton(false);
        initializeNavigationButtons();
        initializeResetButton();

        // Initialize modern sliders with enhanced styling
        initializeModernSliders();

        System.out.println("SoundPlayerController initialization complete");
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

    @FXML
    private void onPrevious() {
        playlistController.playPrevious();
    }

    @FXML
    private void onNext() {
        playlistController.playNext();
    }

    public void showVideoMode() {
        Platform.runLater(() -> {
            // Show video, hide disk
            mediaView.setVisible(true);
            diskImageView.setVisible(false);

            // Resize container for video
            mediaContainer.setPrefWidth(380);
            mediaContainer.setPrefHeight(280);
            mediaContainer.setMinWidth(380);
            mediaContainer.setMinHeight(280);
            mediaContainer.setMaxWidth(380);
            mediaContainer.setMaxHeight(280);

            sidePanelToggleButton.setText("Show Media");
            System.out.println("Video mode activated - container resized to 380x280");
        });
    }

    public void showAudioMode() {
        Platform.runLater(() -> {
            // Hide video, show disk
            mediaView.setVisible(false);
            diskImageView.setVisible(true);

            // Resize container for disk (smaller)
            mediaContainer.setPrefWidth(140);
            mediaContainer.setPrefHeight(140);
            mediaContainer.setMinWidth(140);
            mediaContainer.setMinHeight(140);
            mediaContainer.setMaxWidth(140);
            mediaContainer.setMaxHeight(140);

            sidePanelToggleButton.setText("Show Sounds");
            System.out.println("Audio mode activated - container resized to 140x140");
        });
    }

    public void updatePlayPauseButton(boolean playing) {
        FontIcon icon = new FontIcon(playing ? FontAwesomeSolid.PAUSE : FontAwesomeSolid.PLAY);
        icon.setIconSize(20);

        updateIconColor(icon);

        playPauseButton.setGraphic(icon);
        playPauseButton.setText("");
    }

    private void initializeNavigationButtons() {
        // Previous button
        FontIcon prevIcon = new FontIcon(FontAwesomeSolid.STEP_BACKWARD);
        prevIcon.setIconSize(16);
        updateIconColor(prevIcon);
        previousButton.setGraphic(prevIcon);
        previousButton.setText("");

        // Next button
        FontIcon nextIcon = new FontIcon(FontAwesomeSolid.STEP_FORWARD);
        nextIcon.setIconSize(16);
        updateIconColor(nextIcon);
        nextButton.setGraphic(nextIcon);
        nextButton.setText("");
    }

    private void updateIconColor(FontIcon icon) {
        if (playPauseButton.getScene() != null) {
            boolean isDarkTheme = playPauseButton.getScene().getStylesheets().stream()
                    .anyMatch(stylesheet -> stylesheet.contains("dark-theme.css"));

            if (isDarkTheme) {
                icon.setIconColor(javafx.scene.paint.Color.web("#cccccc"));
            } else {
                icon.setIconColor(javafx.scene.paint.Color.web("#333333"));
            }
        }
    }

    private void initializeResetButton() {
        FontIcon resetIcon = new FontIcon(FontAwesomeSolid.REDO);
        resetIcon.setIconSize(18);
        updateIconColor(resetIcon);
        resetButton.setGraphic(resetIcon);
        resetButton.setText("");
    }

    public void refreshPlayPauseButtonTheme() {
        if (playPauseButton.getGraphic() instanceof FontIcon) {
            updateIconColor((FontIcon) playPauseButton.getGraphic());
        }
        if (resetButton.getGraphic() instanceof FontIcon) {
            updateIconColor((FontIcon) resetButton.getGraphic());
        }
        if (previousButton.getGraphic() instanceof FontIcon) {
            updateIconColor((FontIcon) previousButton.getGraphic());
        }
        if (nextButton.getGraphic() instanceof FontIcon) {
            updateIconColor((FontIcon) nextButton.getGraphic());
        }
    }

    private void initializeModernSliders() {
        progressSlider.setIndicatorPosition(JFXSlider.IndicatorPosition.LEFT);
        progressSlider.setShowTickLabels(false);
        progressSlider.setShowTickMarks(false);

        volumeSlider.setIndicatorPosition(JFXSlider.IndicatorPosition.LEFT);
        volumeSlider.setShowTickLabels(false);
        volumeSlider.setShowTickMarks(false);

        progressSlider.setValueFactory(null);
        volumeSlider.setValueFactory(null);

        progressSlider.setValueFactory(slider ->
                javafx.beans.binding.Bindings.createStringBinding(() -> "", slider.valueProperty()));
        volumeSlider.setValueFactory(slider ->
                javafx.beans.binding.Bindings.createStringBinding(() -> "", slider.valueProperty()));

        progressSlider.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Platform.runLater(() -> {
                    setupCustomTooltips();
                });
            }
        });

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (Math.abs(newVal.doubleValue() - oldVal.doubleValue()) > 0.01) {
                volumeSlider.requestFocus();
            }
        });
    }

    private void setupCustomTooltips() {
        createCustomTooltips();

        progressSlider.setOnMouseEntered(this::showProgressTooltip);
        progressSlider.setOnMouseExited(e -> hideProgressTooltip());
        progressSlider.setOnMouseMoved(this::updateProgressTooltip);
        progressSlider.setOnMouseDragged(this::updateProgressTooltip);

        volumeSlider.setOnMouseEntered(this::showVolumeTooltip);
        volumeSlider.setOnMouseExited(e -> hideVolumeTooltip());
        volumeSlider.setOnMouseMoved(this::updateVolumeTooltip);
        volumeSlider.setOnMouseDragged(this::updateVolumeTooltip);
    }

    private void createCustomTooltips() {
        progressTooltip = new Popup();
        progressTooltip.setAutoHide(false);
        progressTooltip.setConsumeAutoHidingEvents(false);
        progressTooltipLabel = new Label();
        progressTooltipLabel.setStyle(
                "-fx-background-color: white; " +
                        "-fx-text-fill: #333333; " +
                        "-fx-border-color: #dddddd; " +
                        "-fx-border-width: 1px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-border-radius: 6px; " +
                        "-fx-font-size: 12px; " +
                        "-fx-font-weight: 500; " +
                        "-fx-padding: 6px 10px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 4, 0, 0, 2);"
        );
        progressTooltip.getContent().add(progressTooltipLabel);

        volumeTooltip = new Popup();
        volumeTooltip.setAutoHide(false);
        volumeTooltip.setConsumeAutoHidingEvents(false);
        volumeTooltipLabel = new Label();
        volumeTooltipLabel.setStyle(
                "-fx-background-color: white; " +
                        "-fx-text-fill: #333333; " +
                        "-fx-border-color: #dddddd; " +
                        "-fx-border-width: 1px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-border-radius: 6px; " +
                        "-fx-font-size: 12px; " +
                        "-fx-font-weight: 500; " +
                        "-fx-padding: 6px 10px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 4, 0, 0, 2);"
        );
        volumeTooltip.getContent().add(volumeTooltipLabel);
    }

    private void showProgressTooltip(MouseEvent event) {
        updateProgressTooltip(event);
        if (!progressTooltip.isShowing()) {
            progressTooltip.show(progressSlider, event.getScreenX() + 10, event.getScreenY() - 40);
        }
    }

    private void updateProgressTooltip(MouseEvent event) {
        double currentSeconds = progressSlider.getValue();
        String timeText = formatTime(currentSeconds);
        progressTooltipLabel.setText(timeText);

        if (progressTooltip.isShowing()) {
            progressTooltip.setX(event.getScreenX() + 10);
            progressTooltip.setY(event.getScreenY() - 40);
        }
    }

    private void hideProgressTooltip() {
        if (progressTooltip.isShowing()) {
            progressTooltip.hide();
        }
    }

    private void showVolumeTooltip(MouseEvent event) {
        updateVolumeTooltip(event);
        if (!volumeTooltip.isShowing()) {
            volumeTooltip.show(volumeSlider, event.getScreenX() + 10, event.getScreenY() - 40);
        }
    }

    private void updateVolumeTooltip(MouseEvent event) {
        int percentage = (int) Math.round(volumeSlider.getValue());
        volumeTooltipLabel.setText(percentage + "%");

        if (volumeTooltip.isShowing()) {
            volumeTooltip.setX(event.getScreenX() + 10);
            volumeTooltip.setY(event.getScreenY() - 40);
        }
    }

    private void hideVolumeTooltip() {
        if (volumeTooltip.isShowing()) {
            volumeTooltip.hide();
        }
    }

    public void updateFileNameLabel(String text) {
        fileNameLabel.setText(text);
    }

    public void resetProgressUI() {
        progressSlider.setValue(0);
        currentTimeLabel.setText("0:00");
        totalTimeLabel.setText("0:00");
    }

    private String formatTime(double seconds) {
        if (Double.isNaN(seconds) || seconds < 0) {
            return "0:00";
        }

        int totalSecs = (int) Math.round(seconds);
        int minutes = totalSecs / 60;
        int secs = totalSecs % 60;

        return String.format("%d:%02d", minutes, secs);
    }

    // Getters
    public JFXSlider getProgressSlider() { return progressSlider; }
    public Label getFileNameLabel() { return fileNameLabel; }
    public Label getCurrentTimeLabel() { return currentTimeLabel; }
    public Label getTotalTimeLabel() { return totalTimeLabel; }
    public JFXSlider getVolumeSlider() { return volumeSlider; }
    public JFXComboBox<String> getSpeedComboBox() { return speedComboBox; }
    public ListView<String> getSoundListView() { return soundListView; }
    public VBox getSidePanel() { return sidePanel; }
    public Button getSidePanelToggleButton() { return sidePanelToggleButton; }
    public ImageView getDiskImageView() { return diskImageView; }
    public Button getPlayPauseButton() { return playPauseButton; }
    public Button getThemeToggleButton() { return themeToggleButton; }
    public MediaView getMediaView() { return mediaView; }
    public StackPane getMediaContainer() { return mediaContainer; }

    public MediaController getMediaController() { return mediaController; }
    public PlaylistController getPlaylistController() { return playlistController; }
    public ThemeController getThemeController() { return themeController; }
    public UIController getUiController() { return uiController; }
}