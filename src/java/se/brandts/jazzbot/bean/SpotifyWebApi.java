/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.brandts.jazzbot.bean;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import se.brandts.jazzbot.rest.client.SpotifyWebApiClient;
import se.brandts.jazzbot.rest.data.SampleTrackData;

/**
 *
 * @author Teddy
 */
@ManagedBean
@SessionScoped
public class SpotifyWebApi {

    private final SpotifyWebApiClient rest;
    private String trackSearchQuery;
    private List<SampleTrackData> searchResult;

    public SpotifyWebApi() {
        rest = new SpotifyWebApiClient();
    }

    public void searchTrack() {
         System.out.println("Searching with: " + trackSearchQuery);
        setSearchResult(rest.searchTrack(trackSearchQuery));
        trackSearchQuery = "";
    }

    /**
     * @return the trackSearchQuery
     */
    public String getTrackSearchQuery() {
        return trackSearchQuery;
    }

    /**
     * @param trackSearchQuery the trackSearchQuery to set
     */
    public void setTrackSearchQuery(String trackSearchQuery) {
        this.trackSearchQuery = trackSearchQuery;
    }

    /**
     * @return the searchResult
     */
    public List<SampleTrackData> getSearchResult() {
        return searchResult;
    }

    /**
     * @param searchResult the searchResult to set
     */
    public void setSearchResult(List<SampleTrackData> searchResult) {
        this.searchResult = searchResult;
    }
}
