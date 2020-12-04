package jmcmusicplayer;

import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

/**
 * Name: Reece Pieri
 * ID: M087496
 * Date: 30/11/2020
 * Course: Java III
 * Assessment: AT3 Project
 */
public class JMCLoginController {
    public JMCLogin login;
    public Stage stage;
    @FXML private TextField textServerIP;
    @FXML private TextField textUsernameLogin;
    @FXML private TextField textUsernameSignUp;
    @FXML private PasswordField pwdLogin;
    @FXML private PasswordField pwdSignUp;
    @FXML public Label lblStatus;
    
    // Login button function
    public void btnLogin_OnClick(ActionEvent event) {
        if (checkServerIP()) {
            if (!textUsernameLogin.getText().isBlank() && !pwdLogin.getText().isBlank()) {
                login.login(textServerIP.getText(), textUsernameLogin.getText(), pwdLogin.getText());
            } else {
                login.updateStatus("Enter username and password.");
            }
        } else {
            login.updateStatus("Enter server IP address.");
        }
    }
    
    // SignUp button function
    public void btnSignUp_OnClick(ActionEvent event) {
        if (checkServerIP()) {
            if (!textUsernameSignUp.getText().isBlank() && !pwdSignUp.getText().isBlank()) {
                login.signUp(textServerIP.getText(), textUsernameSignUp.getText(), pwdSignUp.getText());
            } else {
                login.updateStatus("Enter username and password.");
            }    
        } else {
            login.updateStatus("Enter server IP address.");
        }
    }
    
    // Check for server IP address
    private boolean checkServerIP() {
        return !textServerIP.getText().isBlank();
    }
}
