/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9_reproductor;

/**
 *
 * @author jenniferbueso
 */
class Music {
    private String musicFilePath;
    private String imageFilePath;

    public Music(String musicFilePath, String imageFilePath) {
        this.musicFilePath = musicFilePath;
        this.imageFilePath = imageFilePath;
    }

    public String getMusicFilePath() {
        return musicFilePath;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Music music = (Music) o;
        return musicFilePath.equals(music.musicFilePath);
    }

    @Override
    public int hashCode() {
        return musicFilePath.hashCode();
    }
}