/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9_reproductor;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author jenniferbueso
 */
public class MusicPlayer {
    private List<String> playlist;
    private List<String> covers;
    private int currentSongIndex;
    private boolean isPlaying;
    private long currentMicroseconds;
    private long totalMicroseconds;
    private Clip clip;
    private long pausedMicroseconds;
    private boolean wasPaused;

    private static final String MUSIC_FOLDER = "Music/";
    private static final String IMAGES_FOLDER = "Images/";

    public MusicPlayer() {
        playlist = new ArrayList<>();
        covers = new ArrayList<>();
        currentSongIndex = 0;
        isPlaying = false;
        currentMicroseconds = 0;
        totalMicroseconds = 0;
        clip = null;
        pausedMicroseconds = 0;
        wasPaused = false;
        
        cargarCaratulas();
    }
    
    public void cargarCaratulas() {
        File imagesFolder = new File(IMAGES_FOLDER);
        if (imagesFolder.exists() && imagesFolder.isDirectory()) {
            File[] imageFiles = imagesFolder.listFiles();
            if (imageFiles != null) {
                for (File file : imageFiles) {
                    covers.add(file.getAbsolutePath());
                }
            }
        }
    }

    public void addSong(String filePath, String coverFileName) {
        File sourceFile = new File(filePath);
        File destinationFolder = new File(MUSIC_FOLDER);

        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }

        File destinationFile = new File(destinationFolder, sourceFile.getName());

        if (!playlist.contains(destinationFile.getAbsolutePath())) {
            try {
                Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                playlist.add(destinationFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String coverPath = IMAGES_FOLDER + coverFileName;
        if (!covers.contains(coverPath)) {
            covers.add(coverPath);
            System.out.println("Added cover: " + coverPath);
        }
    }

    public void selectSong(int index) {
        if (index != currentSongIndex) {
            stop();
            currentSongIndex = index;
        }
    }
    
    public void updateCoverImage(JButton portadaLabel) {
        String coverPath = getCurrentCoverPath();
        if (coverPath != null) {
            ImageIcon icon = new ImageIcon(coverPath);
            Image image = icon.getImage().getScaledInstance(200, 190, Image.SCALE_DEFAULT);
            ImageIcon scaledIcon = new ImageIcon(image);
            portadaLabel.setIcon(scaledIcon);
        }
    }

    public void play() {
        if (!isPlaying && currentSongIndex >= 0 && currentSongIndex < playlist.size()) {
            isPlaying = true;
            String filePath = playlist.get(currentSongIndex);
            try {
                if (clip == null || !clip.isOpen()) { 
                    File file = new File(filePath);
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    totalMicroseconds = clip.getMicrosecondLength();
                }
                if (wasPaused) { 
                    clip.setMicrosecondPosition(pausedMicroseconds);
                } else {
                    clip.setMicrosecondPosition(0); 
                }
                clip.start();

                Timer timer = new Timer(100, e -> {
                    if (clip != null && clip.isRunning()) {
                        currentMicroseconds = clip.getMicrosecondPosition();
                    } else {
                        stop();
                    }
                });
                timer.setRepeats(true);
                timer.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    public String getCurrentCoverPath() {
        if (currentSongIndex >= 0 && currentSongIndex < covers.size()) {
            return covers.get(currentSongIndex);
        }
        return null;
    }
    
    public void pause() {
        if (clip != null && clip.isRunning()) {
            pausedMicroseconds = clip.getMicrosecondPosition();
            clip.stop();
            isPlaying = false;
        }
    }

    public void stop() {
        if (clip != null && clip.isOpen()) {
            clip.stop();
            clip.close();
            clip = null;
        }
        isPlaying = false;
        wasPaused = false;
    }


    public void resume() {
        if (clip != null && !clip.isRunning()) {
            clip.setMicrosecondPosition(pausedMicroseconds);
            clip.start();
            isPlaying = true;
            wasPaused = false;
        }
    }

    public void nextSong() {
        if (currentSongIndex < playlist.size() - 1) {
            currentSongIndex++;
            stop();
            play();
        }
    }

    public void previousSong() {
        if (currentSongIndex > 0) {
            currentSongIndex--;
            stop();
            play();
        }
    }
    
    public long getCurrentMicroseconds() {
        return currentMicroseconds;
    }

    public long getTotalMicroseconds() {
        return totalMicroseconds;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public long getTotalSeconds() {
        return totalMicroseconds / 1_000_000;
    }
}
