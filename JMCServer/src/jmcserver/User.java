package jmcserver;

import java.io.Serializable;

/**
 * Name: Reece Pieri
 * ID: M087496
 * Date: 30/11/2020
 * Course: Java III
 * Assessment: AT3 Project
 */
public class User implements Serializable {
    private final String username;
    private final String salt;
    private String passwordHash;
    private final Password password = new Password();
    
    public User(String username, String password) {
        this.username = username;
        salt = this.password.generateSalt();
        passwordHash = this.password.hashPassword(password, salt);
    }
    
    // Verify user password 
    public boolean verifyPassword(String password) {
        return passwordHash.equals(this.password.hashPassword(password, salt));
    }
    
    // Change user password
    public void changePassword(String password) {
        passwordHash = this.password.hashPassword(password, salt);
    }

    // Get username
    public String getUsername() {
        return username;
    }
    
    
}
