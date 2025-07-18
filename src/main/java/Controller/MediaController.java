package Controller;

import util.TimeFormatter;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class MediaController {
    private MediaPlayer mediaPlayer;
    private RotateTransition diskSpin;
    private boolean isPlaying = false;

    private Slider progressSlider;
    private Slider volumeSlider;
    private Label currentTimeLabel;
    private Label totalTimeLabel;
    private Label fileNameLabel;
    private ImageView diskImageView;

    private SoundPlayerController mainController;

    public MediaController(SoundPlayerController mainController,
                           Slider progressSlider, Slider volumeSlider,
                           Label currentTimeLabel, Label totalTimeLabel,
                           Label fileNameLabel, ImageView diskImageView) {
        this.mainController = mainController;
        this.progressSlider = progressSlider;
        this.volumeSlider = volumeSlider;
        this.currentTimeLabel = currentTimeLabel;
        this.totalTimeLabel = totalTimeLabel;
        this.fileNameLabel = fileNameLabel;
        this.diskImageView = diskImageView;

        setupDiskAnimation();
    }

    private void setupDiskAnimation() {
        diskSpin = new RotateTransition(Duration.seconds(2), diskImageView);
        diskSpin.setByAngle(360);
        diskSpin.setCycleCount(RotateTransition.INDEFINITE);
        diskSpin.setInterpolator(Interpolator.LINEAR);
        diskSpin.stop();
    }

    public void playFile(File file) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        fileNameLabel.setText(file.getName());

        setupMediaPlayerListeners();

        mediaPlayer.play();
        startDiskSpin();
        isPlaying = true;
    }

    private void setupMediaPlayerListeners() {
        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!progressSlider.isValueChanging()) {
                progressSlider.setValue(newTime.toSeconds());
            }
            currentTimeLabel.setText(TimeFormatter.formatTime(newTime, mediaPlayer.getTotalDuration()));
        });

        mediaPlayer.setOnReady(() -> {
            Duration total = mediaPlayer.getTotalDuration();
            progressSlider.setMax(total.toSeconds());
            totalTimeLabel.setText(TimeFormatter.formatTime(total, total));
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
            mainController.updatePlayPauseButton(true);
        });

        mediaPlayer.setOnPaused(() -> {
            stopDiskSpin();
            mainController.updatePlayPauseButton(false);
        });

        mediaPlayer.setOnEndOfMedia(() -> {
            stopDiskSpin();
            progressSlider.setValue(progressSlider.getMax());
            mainController.updatePlayPauseButton(false);
            isPlaying = false;
        });

        mediaPlayer.setOnStopped(() -> {
            stopDiskSpin();
            mainController.updatePlayPauseButton(false);
        });
    }

    public void updatePlaybackRate() {
        if (mediaPlayer != null) {
            String speedStr = mainController.getSpeedComboBox().getValue();
            double rate = 1.0;
            switch (speedStr) {
                case "0.5x": rate = 0.5; break;
                case "1x": rate = 1.0; break;
                case "1.5x": rate = 1.5; break;
                case "2x": rate = 2.0; break;
            }
            mediaPlayer.setRate(rate);
            diskSpin.setDuration(Duration.seconds(2 / rate));
            if (diskSpin.getStatus() == javafx.animation.Animation.Status.RUNNING) {
                diskSpin.play();
            }
        }
    }

    public void togglePlayPause() {
        if (mediaPlayer != null) {
            MediaPlayer.Status status = mediaPlayer.getStatus();

            // If song ended, reset and play from start
            if (status == MediaPlayer.Status.STOPPED ||
                    (status == MediaPlayer.Status.PAUSED && progressSlider.getValue() == progressSlider.getMax())) {
                mediaPlayer.seek(Duration.ZERO);
                progressSlider.setValue(0);
                resetDiskSpin();
                startDiskSpin();
                mediaPlayer.play();
                mainController.updatePlayPauseButton(true);
                isPlaying = true;
            }
            // If song is at the end (finished), reset and play
            else if (progressSlider.getValue() == progressSlider.getMax()) {
                mediaPlayer.seek(Duration.ZERO);
                progressSlider.setValue(0);
                resetDiskSpin();
                startDiskSpin();
                mediaPlayer.play();
                mainController.updatePlayPauseButton(true);
                isPlaying = true;
            }
            // If paused and not at end, play
            else if (status == MediaPlayer.Status.PAUSED) {
                mediaPlayer.play();
                startDiskSpin();
                mainController.updatePlayPauseButton(true);
                isPlaying = true;
            }
            // If playing, pause
            else if (status == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                stopDiskSpin();
                mainController.updatePlayPauseButton(false);
                isPlaying = false;
            }
            // If ready, play
            else if (status == MediaPlayer.Status.READY) {
                mediaPlayer.play();
                startDiskSpin();
                mainController.updatePlayPauseButton(true);
                isPlaying = true;
            }
        }
    }

    public void resetAndPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.ZERO);
            progressSlider.setValue(0);
            resetDiskSpin();
            startDiskSpin();
            mediaPlayer.play();
            mainController.updatePlayPauseButton(true);
            isPlaying = true;
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            stopDiskSpin();
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

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}