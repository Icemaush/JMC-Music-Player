package jmcmusicplayer;

/**
 * Name: Reece Pieri
 * ID: M087496
 * Date: 30/11/2020
 * Course: Java III
 * Assessment: AT3 Project
 */
public class Song {
    private final String name;
    private final String path;
    
    public Song(String name, String path) {
        this.name = name;
        this.path = path;
    }
    
    // Get name
    public String getName() {
        return name;
    }
    
    // Get path
    public String getPath() {
        return path;
    }
}
