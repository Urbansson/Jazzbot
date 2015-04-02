/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.brandts.jazzbot.bean;

import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SlideEndEvent;
import se.brandts.jazzbot.rest.data.CurrentTrackData;
import se.brandts.jazzbot.rest.data.SampleTrackData;
import se.brandts.jazzbot.spotify.SpotifyController.SpotifyController;

/**
 *
 * @author Teddy
 */
@ManagedBean
@SessionScoped
public class SpotifyControllerBean {

    private int volume;

    @EJB
    private SpotifyController controller;

    public SpotifyControllerBean() {
        System.out.println("created");
    }

    public void pause() {
        this.controller.pause();
    }

    public void play() {
        this.getController().play();
    }

    public void playPause() {
        this.getController().playPause();
    }

    public void playTrackWithId(String id) {
        this.getController().playTrackWithId(id);
    }

    public void nextTrack() {
        this.getController().nextTrack();
    }

    public void prevTrack() {
        this.getController().prevTrack();
    }

    public void addToQueue(String id) {
        this.getController().addToQueue(id);
    }

    public void removeFromQueue(int pos) {
        this.getController().removeFromQueue(pos);
    }

    public void playNow(int pos, String id) {
        this.getController().playNow(pos, id);
    }

    public void volumeUp() {
        this.changeVolume(this.getController().getCurrentlyPlaying().getVolume()+11);
    }

    public void volumeDown() {
        this.changeVolume(this.getController().getCurrentlyPlaying().getVolume()-9);
    }

    public void changeVolume(int volume) {
        this.getController().changeVolume(volume);
    }

    /**
     * @return the currentlyPlaying
     */
    public CurrentTrackData getCurrentlyPlaying() {
        return this.controller.getCurrentlyPlaying();//currentlyPlaying;
    }

    /**
     * @return the playQueue
     */
    public List<SampleTrackData> getQueue() {
        return this.controller.getQueue();
    }

    /**
     * @return the controller
     */
    public SpotifyController getController() {
        return controller;
    }

    /**
     * @param controller the controller to set
     */
    public void setController(SpotifyController controller) {
        this.controller = controller;
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
