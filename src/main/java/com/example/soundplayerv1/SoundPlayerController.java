package com.example.soundplayerv1;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.*;

public class SoundPlayerController {

    @FXML
    private Slider progressSlider;
    @FXML
    private Label fileNameLabel;
    @FXML
    private Label currentTimeLabel;
    @FXML
    private Label totalTimeLabel;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ComboBox<String> speedComboBox;
    @FXML
    private ListView<String> soundListView;
    @FXML
    private VBox sidePanel;
    @FXML
    private Button sidePanelToggleButton;
    @FXML
    private ImageView diskImageView;
    @FXML
    private Button playPauseButton;

    private ObservableList<String> soundNames = FXCollections.observableArrayList();
    private ObservableList<File> soundFiles = FXCollections.observableArrayList();

    private MediaPlayer mediaPlayer;
    private int currentlyPlayingIndex = -1;
    private RotateTransition diskSpin;
    private boolean isPlaying = false;

    private static final String SOUND_LIST_FILE = System.getProperty("user.home") + File.separator + ".soundplayerv1_sounds.txt";


    @FXML
    private void initialize() {
        loadSoundList();

        soundListView.setItems(soundNames);

        soundListView.setOnMouseClicked(event -> {
            int index = soundListView.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                playSoundAtIndex(index);
            }
        });

        sidePanel.managedProperty().bind(sidePanel.visibleProperty());
        sidePanel.setVisible(false);

        sidePanelToggleButton.setOnAction(e -> {
            sidePanel.setVisible(!sidePanel.isVisible());
            sidePanelToggleButton.setText(sidePanel.isVisible() ? "Hide Sounds" : "Show Sounds");
        });

        soundListView.getStyleClass().add("sound-list-view");
        fileNameLabel.getStyleClass().add("file-name-label");
        progressSlider.getStyleClass().add("progress-slider");
        sidePanelToggleButton.getStyleClass().add("side-panel-toggle-button");
        VBox.setVgrow(soundListView, Priority.ALWAYS);

        progressSlider.setValue(0);
        currentTimeLabel.setText("0:00");
        totalTimeLabel.setText("0:00");

