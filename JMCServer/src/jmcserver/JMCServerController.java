package jmcserver;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Name: Reece Pieri
 * ID: M087496
 * Date: 30/11/2020
 * Course: Java III
 * Assessment: AT3 Project
 */
public class JMCServerController {
    public Stage stage;
    public JMCServer server;
    @FXML private TextField textUsername;
    @FXML private Label lblStatus;
    @FXML private TextArea textareaMessages;
    
    // Start button function
    public void btnStart_OnAction(ActionEvent event) {
        server.startServer();
    }
    
    // Stop button function
    public void btnStop_OnAction(ActionEvent event) {
        server.stopServer();
    }
    
    // Remove User button function
    public void btnRemoveUser_OnAction(ActionEvent event) {
        if (textUsername.getText().equals("")) {
            appendMessage("Enter a username.");
        } else {
            server.removeUser(textUsername.getText());
        }
    }
    
    // Update server status
    public void updateServerStatus(String status) {
        if (status.equals("ONLINE")) {
            lblStatus.setStyle("-fx-text-fill: green");
        } else {
            lblStatus.setStyle("-fx-text-fill: red");
        }
        lblStatus.setText(status);
    }
    
    // Append message to message window
    public void appendMessage(String message) {
        textareaMessages.appendText(message + "\n");
    }
    
    // Clear username field
    public void clearUsername() {
        textUsername.setText("");
    }
}
