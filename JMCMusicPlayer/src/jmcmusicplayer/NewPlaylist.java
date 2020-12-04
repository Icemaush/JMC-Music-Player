package jmcmusicplayer;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Name: Reece Pieri
 * ID: M087496
 * Date: 30/11/2020
 * Course: Java III
 * Assessment: AT3 Project
 */
public class NewPlaylist {
    private final JMCMusicPlayer jmcMusicPlayer;
    private NewPlaylistController controller;
    public Stage stage2;
    
    public NewPlaylist(JMCMusicPlayer jmcMusicPlayer, Stage stage) {
        this.jmcMusicPlayer = jmcMusicPlayer;
        createNewPlaylistWindow(stage);
    }
    
    // Create new playlist window
    private void createNewPlaylistWindow(Stage stage) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewPlaylist.fxml"));
            root = loader.load();
            controller = loader.getController();
            controller.jmcMusicPlayer = jmcMusicPlayer;
            controller.newPlaylist = this;

            stage2 = new Stage();
            stage2.setTitle("New Playlist");
            stage2.setScene(new Scene(root));
            stage2.initModality(Modality.APPLICATION_MODAL);
            stage2.initOwner(stage);
            stage2.setOnCloseRequest(event -> {
                stage2.close();
            });
            stage2.showAndWait();
        }
        catch (IOException e) {

        }
    }

}
