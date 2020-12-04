package jmcmusicplayer;

import java.io.File;
import java.util.Map;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import streamplayer.*;
import visualizer.Visualizer;

/**
 * Name: Reece Pieri
 * ID: M087496
 * Date: 30/11/2020
 * Course: Java III
 * Assessment: AT3 Project
 */
public class Player extends StackPane implements StreamPlayerListener {
    public JMCMusicPlayer jmcMusicPlayer;
    public Label lblNowPlaying;
    Visualizer visualizer = new Visualizer("Example Visualizer");
    StreamPlayer streamPlayer = new StreamPlayer();

    public Player() {
        setStyle("-fx-background-color:black; -fx-border-color:#454d54;");
        setPrefSize(335, 300);
        setLayoutX(13);
        setLayoutY(73);

        lblNowPlaying = new Label();
        lblNowPlaying.setFont(new Font("System", 14.0));
        lblNowPlaying.setTextFill(Color.WHITE);
        setAlignment(lblNowPlaying, Pos.BOTTOM_CENTER);
        
        getChildren().add(visualizer);
        getChildren().add(lblNowPlaying);

        // Add the Listener to the Player
        streamPlayer.addStreamPlayerListener(this);
    }
    
    // Play song
    public void playSong(File file) {
        try {
            // ----------------------Open the Media
            System.out.println("Opening ...");
            streamPlayer.open(file);

            // ---------------------- Play the Media
            System.out.println("Starting to play ...");
            streamPlayer.play();

            visualizer.setShowFPS(false);

        } catch (StreamPlayerException e) {
            e.printStackTrace();
        }
    }

    // Pause song
    public void pauseSong() {
        streamPlayer.pause();
    }

    // Resume song
    public void resumeSong() {
        streamPlayer.resume();
    }

    // Stop song
    public void stopSong() {
        streamPlayer.stop();
    }

    // As the song progresses, write data to the visualizer
    @Override
    public void progress(int nEncodedBytes, long microsecondPosition, 
            byte[] pcmData, Map<String, Object> properties) {
            // write the data to the visualizer
            visualizer.writeDSP(pcmData);
    }

    // Status updates
    @Override
    public void statusUpdated(StreamPlayerEvent event) {
        System.out.println("Player Status is:" + streamPlayer.getStatus());

        // player is opened
        if (event.getPlayerStatus() == Status.OPENED && streamPlayer.getSourceDataLine() != null) {

            visualizer.setupDSP(streamPlayer.getSourceDataLine());
            visualizer.startDSP(streamPlayer.getSourceDataLine());

            Platform.runLater(() -> {
                visualizer.startVisualizerPainter();
                jmcMusicPlayer.updateNowPlaying(jmcMusicPlayer.currentlyPlaying);
            });

        // player is stopped
        } else if (event.getPlayerStatus() == Status.STOPPED) {
            visualizer.stopDSP();

            Platform.runLater(() -> {
                visualizer.stopVisualizerPainter();
                jmcMusicPlayer.updateNowPlaying("");
            });
            jmcMusicPlayer.currentlyPlaying = null;
        }
    }

    @Override
    public void opened(Object dataSource, Map<String, Object> properties) {
        
    }
}
