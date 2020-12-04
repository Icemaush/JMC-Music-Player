package jmcmusicplayer;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.RFC4180Parser;
import com.opencsv.exceptions.CsvValidationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.filechooser.FileSystemView;

/**
 * Name: Reece Pieri
 * ID: M087496
 * Date: 30/11/2020
 * Course: Java III
 * Assessment: AT3 Project
 */
public class JMCMusicPlayer {
    private JMCMusicPlayerController controller;
    public Stage stage;
    private Player player;
    public Playlist currentPlaylist;
    public boolean isPlaying = false;
    public boolean autoplay = false;
    public String currentlyPlaying; 
    
    public JMCMusicPlayer(String username) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("JMCMusicPlayer.fxml"));
            root = loader.load();
            controller = loader.getController();
            controller.jmcMusicPlayer = this;
            controller.setupGUI();
            player = controller.player;
            player.jmcMusicPlayer = this;
            stage = new Stage();
            stage.setTitle("JMC Music Player - " + username);
            stage.setResizable(false);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setOnCloseRequest(event -> System.exit(0));
            stage.show();
        }
        catch (IOException e) {

        }
    }
    
    // Display new playlist dialog
    public void newPlaylistDialog() {
        NewPlaylist newPlaylist = new NewPlaylist(this, stage);
    }
    
    // Create new playlist
    public void createNewPlaylist(String name) {
        stopSong();
        currentPlaylist = new Playlist(name);
        updatePlaylistName();
        displaySongs();
    }
    
    // Save playlist information in file
    public void savePlaylist() {
        if (currentPlaylist != null) {
            if (currentPlaylist.getPath() == null) {
                savePlaylistAs();
            } else {
                try (PrintWriter writer = new PrintWriter(currentPlaylist.getPath())) {
                    for (Song song : currentPlaylist.getPlaylist()) {
                        writer.write(song.getName() + "," + song.getPath() + "\n");
                    }
                    
                } catch (IOException e) {

                }
            }
        } else {
            displayAlert("Error", "Unable to save playlist. No playlist loaded.");
        }
    }
    
    // Save playlist as...
    public void savePlaylistAs() {
        if (currentPlaylist != null) {
            FileChooser saveFile = new FileChooser();
            saveFile.setTitle("Save Playlist");
            saveFile.setInitialDirectory(new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath()));
            saveFile.getExtensionFilters().add(new ExtensionFilter("CSV Files", "*.csv"));
            saveFile.setInitialFileName(currentPlaylist.getName());
            
            File file = saveFile.showSaveDialog(stage);
            if (file != null) {
                currentPlaylist.setName(file.getName());
                currentPlaylist.setPath(file.getPath());
                try (PrintWriter writer = new PrintWriter(file.getPath())) {
                    for (Song song : currentPlaylist.getPlaylist()) {
                        writer.write(song.getName() + "," + song.getPath() + "\n");
                    }
                } catch (IOException e) {

                }
            }
        } else {
            displayAlert("Error", "Unable to save playlist. No playlist loaded.");
        }
    }
    
    // Load a playlist
    public void loadPlaylist() {
        FileChooser openFile = new FileChooser();
        openFile.setTitle("Load Playlist");
        openFile.setInitialDirectory(new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath()));
        openFile.getExtensionFilters().add(new ExtensionFilter("CSV Files", "*.csv"));
        
        File file = openFile.showOpenDialog(stage);
        if (file != null) {
            stopSong();
            currentPlaylist = new Playlist(file.getName());
            currentPlaylist.setPath(file.getPath());

            try {
                RFC4180Parser parser = new RFC4180Parser();
                CSVReaderBuilder csvReaderBuilder = new CSVReaderBuilder(new FileReader(currentPlaylist.getPath())).withCSVParser(parser);
                CSVReader reader = csvReaderBuilder.build();
                
                String[] line;
                while((line = reader.readNext()) != null) {
                    if (line.length > 1) {
                        currentPlaylist.addSong(new Song(line[0], line[1]));
                    }
                }
                
                updatePlaylistName();
                displaySongs();
                controller.listPlaylist.getSelectionModel().select(0);
                playSong();
            } catch (CsvValidationException | IOException e) {

            }
        }
    }
    
    // Add song to playlist
    public void addSong() {
        boolean duplicates = false;
        List<String> duplicatesList = new ArrayList<>();
        String duplicatesString = "";
        List<Song> addList = new ArrayList<>();
        
        if (currentPlaylist == null) {
            newPlaylistDialog();
        }
        
        FileChooser openFile = new FileChooser();
        openFile.setTitle("Add Song");
        openFile.setInitialDirectory(new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath()));
        openFile.getExtensionFilters().add(new ExtensionFilter("MP3 Files", "*.mp3"));
        List<File> files = openFile.showOpenMultipleDialog(stage);
        for (File file : files) {
            if (file != null) {
                if (currentPlaylist.getPlaylist().size() > 0) {
                    for (Song song : currentPlaylist.getPlaylist()) {
                        if (song.getName().equals(file.getName().split("[.]")[0])) {
                            duplicatesList.add(song.getName());
                            duplicates = true;
                            break;
                        }
                    }
                }
            }
        }
        
        for (File file : files) {
            if (!duplicatesList.contains(file.getName().split("[.]")[0])) {
                currentPlaylist.addSong(new Song(file.getName().split("[.]")[0], file.getPath()));
            }
        }
        
        currentPlaylist.setSorted(false);
        displaySongs();
        
        if (duplicates) {
            for (String songName : duplicatesList) {
                duplicatesString += songName + "\n";
            }
            displayAlert("Duplicates Found", "The following " + duplicatesList.size() + " songs already exist in the\ncurrent playlist and have not been added:\n\n" + duplicatesString);
        }
    }
    
    // Remove song from playlist
    public void removeSong() {
        int selectedIndex = controller.listPlaylist.getSelectionModel().getSelectedIndex();
        if (currentPlaylist != null && selectedIndex != -1) {
            if (currentlyPlaying.equals(controller.listPlaylist.getItems().get(selectedIndex))) {
                stopSong();
            }
            currentPlaylist.getPlaylist().remove(controller.listPlaylist.getSelectionModel().getSelectedIndex());
            displaySongs();
        }
    }
    
    // Display message box
    public void displayAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.showAndWait();
    }
    
    // Update playlist name
    private void updatePlaylistName() {
        controller.lblCurrentPlaylist.setText(currentPlaylist.getName().split("[.]")[0]);
    }
    
    // Update Now Playing label
    public void updateNowPlaying(String songName) {
        player.lblNowPlaying.setText(songName);
    }
    
    // Display songs in playlist
    private void displaySongs() {
        controller.listPlaylist.getItems().clear();
        for (Song song : currentPlaylist.getPlaylist()) {
            controller.listPlaylist.getItems().add(song.getName());
        }
    }
    
    // Clear playlist
    public void clearPlaylist() {
        stopSong();
        currentPlaylist.getPlaylist().clear();
        displaySongs();
    }
    
    // Sort playlist
    public void sortPlaylist() {
        Sort sort = new Sort();
        Song[] array = currentPlaylist.getPlaylist().toArray(new Song[currentPlaylist.getPlaylist().size()]);
        currentPlaylist.getPlaylist().clear();
        currentPlaylist.getPlaylist().addAll(Arrays.asList(sort.MergeSort(array)));
        currentPlaylist.setSorted(true);
        displaySongs();
    }
    
    // Search playlist
    public void searchPlaylist(String searchText) {
        
        if (!currentPlaylist.getSorted()) {
            displayAlert("Error", "Sort playlist first.");
            return;
        }
        
        if (!searchText.isBlank()) {
            String target = searchText.toLowerCase();
            int first = 0;
            int last = currentPlaylist.getPlaylist().size() - 1;
            int mid = (first + last) / 2;

            while (first <= last) {
                int res = target.compareTo(currentPlaylist.getPlaylist().get(mid).getName().toLowerCase());
                
                // If target is found, highlight song
                if (res == 0) {
                    controller.listPlaylist.getSelectionModel().select(mid);
                    return;
                }
                
                if (res > 0) {
                    first = mid + 1;
                } else {
                    last = mid - 1;
                }
                mid = (first + last) / 2;
                
            }
            displayAlert("Search", "Unable to find song.");
        }
    }

