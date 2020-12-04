package jmcserver;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
public class JMCServer extends Application {
    private JMCServerController controller;
    private ServerSocket serverSocket;
    private int port = 3001;
    private boolean listening;
    private List<User> users = new ArrayList<User>();
    
    public static void main(String[] args) {
        launch(args);
    }

    // Load GUI
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("JMCServer.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        controller.server = this;
        controller.stage = stage;
        
        stage.setTitle("JMC Server");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            stopServer();
            Platform.exit();
        });
        stage.show();
    }
    
    // Start server
    public void startServer() {
        try {
            loadUsers();
            serverSocket = new ServerSocket(port);
            controller.updateServerStatus("ONLINE");
            controller.appendMessage("Server started. Listening on port: " + port);
            Listener listener = new Listener(this);
            listener.start();
        } catch (IOException e) {

        }
    }
    
    // Stop server
    public void stopServer() {
        try {
            saveUsers();
            serverSocket.close();
            listening = false;
            controller.updateServerStatus("OFFLINE");
            controller.appendMessage("Server stopped.\n----------");
        } catch (Exception e) {

        }
    }
    
    // Listener class to listen for incoming connections
    class Listener extends Thread {
        private final JMCServer server;
        
        public Listener(JMCServer server) {
            this.server = server;
        }
        
        @Override
        public void run() {
            listening = true;
            while (listening) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    Connection connection = new Connection(server, controller, clientSocket);
                    connection.start();
                } catch (Exception e) {

                }
            }
        }
    }
    
    // Save users to file
    private void saveUsers() {
        try (FileOutputStream fileOut = new FileOutputStream("users.bin")) {
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(users);
            controller.appendMessage("Users saved to file.");
        } catch (IOException e) {
            controller.appendMessage("Unable to save users to file.");
        }
    }
    
    // Load users from file
    private void loadUsers() {
        try (FileInputStream fileIn = new FileInputStream("users.bin")) {
            ObjectInputStream objIn = new ObjectInputStream(fileIn);
            users = (ArrayList<User>)objIn.readObject();
            controller.appendMessage("Users loaded.");
        } catch (IOException | ClassNotFoundException e) {
            controller.appendMessage("Unable to load users from file.");
        }
    }
    
    // Check if user exists
    private User findUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    
    // Remove a user
    public void removeUser(String username) {
        if (users.remove(findUser(username))) {
            controller.clearUsername();
            controller.appendMessage("Account removed: [" + username + "]");
        } else {
            controller.appendMessage("User [" + username + "] does not exist.");
        }
    }
    
    // Create a user
    public void createUser(Connection connection, String username, String password) {
        User user = findUser(username);
        if (user == null) {
            user = new User(username, password);
            users.add(user);
            controller.appendMessage("Account created: [" + username + "]");
            connection.sendToClient("Account created! Please login with your new account.");
        } else {
            connection.sendToClient("A user with that username already exists.");
        }
    }
    
    // Verify user credentials
    public void verifyCredentials(Connection connection, String username, String password) {
        User user = findUser(username);
        if (user != null) {
            if (user.verifyPassword(password)) {
                acceptLogin(connection);
            } else {
                refuseLogin(connection, "Incorrect password.");
            }
        } else {
            refuseLogin(connection, "User does not exist.");
        }
    }
    
    // Accept user login
    private void acceptLogin(Connection connection) {
        connection.sendToClient("/accept");
        connection.listening = false;
    }
    
    // Refuse user login
    private void refuseLogin(Connection connection, String message) {
        connection.sendToClient(message);
    }
}