        volumeSlider.setMin(0);
        volumeSlider.setMax(1);
        volumeSlider.setValue(0.8);
        volumeSlider.getStyleClass().add("volume-slider");
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newVal.doubleValue());
            }
        });

        speedComboBox.setItems(FXCollections.observableArrayList("0.5x", "1x", "1.5x", "2x"));
        speedComboBox.setValue("1x");
        speedComboBox.getStyleClass().add("speed-combo");
        speedComboBox.setOnAction(e -> updatePlaybackRate());

        Image diskImage = new Image(getClass().getResourceAsStream("/com/example/soundplayerv1/disk.png"));
        diskImageView.setImage(diskImage);
        diskImageView.setPreserveRatio(true);
        diskImageView.setFitWidth(120);
        diskImageView.setFitHeight(120);
        diskImageView.getStyleClass().add("disk-image");

        diskSpin = new RotateTransition(javafx.util.Duration.seconds(2), diskImageView);
        diskSpin.setByAngle(360);
        diskSpin.setCycleCount(RotateTransition.INDEFINITE);
        diskSpin.setInterpolator(Interpolator.LINEAR);
        diskSpin.stop();

        updatePlayPauseButton(false); // Start with play icon
    }

    private void updatePlaybackRate() {
        if (mediaPlayer != null) {
            String speedStr = speedComboBox.getValue();
            double rate = 1.0;
            switch (speedStr) {
                case "0.5x": rate = 0.5; break;
                case "1x": rate = 1.0; break;
                case "1.5x": rate = 1.5; break;
                case "2x": rate = 2.0; break;
            }
            mediaPlayer.setRate(rate);
            diskSpin.setDuration(javafx.util.Duration.seconds(2 / rate));
            if (diskSpin.getStatus() == javafx.animation.Animation.Status.RUNNING) {
                diskSpin.play();
            }
        }
    }

    private void playSoundAtIndex(int index) {
        File file = soundFiles.get(index);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        fileNameLabel.setText(file.getName());
        currentlyPlayingIndex = index;

        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!progressSlider.isValueChanging()) {
                progressSlider.setValue(newTime.toSeconds());
            }
            currentTimeLabel.setText(formatTime(newTime, mediaPlayer.getTotalDuration()));
        });

        mediaPlayer.setOnReady(() -> {
            Duration total = mediaPlayer.getTotalDuration();
            progressSlider.setMax(total.toSeconds());
            totalTimeLabel.setText(formatTime(total, total));
            mediaPlayer.setVolume(volumeSlider.getValue());
            updatePlaybackRate();
        });

        progressSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (progressSlider.isValueChanging() && mediaPlayer != null) {
                mediaPlayer.seek(Duration.seconds(newVal.doubleValue()));
            }
        });

        mediaPlayer.setOnPlaying(() -> {
            startDiskSpin();
            updatePlayPauseButton(true);
        });
        mediaPlayer.setOnPaused(() -> {
            stopDiskSpin();
            updatePlayPauseButton(false);
        });
        mediaPlayer.setOnEndOfMedia(() -> {
            stopDiskSpin();
            progressSlider.setValue(progressSlider.getMax());
            updatePlayPauseButton(false);
            isPlaying = false;
        });
        mediaPlayer.setOnStopped(() -> {
            stopDiskSpin();
            updatePlayPauseButton(false);
        });

        mediaPlayer.play();
        startDiskSpin();
        updatePlayPauseButton(true);
        isPlaying = true;
    }

    @FXML
    private void onOpenFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav")
        );
        File file = fileChooser.showOpenDialog(fileNameLabel.getScene().getWindow());

        if (file != null) {
            boolean alreadyExists = soundFiles.stream()
                    .anyMatch(f -> f.getAbsolutePath().equalsIgnoreCase(file.getAbsolutePath()));

            if (!alreadyExists) {
                soundFiles.add(file);
                soundNames.add(file.getName());
                soundListView.getSelectionModel().select(soundNames.size() - 1);
                playSoundAtIndex(soundNames.size() - 1);
                saveSoundList();
            } else {
                soundListView.getSelectionModel().select(soundFiles.indexOf(file));
            }
        }
    }

    @FXML
    private void onPlayPause() {
        if (mediaPlayer != null) {
            MediaPlayer.Status status = mediaPlayer.getStatus();

            // If song ended, reset and play from start
            if (status == MediaPlayer.Status.STOPPED || (status == MediaPlayer.Status.PAUSED && progressSlider.getValue() == progressSlider.getMax())) {
                mediaPlayer.seek(Duration.ZERO);
                progressSlider.setValue(0);
                resetDiskSpin();
                startDiskSpin();
                mediaPlayer.play();
                updatePlayPauseButton(true);
                isPlaying = true;
            }
            // If song is at the end (finished), reset and play
            else if (progressSlider.getValue() == progressSlider.getMax()) {
                mediaPlayer.seek(Duration.ZERO);
                progressSlider.setValue(0);
                resetDiskSpin();
                startDiskSpin();
                mediaPlayer.play();
                updatePlayPauseButton(true);
                isPlaying = true;
            }
            // If paused and not at end, play
            else if (status == MediaPlayer.Status.PAUSED) {
                mediaPlayer.play();
                startDiskSpin();
                updatePlayPauseButton(true);
                isPlaying = true;
            }
            // If playing, pause
            else if (status == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                stopDiskSpin();
                updatePlayPauseButton(false);
                isPlaying = false;
            }
            // If ready, play
            else if (status == MediaPlayer.Status.READY) {
                mediaPlayer.play();
                startDiskSpin();
                updatePlayPauseButton(true);
                isPlaying = true;
            }
        }
    }

    @FXML
    private void onReset() {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.ZERO);
            progressSlider.setValue(0);
            resetDiskSpin();
            startDiskSpin();
            mediaPlayer.play();
            updatePlayPauseButton(true);
            isPlaying = true;
        }
    }

    private void startDiskSpin() {
        diskSpin.play();
    }

    private void stopDiskSpin() {
        diskSpin.pause();
    }

    private void resetDiskSpin() {
        diskSpin.stop();
        diskImageView.setRotate(0);
    }

    private void updatePlayPauseButton(boolean playing) {
        playPauseButton.setText(playing ? "⏸" : "▶");
    }

    private String formatTime(Duration elapsed, Duration total) {
        int intElapsed = (int)Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed % 60;

        return String.format("%d:%02d", elapsedMinutes, elapsedSeconds);
    }

    // ----------- Persistence Logic -------------
    private void saveSoundList() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SOUND_LIST_FILE))) {
            for (File file : soundFiles) {
                bw.write(file.getAbsolutePath());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving sound list: " + e.getMessage());
        }
    }

    private void loadSoundList() {
        soundFiles.clear();
        soundNames.clear();
        File file = new File(SOUND_LIST_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                File f = new File(line);
                if (f.exists()) {
                    soundFiles.add(f);
                    soundNames.add(f.getName());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading sound list: " + e.getMessage());
        }
    }
}