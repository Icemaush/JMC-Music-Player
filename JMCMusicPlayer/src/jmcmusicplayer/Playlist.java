package jmcmusicplayer;

import java.util.LinkedList;

/**
 * Name: Reece Pieri
 * ID: M087496
 * Date: 30/11/2020
 * Course: Java III
 * Assessment: AT3 Project
 */
public class Playlist {
    private String name;
    private String path = null;
    private final LinkedList<Song> playlist = new LinkedList();
    private boolean sorted = false;
    
    public Playlist(String name) {
        this.name = name;
    }
    
    // Add song to playlist
    public void addSong(Song song) {
        playlist.add(song);
    }
    
    // Remove song from playlist
    public void removeSong(Song song) {
        playlist.remove(song);
    }
    
    // Clear all songs from playlist
    public void clearSongs() {
        playlist.clear();
    }
    
    // Get playlist
    public LinkedList<Song> getPlaylist() {
        return playlist;
    }
    
    // Get name
    public String getName() {
        return name;
    }
    
    // Set name
    public void setName(String name) {
        this.name = name;
    }
    
    // Get path
    public String getPath() {
        return path;
    }
    
    // Set path
    public void setPath(String path) {
        this.path = path;
    }
    
    // Get sorted
    public boolean getSorted() {
        return sorted;
    }
    
    // Set sorted
    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }
}
