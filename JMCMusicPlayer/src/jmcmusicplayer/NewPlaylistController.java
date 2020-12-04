package jmcmusicplayer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Name: Reece Pieri
 * ID: M087496
 * Date: 30/11/2020
 * Course: Java III
 * Assessment: AT3 Project
 */
public class NewPlaylistController {
    public JMCMusicPlayer jmcMusicPlayer;
    public NewPlaylist newPlaylist;
    
    @FXML public TextField textName;
    @FXML public Button btnOK;
    
    // OK button function
    public void btnOK_OnAction(ActionEvent event) {
        
        if (textName.getText().matches("[A-Za-z0-9 ]+")) {
            jmcMusicPlayer.createNewPlaylist(textName.getText());
            newPlaylist.stage2.close();
        }
    }
    
}
