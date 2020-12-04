package jmcmusicplayer;

import java.io.IOException;

/**
 * Name: Reece Pieri
 * ID: M087496
 * Date: 30/11/2020
 * Course: Java III
 * Assessment: AT3 Project
 */
public class Listener extends Thread {
    private final JMCLogin login;
    
    public Listener(JMCLogin login) {
        this.login = login;
    }
    
    // Listen for incoming messages from server
    @Override
    public void run() {
        login.listening = true;
        String line;
        
        while (login.listening == true) {
            try {
                line = login.inStream.readLine();

                if (line.startsWith("/accept")) {
                    login.disconnect();
                    login.loadMusicPlayer();
                } else {
                    login.updateStatus(line);
                    login.disconnect();
                }
            } catch (IOException e) {
                
            }
        }
    }
    
}
