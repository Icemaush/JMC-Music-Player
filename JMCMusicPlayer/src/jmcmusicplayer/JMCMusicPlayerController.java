package jmcmusicplayer;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

/**
 * Name: Reece Pieri
 * ID: M087496
 * Date: 30/11/2020
 * Course: Java III
 * Assessment: AT3 Project
 */
public class JMCMusicPlayerController {
    public JMCMusicPlayer jmcMusicPlayer;
    public Player player;
    @FXML public AnchorPane paneRoot;
    @FXML private MenuItem menuitemNewPlaylist;
    @FXML private MenuItem menuitemLoadPlaylist;
    @FXML private MenuItem menuitemSavePlaylist;
    @FXML private MenuItem menuitemSavePlaylistAs;
    @FXML private MenuItem menuitemClose;
    @FXML private MenuItem menuitemAddSong;
    @FXML private MenuItem menuitemRemoveSong;
    @FXML private MenuItem menuitemClear;
    @FXML private MenuItem menuitemHowToGuide;
    @FXML private MenuItem menuitemAbout;
    @FXML public Label lblCurrentPlaylist;
    @FXML public ListView listPlaylist;
    @FXML private TextField textSearch;
    
    @FXML
    void initialize() {
        player = new Player();
        paneRoot.getChildren().add(player);
    }
    
    // Setup GUI functions
    public void setupGUI() {
        setupPlaylist();
        setupMenuBar();
    }
    
    // Setup playlist double-click function
    public void setupPlaylist() {
        listPlaylist.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    jmcMusicPlayer.playSong();
                }
            }
        });
    }
    
    // Setup menubar functions
    public void setupMenuBar() {
        menuitemNewPlaylist.setOnAction(event -> {
            jmcMusicPlayer.newPlaylistDialog();
        });
        
        menuitemLoadPlaylist.setOnAction(event -> {
            jmcMusicPlayer.loadPlaylist();
        });
        
        menuitemSavePlaylist.setOnAction(event -> {
            jmcMusicPlayer.savePlaylist();
        });
        
        menuitemSavePlaylistAs.setOnAction(event -> {
            jmcMusicPlayer.savePlaylistAs();
        });
        
        menuitemClose.setOnAction(event -> {
            System.exit(0);
        });
        
        menuitemAddSong.setOnAction(event -> {
            jmcMusicPlayer.addSong();
        });
        
        menuitemRemoveSong.setOnAction(event -> {
            jmcMusicPlayer.removeSong();
        });
        
        menuitemClear.setOnAction(event -> {
            jmcMusicPlayer.clearPlaylist();
        });
        
        menuitemHowToGuide.setOnAction(event -> {
            try {
                File file = new java.io.File("./HowToGuide/HowTo.html").getAbsoluteFile();
                Desktop.getDesktop().open(file); 
            } catch (IOException ex) {
                
            }
        });
        
        menuitemAbout.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, 
                    "JMC Music Player\n"
                    + "Created by Reece Pieri (M087496).\n"
                    + "Date: 2/12/2020\n"
                    + "South Metropolitan TAFE\n"
                    + "Diploma in Software Development, Java III - Project");
            alert.setTitle("About");
            alert.setHeaderText(null);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(jmcMusicPlayer.stage);
            alert.showAndWait();
        });
    }
    
    // Play/Pause button function
    public void btnPlayPause_OnClick(ActionEvent event) {
        jmcMusicPlayer.playSong();
    }
    
    // Previous button function
    public void btnPrevious_OnClick(ActionEvent event) {
        jmcMusicPlayer.previousSong();
    }
    
    // Next button function
    public void btnNext_OnClick(ActionEvent event) {
        jmcMusicPlayer.nextSong();
    }
    
    // Sort button function
    public void btnSort_OnClick(ActionEvent event) {
        jmcMusicPlayer.sortPlaylist();
    }
    // Search button function
    public void btnSearch_OnClick(ActionEvent event) {
        jmcMusicPlayer.searchPlaylist(textSearch.getText());
    }
    

    
}
