import java.util.*;

class Song {
    int id;
    String name;
    Songtype songtype;
    boolean isPlaying;
    
    Song(int id, String name, Songtype songtype) {
        this.id = id;
        this.name = name;
        this.songtype = songtype;
        this.isPlaying = false;
    }
    
    @Override
    public String toString() {
        return "Song{id=" + id + ", name='" + name + "', type=" + songtype + "}";
    }
}

enum Songtype {
    POP,
    DHH,
    BOLLYWOOD
}

class SongManager {
    List<Song> ls;
    Queue<Song> song;
    
    SongManager(List<Song> ls, Queue<Song> song) {
        this.ls = ls;
        this.song = song;
    }
    
    SongManager() {
        this.ls = new ArrayList<>();
        this.song = new LinkedList<>();
    }
    
    void addSong(Song s) {
        ls.add(s);
        System.out.println("Added: " + s);
    }
    
    void removeSong(int id) {
        ls.removeIf(s -> s.id == id);
        System.out.println("Removed song with id: " + id);
    }
    
    Song findSongByName(String name) {
        for (Song s : ls) {
            if (s.name.equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }
    
    void addToQueue(Song s) {
        song.offer(s);
        System.out.println("Added to queue: " + s.name);
    }
}

class MusicPlayer {
    SongManager manager;
    Random random;
    
    MusicPlayer() {
        manager = new SongManager();
        random = new Random();
    }
    
    void randomSong() {
        if (manager.ls.isEmpty()) {
            System.out.println("No songs available");
            return;
        }
        int number = random.nextInt(manager.ls.size());
        playSong(number);
    }
    
    void playSongByName(String name) {
        Song song = manager.findSongByName(name);
        if (song != null) {
            song.isPlaying = true;
            System.out.println("Playing: " + song.name);
        } else {
            System.out.println("Song not found: " + name);
        }
    }
    
    void playSongFromQueue() {
        if (manager.song.isEmpty()) {
            System.out.println("Queue is empty");
            return;
        }
        Song song = manager.song.poll();
        song.isPlaying = true;
        System.out.println("Playing from queue: " + song.name);
    }
    
    void playSong(int num) {
        if (manager.ls.isEmpty()) {
            System.out.println("No songs available");
            return;
        }
        
        Song song = manager.ls.get(num);
        song.isPlaying = true;
        System.out.println("Playing: " + song.name);
    }
    
    void displayAllSongs() {
        System.out.println("\n--- All Songs ---");
        for (Song s : manager.ls) {
            System.out.println(s);
        }
    }
}

// Playlist class - fixed
class PlayList {
    int id; 
    String name;
    Queue<Song> q;
    
    PlayList(int id, String name) {
        this.id = id;
        this.name = name;
        this.q = new LinkedList<>();
    }
    
    void addSongToPlaylist(Song s) {
        q.offer(s);
        System.out.println("Added to playlist '" + name + "': " + s.name);
    }
    
    void showPlaylist() {
        System.out.println("\n--- Playlist: " + name + " ---");
        for (Song s : q) {
            System.out.println(s);
        }
    }
}

// User class - fixed
class User {
    int id; 
    String name;
    boolean plus; 
    PlayList playlist;
    
    User(int id, String name) {
        this.id = id;
        this.name = name;
        this.plus = false;
        this.playlist = new PlayList(id, name + "'s Playlist");
    }
    
    void addPlus() {
        plus = true;
        System.out.println(name + " is now a Plus user!");
    }
    
    void addSongToPlaylist(Song s) {
        playlist.addSongToPlaylist(s);
    }
    
    void showPlaylist() {
        playlist.showPlaylist();
    }
}

public class Main {
    public static void main(String[] args) {
        MusicPlayer player = new MusicPlayer();
        
        // Add some songs
        player.manager.addSong(new Song(1, "Shape of You", Songtype.POP));
        player.manager.addSong(new Song(2, "Apna Time Aayega", Songtype.DHH));
        player.manager.addSong(new Song(3, "Tum Hi Ho", Songtype.BOLLYWOOD));
        player.manager.addSong(new Song(4, "Blinding Lights", Songtype.POP));
        player.manager.addSong(new Song(5, "Kala Chashma", Songtype.BOLLYWOOD));
        
        // Display all songs
        player.displayAllSongs();
        
        // Play random song
        System.out.println("\n--- Playing Random Song ---");
        player.randomSong();
        
        // Play song by name
        System.out.println("\n--- Playing Song by Name ---");
        player.playSongByName("Tum Hi Ho");
        
        // Add songs to queue and play from queue
        System.out.println("\n--- Queue Operations ---");
        player.manager.addToQueue(new Song(6, "Kesariya", Songtype.BOLLYWOOD));
        player.manager.addToQueue(new Song(7, "Levitating", Songtype.POP));
        
        player.playSongFromQueue();
        player.playSongFromQueue();
        player.playSongFromQueue(); // Queue empty case
        
        // Remove a song
        System.out.println("\n--- Removing Song ---");
        player.manager.removeSong(2);
        player.displayAllSongs();
        
        // User and playlist demo
        System.out.println("\n--- User Playlist Demo ---");
        User user = new User(1, "Ayush");
        user.addPlus();
        user.addSongToPlaylist(new Song(8, "Perfect", Songtype.POP));
        user.addSongToPlaylist(new Song(9, "Zinda", Songtype.BOLLYWOOD));
        user.showPlaylist();
    }
}
