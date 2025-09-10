package Controller;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXComboBox;
import util.TimeFormatter;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import javafx.application.Platform;

import java.io.File;

public class MediaController {
    private MediaPlayer mediaPlayer;
    private RotateTransition diskSpin;
    private boolean isPlaying = false;
    private boolean isVideoFile = false;
    private boolean isSeekingFromSlider = false;
    private boolean isUpdatingProgressFromMedia = false;

    private JFXSlider progressSlider;
    private JFXSlider volumeSlider;
    private Label currentTimeLabel;
    private Label totalTimeLabel;
    private Label fileNameLabel;
    private ImageView diskImageView;
    private MediaView mediaView;

    private SoundPlayerController mainController;

    public MediaController(SoundPlayerController mainController,
                           JFXSlider progressSlider, JFXSlider volumeSlider,
                           Label currentTimeLabel, Label totalTimeLabel,
                           Label fileNameLabel, ImageView diskImageView,
                           MediaView mediaView) {
        this.mainController = mainController;
        this.progressSlider = progressSlider;
        this.volumeSlider = volumeSlider;
        this.currentTimeLabel = currentTimeLabel;
        this.totalTimeLabel = totalTimeLabel;
        this.fileNameLabel = fileNameLabel;
        this.diskImageView = diskImageView;
        this.mediaView = mediaView;

        setupDiskAnimation();
        setupMediaView();
    }

    private void setupDiskAnimation() {
        diskSpin = new RotateTransition(Duration.seconds(2), diskImageView);
        diskSpin.setByAngle(360);
        diskSpin.setCycleCount(RotateTransition.INDEFINITE);
        diskSpin.setInterpolator(Interpolator.LINEAR);
        diskSpin.stop();
    }

    private void setupMediaView() {
        mediaView.setPreserveRatio(true);
        mediaView.setFitWidth(400);
        mediaView.setFitHeight(300);
        mediaView.setVisible(false);
    }

    public void playFile(File file) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        // Reset flags
        isSeekingFromSlider = false;
        isUpdatingProgressFromMedia = false;

        // Determine if file is video or audio
        String fileName = file.getName().toLowerCase();
        isVideoFile = fileName.endsWith(".mp4") || fileName.endsWith(".avi") ||
                fileName.endsWith(".mov") || fileName.endsWith(".mkv") ||
                fileName.endsWith(".wmv") || fileName.endsWith(".flv") ||
                fileName.endsWith(".webm");

        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        // Enhanced file name display with media type styling
        MediaInfoController.updateFileNameWithMediaType(fileNameLabel, file, isVideoFile);

        // Setup media player first, then UI state changes will happen in onReady
        setupMediaPlayerListeners();

