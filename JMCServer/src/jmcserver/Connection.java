package jmcserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Name: Reece Pieri
 * ID: M087496
 * Date: 30/11/2020
 * Course: Java III
 * Assessment: AT3 Project
 */
public class Connection extends Thread {
    private static int count = 0;
    private final JMCServer server;
    public Socket clientSocket;
    private BufferedReader inStream;
    private DataOutputStream outStream;
    public boolean listening = true;
    
    public Connection(JMCServer server, JMCServerController controller, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
        count++;
        controller.appendMessage("Connection received. #" + count);
    }
    
    // Action to take when thread is started
    @Override
    public void run() {
        try {
            inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outStream = new DataOutputStream(clientSocket.getOutputStream());
            listen();
        } catch (IOException e) {

        }
    }
    
    // Listen for incoming messages from client
    private void listen() {
        while (listening == true) {
            if (!clientSocket.isClosed() && inStream != null) {
                try {
                    String line;
                    
                    if (!(line = inStream.readLine()).isEmpty()) {
                        // Listen for disconnect
                        if (line.startsWith("/disconnect")) {
                            break;
                        }
                        
                        String uname = line.split(" ")[1];
                        String pword = line.split(" ")[2];
                        
                        // Listen for user login
                        if (line.startsWith("/login")) {
                            server.verifyCredentials(this, uname, pword);
                        }
                        
                        // Listen for user sign up
                        if (line.startsWith("/signup")) {
                            server.createUser(this, uname, pword);
                        }
                    }
                } catch (IOException e) {
                    listening = false;
                }
            } else {
                listening = false;
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    
                }
            }
        }
    }
    
    // Send message to client
    public void sendToClient(String message) {
        try {
            outStream.writeBytes(message + "\r\n");
            outStream.flush();
        } catch (IOException e) {

        }
    }
}
