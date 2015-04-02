/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.brandts.jazzbot.rest.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Teddy
 */
public class CurrentTrackData {

    private String title;
    private String id;
    private String albumName;
    private List<String> artists;

    private String albumCoverUrlLarge;
    private String albumCoverUrlMedium;
    private String albumCoverUrlSmall;

    private String previewUrl;

    private int duration;
    private int position;
    private boolean playing;

    private int volume;
    
    public CurrentTrackData() {
        artists = new ArrayList<String>();

    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the albumName
     */
    public String getAlbumName() {
        return albumName;
    }

    /**
     * @param albumName the albumName to set
     */
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    /**
     * @return the artists
     */
    public List<String> getArtistsList() {

        return artists;
    }

    public String getArtists() {
        StringBuilder sb = new StringBuilder();

        for (String artist : artists) {
            sb.append(artist);
            sb.append(", ");
        }
        sb.setLength(sb.length() - 2);

        return sb.toString();
    }

    /**
     * @param artists the artists to set
     */
    public void addArtists(String artists) {
        this.artists.add(artists);
    }

    /**
     * @return the albumCoverUrlLarge
     */
    public String getAlbumCoverUrlLarge() {
        return albumCoverUrlLarge;
    }
    
    

    /**
     * @param albumCoverUrlLarge the albumCoverUrlLarge to set
     */
    public void setAlbumCoverUrlLarge(String albumCoverUrlLarge) {
        this.albumCoverUrlLarge = albumCoverUrlLarge;
    }

    /**
     * @return the albumCoverUrlMedium
     */
    public String getAlbumCoverUrlMedium() {
        return albumCoverUrlMedium;
    }

    /**
     * @param albumCoverUrlMedium the albumCoverUrlMedium to set
     */
    public void setAlbumCoverUrlMedium(String albumCoverUrlMedium) {
        this.albumCoverUrlMedium = albumCoverUrlMedium;
    }

    /**
     * @return the albumCoverUrlSmall
     */
    public String getAlbumCoverUrlSmall() {
        return albumCoverUrlSmall;
    }

    /**
     * @param albumCoverUrlSmall the albumCoverUrlSmall to set
     */
    public void setAlbumCoverUrlSmall(String albumCoverUrlSmall) {
        this.albumCoverUrlSmall = albumCoverUrlSmall;
    }

    /**
     * @return the sampleUrl
     */
    public String getPreviewUrl() {
        return previewUrl;
    }

    /**
     * @param sampleUrl the sampleUrl to set
     */
    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @return the playing
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * @param playing the playing to set
     */
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    /**
     * @return the volume
     */
    public int getVolume() {
        return volume;
    }

    /**
     * @param volume the volume to set
     */
    public void setVolume(int volume) {
        this.volume = volume;
    }

}