// PLAYER CONTROLS
// <editor-fold>
    // Play song
    public void playSong() {
        if (currentPlaylist != null && currentPlaylist.getPlaylist().size() > 0) {
            int selectedIndex = controller.listPlaylist.getSelectionModel().getSelectedIndex();
            if (currentlyPlaying == null && selectedIndex == -1) {
                File file = new File(currentPlaylist.getPlaylist().get(0).getPath());
                player.playSong(file);
                isPlaying = true;
                currentlyPlaying = currentPlaylist.getPlaylist().get(0).getName();
                updateNowPlaying(currentlyPlaying);
            } else if (currentlyPlaying == null && selectedIndex != -1) {
                File file = new File(currentPlaylist.getPlaylist().get(selectedIndex).getPath());
                player.playSong(file);
                isPlaying = true;
                currentlyPlaying = currentPlaylist.getPlaylist().get(selectedIndex).getName();
                updateNowPlaying(currentlyPlaying);
            } else if (currentlyPlaying != null && selectedIndex == -1) {
                if (isPlaying) {
                    pauseSong();
                    isPlaying = false;
                } else {
                    player.resumeSong();
                    isPlaying = true;
                }
            } else if (currentlyPlaying != null && selectedIndex != -1) {
                if (currentlyPlaying.equals(currentPlaylist.getPlaylist().get(selectedIndex).getName())) {
                    if (isPlaying) {
                        pauseSong();
                        isPlaying = false;
                    } else {
                        player.resumeSong();
                        isPlaying = true;
                    }
                } else {
                    player.stopSong();
                    File file = new File(currentPlaylist.getPlaylist().get(selectedIndex).getPath());
                    player.playSong(file);
                    isPlaying = true;
                    currentlyPlaying = currentPlaylist.getPlaylist().get(selectedIndex).getName();
                    updateNowPlaying(currentlyPlaying);
                }
            }
        }
    }
    
    // Pause song
    public void pauseSong() {
        player.pauseSong();
        isPlaying = false;
    }
    
    // Stop song
    public void stopSong() {
        autoplay = false;
        player.stopSong();
        currentlyPlaying = "";
        updateNowPlaying(currentlyPlaying);
        isPlaying = false;
    }
    
    // Play previous song
    public void previousSong() {
        if (currentPlaylist != null && currentPlaylist.getPlaylist().size() > 0) {
            if (currentlyPlaying != null) {
                int currentIndex = controller.listPlaylist.getItems().indexOf(currentlyPlaying);

                if (currentIndex > 0) {
                    controller.listPlaylist.getSelectionModel().select(currentIndex - 1);
                    playSong();
                }
            }
        }
    }
    
    // Play next song
    public void nextSong() {
        if (currentPlaylist != null && currentPlaylist.getPlaylist().size() > 0) {
            if (currentlyPlaying != null) {
                int currentIndex = controller.listPlaylist.getItems().indexOf(currentlyPlaying);
        
                if (currentIndex < currentPlaylist.getPlaylist().size() - 1) {
                    controller.listPlaylist.getSelectionModel().select(currentIndex + 1);
                    playSong();
                }
            }
        }
    }
// </editor-fold>
}
