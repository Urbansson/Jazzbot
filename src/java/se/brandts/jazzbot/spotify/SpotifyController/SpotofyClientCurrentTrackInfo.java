/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.brandts.jazzbot.spotify.SpotifyController;

import com.google.gson.Gson;

/**
 *
 * @author Teddy
 */
public class SpotofyClientCurrentTrackInfo {

    private String track_id;
    private String state;

    private int volume;

    private int position;
    private int duration;

    public SpotofyClientCurrentTrackInfo(String json) {
        Gson gson = new Gson();
        SpotofyClientCurrentTrackInfo tmp = gson.fromJson(json, SpotofyClientCurrentTrackInfo.class);
        this.track_id = tmp.track_id.split(":")[2];
        this.state = tmp.state;
        this.position = tmp.position;
        this.duration = tmp.duration;
        this.volume = tmp.volume;
    }

    /**
     * @return the track_id
     */
    public String getTrack_id() {
        return track_id;
    }

    /**
     * @param track_id the track_id to set
     */
    public void setTrack_id(String track_id) {
        this.track_id = track_id;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
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
