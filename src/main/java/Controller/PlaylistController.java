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

import java.io.*;
import java.util.List;

public class PlaylistController {
    private ObservableList<String> soundNames = FXCollections.observableArrayList();
    private ObservableList<File> soundFiles = FXCollections.observableArrayList();
    private ListView<String> soundListView;
    private SoundPlayerController mainController;
    private int currentlyPlayingIndex = -1;

    private static final String SOUND_LIST_FILE = System.getProperty("user.home") + File.separator + ".soundplayerv1_sounds.txt";

    public PlaylistController(SoundPlayerController mainController, ListView<String> soundListView) {
        this.mainController = mainController;
        this.soundListView = soundListView;

        loadSoundList();
        setupSoundListView();
    }

    private void setupSoundListView() {
        soundListView.setItems(soundNames);

        // Create a custom cell factory with delete buttons :)
        soundListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    private final Button deleteButton = new Button("✖");
                    private final HBox hbox = new HBox();
                    private final Label nameLabel = new Label();

                    {
                        // Styles the delete button
                        deleteButton.getStyleClass().add("delete-button");
                        deleteButton.setOnAction(event -> {
                            int index = getIndex();
                            if (index >= 0 && index < soundNames.size()) {
                                deleteSong(index);
                            }
                            event.consume(); // Prevent the click from selecting the row
                        });

                        // Sets up the layout
                        hbox.setAlignment(Pos.CENTER_LEFT);
                        hbox.setSpacing(10);
                        nameLabel.setMaxWidth(Double.MAX_VALUE);
                        HBox.setHgrow(nameLabel, Priority.ALWAYS);

                        hbox.getChildren().addAll(nameLabel, deleteButton);
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            nameLabel.setText(item);
                            setGraphic(hbox);
                            setText(null);
                        }
                    }
                };
            }
        });

        soundListView.setOnMouseClicked(event -> {
            int index = soundListView.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                playAtIndex(index);
            }
        });
    }

    public void playAtIndex(int index) {
        if (index >= 0 && index < soundFiles.size()) {
            File file = soundFiles.get(index);
            currentlyPlayingIndex = index;
            mainController.getMediaController().playFile(file);
        }
    }

    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav")
        );
        File file = fileChooser.showOpenDialog(soundListView.getScene().getWindow());

        if (file != null) {
            boolean alreadyExists = soundFiles.stream()
                    .anyMatch(f -> f.getAbsolutePath().equalsIgnoreCase(file.getAbsolutePath()));

            if (!alreadyExists) {
                soundFiles.add(file);
                soundNames.add(file.getName());
                soundListView.getSelectionModel().select(soundNames.size() - 1);
                playAtIndex(soundNames.size() - 1);
                saveSoundList();
            } else {
                soundListView.getSelectionModel().select(soundFiles.indexOf(file));
            }
        }
    }

    public void deleteSong(int index) {
        // If currently playing song is being deleted stop playback
        if (index == currentlyPlayingIndex) {
            mainController.getMediaController().stop();
            mainController.updatePlayPauseButton(false);
            currentlyPlayingIndex = -1;
            mainController.updateFileNameLabel("No file loaded");
            mainController.resetProgressUI();
        } else if (index < currentlyPlayingIndex) {
            // If deleted song is before the currently playing song, adjust the index
            currentlyPlayingIndex--;
        }

        // Remove the song from lists
        soundFiles.remove(index);
        soundNames.remove(index);

        // Save the updated list
        saveSoundList();

        // Show confirmation message
        mainController.updateFileNameLabel("Song removed from playlist");
    }

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

    public int getCurrentlyPlayingIndex() {
        return currentlyPlayingIndex;
    }

    public ObservableList<String> getSoundNames() {
        return soundNames;
    }

    public ObservableList<File> getSoundFiles() {
        return soundFiles;
    }
}