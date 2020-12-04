package jmcmusicplayer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Name: Reece Pieri
 * ID: M087496
 * Date: 30/11/2020
 * Course: Java III
 * Assessment: AT3 Project
 */
public class JMCLogin extends Application {

    private JMCLogin login;
    private JMCLoginController controller;
    private Socket socket;
    public BufferedReader inStream;
    public DataOutputStream outStream;
    public boolean listening = false;
    private String username;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    // Load GUI
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("JMCLogin.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        controller.login = this;
        controller.stage = stage;
        
        stage.setTitle("JMC Music Player - Login");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            System.exit(0);
        });
        stage.show();
    }
    
    // Login to music player
    public void login(String ip, String username, String password) {
        this.username = username;
        String message = "/login " + username + " " + password;
        connectToServer(ip, message);
    }
    
    // Create a new account
    public void signUp(String ip, String username, String password) {
        String message = "/signup " + username + " " + password;
        connectToServer(ip, message);
    }
    
    // Connect to the server and send request
    private void connectToServer(String ip, String message) {
        try {
            updateStatus("");
            socket = new Socket(ip, 3001);
            inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outStream = new DataOutputStream(socket.getOutputStream());
            startListener();
            sendToServer(message);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            updateStatus("Unable to connect to server.");
        }
    }
    
    // Start listener to receive data from server
    private void startListener() {
        Listener listener = new Listener(this);
        Thread thread = new Thread(listener);
        thread.start();
    }
    
    // Send message to server
    public void sendToServer(String message) {
        try {
            outStream.writeBytes(message + "\r\n");
            outStream.flush();
        } catch (IOException e) {

        }
    }
    
    // Update status label
    public void updateStatus(String message) {
        Platform.runLater(() -> {
            controller.lblStatus.setText(message);
        });
    }
    
    // Load music player
    public void loadMusicPlayer() {
        Platform.runLater(() -> {
            JMCMusicPlayer client = new JMCMusicPlayer(username);
            controller.stage.hide();
        });
    }
    
    // Disconnect from server
    public void disconnect() {
        sendToServer("/disconnect");
        listening = false;
        try {
            socket.close();
        } catch (IOException | NullPointerException e) {
            System.out.println(e);
        }
    }
}
