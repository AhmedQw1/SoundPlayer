package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.application.Platform;

import java.io.*;
import java.util.List;

public class PlaylistController {
    private ObservableList<String> mediaNames = FXCollections.observableArrayList();
    private ObservableList<File> mediaFiles = FXCollections.observableArrayList();
    private ListView<String> mediaListView;
    private SoundPlayerController mainController;
    private int currentlyPlayingIndex = -1;

    private static final String MEDIA_LIST_FILE = System.getProperty("user.home") + File.separator + ".soundplayerv1_media.txt";

    public PlaylistController(SoundPlayerController mainController, ListView<String> mediaListView) {
        this.mainController = mainController;
        this.mediaListView = mediaListView;

        loadMediaList();
        setupMediaListView();
    }

    private void setupMediaListView() {
        mediaListView.setItems(mediaNames);

        mediaListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    private final Button deleteButton = new Button("✖");
                    private final HBox hbox = new HBox();
                    private final Label nameLabel = new Label();
                    private final Label typeLabel = new Label();

                    {
                        deleteButton.getStyleClass().add("delete-button");
                        deleteButton.setOnAction(event -> {
                            int index = getIndex();
                            if (index >= 0 && index < mediaNames.size()) {
                                deleteMedia(index);
                            }
                            event.consume();
                        });

                        hbox.setAlignment(Pos.CENTER_LEFT);
                        hbox.setSpacing(10);
                        nameLabel.setMaxWidth(Double.MAX_VALUE);
                        HBox.setHgrow(nameLabel, Priority.ALWAYS);

                        typeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #888;");
                        typeLabel.setMinWidth(40);

                        hbox.getChildren().addAll(nameLabel, typeLabel, deleteButton);
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            nameLabel.setText(item);

                            // Show media type
                            int index = getIndex();
                            if (index >= 0 && index < mediaFiles.size()) {
                                String fileName = mediaFiles.get(index).getName().toLowerCase();
                                if (fileName.endsWith(".mp4") || fileName.endsWith(".avi") ||
                                        fileName.endsWith(".mov") || fileName.endsWith(".mkv") ||
                                        fileName.endsWith(".wmv") || fileName.endsWith(".flv") ||
                                        fileName.endsWith(".webm")) {
                                    typeLabel.setText("📹");
                                } else {
                                    typeLabel.setText("🎵");
                                }
                            }

                            setGraphic(hbox);
                            setText(null);
                        }
                    }
                };
            }
        });

        mediaListView.setOnMouseClicked(event -> {
            int index = mediaListView.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                playAtIndex(index);
            }
        });
    }

    public void playAtIndex(int index) {
        if (index >= 0 && index < mediaFiles.size()) {
            File file = mediaFiles.get(index);
            currentlyPlayingIndex = index;

            // Update the UI selection to match the currently playing track
            updatePlaylistSelection(index);

            mainController.getMediaController().playFile(file);
            System.out.println("Playing: " + file.getName() + " (Index: " + index + ")");
        }
    }

    public void playNext() {
        if (mediaFiles.isEmpty()) {
            System.out.println("No media files to play next");
            return;
        }

        int nextIndex = currentlyPlayingIndex + 1;
        if (nextIndex >= mediaFiles.size()) {
            nextIndex = 0; // Loop to beginning
        }

        System.out.println("Next pressed - Playing index: " + nextIndex);
        playAtIndex(nextIndex);
    }

    public void playPrevious() {
        if (mediaFiles.isEmpty()) {
            System.out.println("No media files to play previous");
            return;
        }

        int prevIndex = currentlyPlayingIndex - 1;
        if (prevIndex < 0) {
            prevIndex = mediaFiles.size() - 1; // Loop to end
        }

        System.out.println("Previous pressed - Playing index: " + prevIndex);
        playAtIndex(prevIndex);
    }

    /**
     * Updates the playlist UI selection to highlight the currently playing track
     */
    private void updatePlaylistSelection(int index) {
        Platform.runLater(() -> {
            if (index >= 0 && index < mediaNames.size()) {
                mediaListView.getSelectionModel().select(index);
                mediaListView.scrollTo(index); // Auto-scroll to the selected item
                System.out.println("Playlist selection updated to index: " + index);
            }
        });
    }

    /**
     * Get the name of the currently playing track
     */
    public String getCurrentTrackName() {
        if (currentlyPlayingIndex >= 0 && currentlyPlayingIndex < mediaNames.size()) {
            return mediaNames.get(currentlyPlayingIndex);
        }
        return "No track playing";
    }

    /**
     * Check if there's a next track available
     */
    public boolean hasNext() {
        return !mediaFiles.isEmpty() && (currentlyPlayingIndex < mediaFiles.size() - 1 || mediaFiles.size() > 1);
    }

    /**
     * Check if there's a previous track available
     */
    public boolean hasPrevious() {
        return !mediaFiles.isEmpty() && (currentlyPlayingIndex > 0 || mediaFiles.size() > 1);
    }

    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Media Files",
                        "*.mp3", "*.wav", "*.mp4", "*.avi", "*.mov", "*.mkv", "*.wmv", "*.flv", "*.webm"),
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav"),
                new FileChooser.ExtensionFilter("Video Files",
                        "*.mp4", "*.avi", "*.mov", "*.mkv", "*.wmv", "*.flv", "*.webm")
        );
        File file = fileChooser.showOpenDialog(mediaListView.getScene().getWindow());

        if (file != null) {
            boolean alreadyExists = mediaFiles.stream()
                    .anyMatch(f -> f.getAbsolutePath().equalsIgnoreCase(file.getAbsolutePath()));

            if (!alreadyExists) {
                mediaFiles.add(file);
                mediaNames.add(file.getName());
                int newIndex = mediaNames.size() - 1;
                playAtIndex(newIndex); // This will automatically update the selection
                saveMediaList();
            } else {
                int existingIndex = getFileIndex(file);
                playAtIndex(existingIndex); // This will automatically update the selection
            }
        }
    }

    /**
     * Get the index of a specific file in the playlist
     */
    private int getFileIndex(File file) {
        for (int i = 0; i < mediaFiles.size(); i++) {
            if (mediaFiles.get(i).getAbsolutePath().equalsIgnoreCase(file.getAbsolutePath())) {
                return i;
            }
        }
        return -1;
    }

    public void deleteMedia(int index) {
        if (index == currentlyPlayingIndex) {
            mainController.getMediaController().stop();
            mainController.updatePlayPauseButton(false);
            currentlyPlayingIndex = -1;
            mainController.updateFileNameLabel("No file loaded");
            mainController.resetProgressUI();
        } else if (index < currentlyPlayingIndex) {
            currentlyPlayingIndex--;
            // Update selection if we're still playing something
            if (currentlyPlayingIndex >= 0) {
                updatePlaylistSelection(currentlyPlayingIndex);
            }
        }

        mediaFiles.remove(index);
        mediaNames.remove(index);

        saveMediaList();
        mainController.updateFileNameLabel("Media removed from playlist");
    }

    private void saveMediaList() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEDIA_LIST_FILE))) {
            for (File file : mediaFiles) {
                bw.write(file.getAbsolutePath());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving media list: " + e.getMessage());
        }
    }

    private void loadMediaList() {
        mediaFiles.clear();
        mediaNames.clear();
        File file = new File(MEDIA_LIST_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                File f = new File(line);
                if (f.exists()) {
                    mediaFiles.add(f);
                    mediaNames.add(f.getName());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading media list: " + e.getMessage());
        }
    }

    public int getCurrentlyPlayingIndex() {
        return currentlyPlayingIndex;
    }

    public ObservableList<String> getMediaNames() {
        return mediaNames;
    }

    public ObservableList<File> getMediaFiles() {
        return mediaFiles;
    }

    public void clearRecentFiles() {
        mediaNames.clear();
        mediaFiles.clear();
        currentlyPlayingIndex = -1;
        mainController.getMediaController().stop();
        mainController.updatePlayPauseButton(false);
        mainController.updateFileNameLabel("Recent files cleared");
        mainController.resetProgressUI();
        saveMediaList(); // Save the empty list
        System.out.println("Recent files cleared");
    }
}