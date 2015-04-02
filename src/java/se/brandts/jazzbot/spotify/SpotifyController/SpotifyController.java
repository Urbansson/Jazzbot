/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.brandts.jazzbot.spotify.SpotifyController;

import com.wrapper.spotify.models.Playlist;
import com.wrapper.spotify.models.PlaylistTrack;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import static javax.ejb.ConcurrencyManagementType.BEAN;
import javax.ejb.EJBException;
import javax.ejb.Schedule;
import javax.ejb.Schedules;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import se.brandts.jazzbot.rest.client.SpotifyWebApiClient;
import se.brandts.jazzbot.rest.data.CurrentTrackData;
import se.brandts.jazzbot.rest.data.SampleTrackData;
import se.brandts.jazzbot.socket.infoSocket;

/**
 *
 * @author Teddy
 */
@Singleton
@Startup
@ConcurrencyManagement(BEAN)
public class SpotifyController {

    private CurrentTrackData currentlyPlaying;
    private int currentlyPlayingPos;
    private List<SampleTrackData> playQueue;
    private Playlist currentPlayList;

    private SpotifyWebApiClient rest;

    private int nrOfIdle = 0;

    @Resource
    TimerService timerService;

    private ScheduledExecutorService crawler;

    public SpotifyController() {
        this.playQueue = new ArrayList<SampleTrackData>();
        this.currentPlayList = null;
  
        
        this.rest = new SpotifyWebApiClient();
    }

    @PostConstruct
    public void applicationStartup() {
        System.out.println("Created Singeton");
        this.updateCurrentlyPlaying();
        //this.startTrackCrawler();
    }

    @PreDestroy
    public void applicationShutdown() {
        System.out.println("Stopping crawler");
        crawler.shutdownNow();
    }

    public void pause() {
        SpotifyClientHandler.pause();
        this.updateCurrentlyPlaying();
    }

    public void play() {
        SpotifyClientHandler.play();
        this.updateCurrentlyPlaying();

    }

    public void playPause() {
        this.nrOfIdle = 0;
        SpotifyClientHandler.playPause();
        this.updateCurrentlyPlaying();

    }

    public void playTrackWithId(String id) {
        SpotifyClientHandler.playTrackWithId(id,"");
        this.updateCurrentlyPlaying();

    }

    public void nextTrack() {
        
        System.out.println("Queue is empty : "+ this.playQueue.isEmpty());
        if (this.playQueue.isEmpty()) {
            SpotifyClientHandler.nextTrack();
            this.updateCurrentlyPlaying();
        } else {
            this.playNow(0, this.playQueue.get(0).getId());
        }

    }

    public void prevTrack() {
        SpotifyClientHandler.prevTrack();
        this.updateCurrentlyPlaying();

    }

    public void addToQueue(String id) {
        SampleTrackData data = rest.getSampleTrackWithID(id);
        this.currentPlayList = rest.getPlayList("jazzbots","7iTd6YShc0DVZWM5ySgvx4");
            
        this.playQueue.add(data);
        infoSocket.broadcastReload();
    }

    public void removeFromQueue(int pos) {
        this.playQueue.remove(pos);
        infoSocket.broadcastReload();
    }

    public void playNow(int pos, String id) {
        this.playTrackWithId(id);
        this.removeFromQueue(pos);
        this.updateCurrentlyPlaying();
    }

    public void changeVolume(int volume) {
        SpotifyClientHandler.setVolume(volume);
        this.updateCurrentlyPlayingNoTimer();

    }

    @Timeout
    public void trackEnded() {
        if (this.playQueue.isEmpty()) {
            this.nrOfIdle++;
        } else {
            this.nrOfIdle = 0;
        }
        this.nextTrack();
    }

    private void stopTimer() {
        for (javax.ejb.Timer timer : timerService.getTimers()) {
                System.out.println("Timer stoped: " + timer.getInfo());
                timer.cancel();
        }
    }

    private void startTimer(String track, int duration) {

        //this.stopTimer();
        System.out.println("Starting Timer for: " + track + " with duration: " + duration);
        TimerConfig config = new TimerConfig();
        config.setInfo(track);
        timerService.createSingleActionTimer(duration - 600, config);

    }

    private void updateCurrentlyPlaying() {

        this.stopTimer();

        System.out.println("Queue is: " + this.getQueue().size() + " idleQueue is: " + this.nrOfIdle);
        Runtime runtime = Runtime.getRuntime();
        SpotofyClientCurrentTrackInfo info = SpotifyClientHandler.getCurrentTrack();
        CurrentTrackData data = rest.getTrackWithID(info.getTrack_id());
        data.setPosition(info.getPosition() * 1000);
        data.setPlaying(info.getState().contains("playing"));
        data.setVolume(info.getVolume());

        if (data != null) {
            if (data.isPlaying()) {
                int time = data.getDuration() - data.getPosition();
                System.out.println("track duration: " + data.getDuration() + " current pos: " + data.getPosition() + " timer length " + time);
                this.startTimer(data.getId(), time);
            }
        }

        infoSocket.broadcastReload();
        this.setCurrentlyPlaying(data);
    }

    private void updateCurrentlyPlayingNoTimer() {

        Runtime runtime = Runtime.getRuntime();
        SpotofyClientCurrentTrackInfo info = SpotifyClientHandler.getCurrentTrack();
        CurrentTrackData data = rest.getTrackWithID(info.getTrack_id());
        data.setPosition(info.getPosition() * 1000);
        data.setPlaying(info.getState().contains("playing"));
        data.setVolume(info.getVolume());

        infoSocket.broadcastReload();
        this.setCurrentlyPlaying(data);
    }

    public void startTrackCrawler() {
        crawler = Executors.newSingleThreadScheduledExecutor();
    }

    @Schedules({
        //@Schedule(minute = "*", hour = "*" ,second = "0,5,10,15,20,25,30,35,40,45,50,55")
        @Schedule(second = "*/3", minute = "*", hour = "*", persistent = false)
    })
    private void SpotifyCrawler() {

        //System.out.println("Result; " + resultJson);
        SpotofyClientCurrentTrackInfo info = SpotifyClientHandler.getCurrentTrack();
        if (SpotifyController.this.currentlyPlaying.isPlaying() != info.getState().contains("playing")) {
            //System.err.println("One is playing one is not");
            this.updateCurrentlyPlaying();
        }

        if (!SpotifyController.this.currentlyPlaying.getId().equals(info.getTrack_id())) {
            //System.out.println("New track Playing");
            this.updateCurrentlyPlaying();
        }

        if (SpotifyController.this.currentlyPlaying.getVolume() != (info.getVolume())) {
            //System.out.println("New track Playing")
            //infoSocket.broadcastReload();
            this.updateCurrentlyPlayingNoTimer();
        }
        /*
         if (SpotifyController.this.currentlyPlaying.isPlaying()) {
         //int pos = (info.getPosition()/info.getDuration())*100;
         int pos = (int) (((double) info.getPosition() / (double) info.getDuration()) * 100);
         //infoSocket.broadcastTrackpos(pos);
         this.currentlyPlayingPos = pos;
         }*/

    }

    /**
     * @return the currentlyPlaying
     */
    public CurrentTrackData getCurrentlyPlaying() {
        return currentlyPlaying;
    }

    /**
     * @param currentlyPlaying the currentlyPlaying to set
     */
    public void setCurrentlyPlaying(CurrentTrackData currentlyPlaying) {
        this.currentlyPlaying = currentlyPlaying;
    }

    /**
     * @return the playQueue
     */
    public List<SampleTrackData> getQueue() {
        return this.playQueue;
    }

}