        // Start playing
        mediaPlayer.play();
        isPlaying = true;
    }

    private void setupMediaPlayerListeners() {
        // Current time listener with lag prevention
        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!isSeekingFromSlider && !progressSlider.isValueChanging()) {
                isUpdatingProgressFromMedia = true;
                Platform.runLater(() -> {
                    progressSlider.setValue(newTime.toSeconds());
                    currentTimeLabel.setText(TimeFormatter.formatTime(newTime, mediaPlayer.getTotalDuration()));
                    isUpdatingProgressFromMedia = false;
                });
            } else if (!isSeekingFromSlider) {
                // Update time label even during slider changes
                Platform.runLater(() -> {
                    currentTimeLabel.setText(TimeFormatter.formatTime(newTime, mediaPlayer.getTotalDuration()));
                });
            }
        });

        // Media ready listener - THIS IS WHERE UI STATE CHANGES HAPPEN
        mediaPlayer.setOnReady(() -> {
            Platform.runLater(() -> {
                Duration total = mediaPlayer.getTotalDuration();
                progressSlider.setMax(total.toSeconds());
                totalTimeLabel.setText(TimeFormatter.formatTime(total, total));
                mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);

                // NOW setup the UI based on media type - after MediaPlayer is ready
                if (isVideoFile) {
                    mediaView.setMediaPlayer(mediaPlayer);
                    mediaView.setVisible(true);
                    diskImageView.setVisible(false);
                    mainController.showVideoMode();
                    System.out.println("Video mode activated for: " + fileNameLabel.getText());
                } else {
                    mediaView.setMediaPlayer(null);
                    mediaView.setVisible(false);
                    diskImageView.setVisible(true);
                    mainController.showAudioMode();
                    System.out.println("Audio mode activated for: " + fileNameLabel.getText());
                }

                updatePlaybackRate();
            });
        });

        // Improved progress slider listener to prevent lag
        progressSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (!isUpdatingProgressFromMedia && progressSlider.isValueChanging()) {
                isSeekingFromSlider = true;
            }
        });

        // Handle mouse released on slider for smooth seeking
        progressSlider.setOnMouseReleased(event -> {
            if (isSeekingFromSlider && mediaPlayer != null) {
                double seekTime = progressSlider.getValue();
                mediaPlayer.seek(Duration.seconds(seekTime));
                System.out.println("Seeking to: " + seekTime + " seconds");
            }
            isSeekingFromSlider = false;
        });

        // Handle drag end for touch/drag operations
        progressSlider.setOnMouseDragged(event -> {
            if (mediaPlayer != null && isSeekingFromSlider) {
                // Only seek on major movements to reduce lag
                double currentSliderValue = progressSlider.getValue();
                double currentMediaTime = mediaPlayer.getCurrentTime().toSeconds();

                // Only seek if difference is significant (more than 0.5 seconds)
                if (Math.abs(currentSliderValue - currentMediaTime) > 0.5) {
                    mediaPlayer.seek(Duration.seconds(currentSliderValue));
                }
            }
        });

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newVal.doubleValue() / 100.0);
            }
        });

        mediaPlayer.setOnPlaying(() -> {
            Platform.runLater(() -> {
                if (!isVideoFile) {
                    startDiskSpin();
                }
                mainController.updatePlayPauseButton(true);
                isPlaying = true;
            });
        });

        mediaPlayer.setOnPaused(() -> {
            Platform.runLater(() -> {
                if (!isVideoFile) {
                    stopDiskSpin();
                }
                mainController.updatePlayPauseButton(false);
                isPlaying = false;
            });
        });

        mediaPlayer.setOnEndOfMedia(() -> {
            Platform.runLater(() -> {
                if (!isVideoFile) {
                    stopDiskSpin();
                }
                progressSlider.setValue(progressSlider.getMax());
                mainController.updatePlayPauseButton(false);
                isPlaying = false;
            });
        });

        mediaPlayer.setOnStopped(() -> {
            Platform.runLater(() -> {
                if (!isVideoFile) {
                    stopDiskSpin();
                }
                mainController.updatePlayPauseButton(false);
                isPlaying = false;
            });
        });

        // Error handling
        mediaPlayer.setOnError(() -> {
            System.err.println("Media Player Error: " + mediaPlayer.getError());
            Platform.runLater(() -> {
                fileNameLabel.setText("Error loading: " + fileNameLabel.getText());
                fileNameLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-weight: bold;");
            });
        });
    }

    public void updatePlaybackRate() {
        if (mediaPlayer != null) {
            String speedStr = null;

            if (mainController != null && mainController.getSpeedComboBox() != null) {
                JFXComboBox<String> speedComboBox = mainController.getSpeedComboBox();
                speedStr = speedComboBox.getValue();
            }

            if (speedStr != null && !speedStr.isEmpty()) {
                double rate = 1.0;
                switch (speedStr) {
                    case "0.5x": rate = 0.5; break;
                    case "1x": rate = 1.0; break;
                    case "1.5x": rate = 1.5; break;
                    case "2x": rate = 2.0; break;
                    default: rate = 1.0; break;
                }

                if (rate <= 0) {
                    rate = 1.0;
                }

                mediaPlayer.setRate(rate);

                if (!isVideoFile && diskSpin != null) {
                    diskSpin.setDuration(Duration.seconds(2 / rate));

                    if (diskSpin.getStatus() == javafx.animation.Animation.Status.RUNNING) {
                        diskSpin.stop();
                        diskSpin.play();
                    }
                }
            }
        }
    }

    public void togglePlayPause() {
        if (mediaPlayer != null) {
            MediaPlayer.Status status = mediaPlayer.getStatus();

            if (status == MediaPlayer.Status.STOPPED ||
                    (status == MediaPlayer.Status.PAUSED && progressSlider.getValue() == progressSlider.getMax())) {
                mediaPlayer.seek(Duration.ZERO);
                progressSlider.setValue(0);
                if (!isVideoFile) {
                    resetDiskSpin();
                    startDiskSpin();
                }
                mediaPlayer.play();
                mainController.updatePlayPauseButton(true);
                isPlaying = true;
            }
            else if (progressSlider.getValue() == progressSlider.getMax()) {
                mediaPlayer.seek(Duration.ZERO);
                progressSlider.setValue(0);
                if (!isVideoFile) {
                    resetDiskSpin();
                    startDiskSpin();
                }
                mediaPlayer.play();
                mainController.updatePlayPauseButton(true);
                isPlaying = true;
            }
            else if (status == MediaPlayer.Status.PAUSED) {
                mediaPlayer.play();
                if (!isVideoFile) {
                    startDiskSpin();
                }
                mainController.updatePlayPauseButton(true);
                isPlaying = true;
            }
            else if (status == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                if (!isVideoFile) {
                    stopDiskSpin();
                }
                mainController.updatePlayPauseButton(false);
                isPlaying = false;
            }
            else if (status == MediaPlayer.Status.READY) {
                mediaPlayer.play();
                if (!isVideoFile) {
                    startDiskSpin();
                }
                mainController.updatePlayPauseButton(true);
                isPlaying = true;
            }
        }
    }

    public void resetAndPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.ZERO);
            progressSlider.setValue(0);
            if (!isVideoFile) {
                resetDiskSpin();
                startDiskSpin();
            }
            mediaPlayer.play();
            mainController.updatePlayPauseButton(true);
            isPlaying = true;
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            if (!isVideoFile) {
                stopDiskSpin();
            }
        }
    }

    private void startDiskSpin() {
        if (!isVideoFile) {
            diskSpin.play();
        }
    }

    private void stopDiskSpin() {
        if (!isVideoFile) {
            diskSpin.pause();
        }
    }

    private void resetDiskSpin() {
        if (!isVideoFile) {
            diskSpin.stop();
            diskImageView.setRotate(0);
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isVideoFile() {
        return isVideoFile;
    }
}