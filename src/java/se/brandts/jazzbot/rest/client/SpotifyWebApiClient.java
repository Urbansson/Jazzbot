/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.brandts.jazzbot.rest.client;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.PlaylistRequest;
import com.wrapper.spotify.methods.TrackRequest;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;
import com.wrapper.spotify.models.Image;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.Playlist;
import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.SimpleAlbum;
import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.lang.instrument.Instrumentation;

import se.brandts.jazzbot.rest.data.CurrentTrackData;
import se.brandts.jazzbot.rest.data.SampleTrackData;

public class SpotifyWebApiClient {

    final String clientId = "f5822e62a191460ea533ecbbb74f90ef";
    final String clientSecret = "8d6b969050c24263a991787dd6a507ec";
    final Api api;

    public SpotifyWebApiClient() {

        api = Api.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();

        final ClientCredentialsGrantRequest request = api.clientCredentialsGrant().build();

        /* Use the request object to make the request, either asynchronously (getAsync) or synchronously (get) */
        final SettableFuture<ClientCredentials> responseFuture = request.getAsync();

        /* Add callbacks to handle success and failure */
        Futures.addCallback(responseFuture, new FutureCallback<ClientCredentials>() {
            @Override
            public void onSuccess(ClientCredentials clientCredentials) {
                /* The tokens were retrieved successfully! */
                System.out.println("Successfully retrieved an access token! " + clientCredentials.getAccessToken());
                System.out.println("The access token expires in " + clientCredentials.getExpiresIn() + " seconds");

                /* Set access token on the Api object so that it's used going forward */
                api.setAccessToken(clientCredentials.getAccessToken());

                /* Please note that this flow does not return a refresh token.
                 * That's only for the Authorization code flow */
            }

            @Override
            public void onFailure(Throwable throwable) {
                /* An error occurred while getting the access token. This is probably caused by the client id or
                 * client secret is invalid. */
            }
        });

    }

    public CurrentTrackData getTrackWithID(String trackID) {
        //Api api = Api.DEFAULT_API;
        CurrentTrackData data = new CurrentTrackData();

        final TrackRequest request = api.getTrack(trackID).build();

        try {
            Track result = request.get();

            data.setTitle(result.getName());
            data.setId(result.getId());
            data.setAlbumName(result.getAlbum().getName());

            for (SimpleArtist artist : result.getArtists()) {
                data.addArtists(artist.getName());
            }

            data.setAlbumCoverUrlLarge(result.getAlbum().getImages().get(0).getUrl());
            data.setAlbumCoverUrlMedium(result.getAlbum().getImages().get(1).getUrl());
            data.setAlbumCoverUrlSmall(result.getAlbum().getImages().get(2).getUrl());

            data.setPreviewUrl(result.getPreviewUrl());

            data.setDuration(result.getDuration());

        } catch (Exception e) {
            System.out.println("Something went wrong getting getTrackWithID");
        }

        return data;
    }

    public SampleTrackData getSampleTrackWithID(String trackID) {
        //Api api = Api.DEFAULT_API;
        SampleTrackData data = new SampleTrackData();

        final TrackRequest request = api.getTrack(trackID).build();

        try {
            Track result = request.get();

            data.setTitle(result.getName());
            data.setId(result.getId());
            data.setAlbumName(result.getAlbum().getName());

            for (SimpleArtist artist : result.getArtists()) {
                data.addArtists(artist.getName());
            }

            data.setAlbumCoverUrlLarge(result.getAlbum().getImages().get(0).getUrl());
            data.setAlbumCoverUrlMedium(result.getAlbum().getImages().get(1).getUrl());
            data.setAlbumCoverUrlSmall(result.getAlbum().getImages().get(2).getUrl());

            data.setPreviewUrl(result.getPreviewUrl());

            data.setDuration(result.getDuration());

        } catch (Exception e) {
            System.out.println("Something went wrong getting getSampleTrackWithID");
        }

        return data;
    }

    public List<SampleTrackData> searchTrack(String query) {
        //Api api = Api.DEFAULT_API;

        ArrayList<SampleTrackData> data = new ArrayList<SampleTrackData>();

        final TrackSearchRequest request = api.searchTracks(query).market("SE").limit(20).build();
        try {

            final Page<Track> trackSearchResult = request.get();

            List<Track> tracks = trackSearchResult.getItems();
            for (Track track : tracks) {
                SampleTrackData tmpData = new SampleTrackData();

                tmpData.setTitle(track.getName());
                tmpData.setId(track.getId());
                tmpData.setAlbumName(track.getAlbum().getName());

                for (SimpleArtist artist : track.getArtists()) {
                    tmpData.addArtists(artist.getName());
                }

                tmpData.setAlbumCoverUrlLarge(track.getAlbum().getImages().get(0).getUrl());
                tmpData.setAlbumCoverUrlMedium(track.getAlbum().getImages().get(1).getUrl());
                tmpData.setAlbumCoverUrlSmall(track.getAlbum().getImages().get(2).getUrl());

                tmpData.setPreviewUrl(track.getPreviewUrl());

                tmpData.setDuration(track.getDuration());
                data.add(tmpData);
            }

            System.out.println("I got " + trackSearchResult.getTotal() + " results!");
        } catch (Exception e) {
            System.out.println("Something went wrong! searchTrack");
        }

        return data;
    }

    public Playlist getPlayList(String playListOwner, String playListId) {

        final PlaylistRequest request = api.getPlaylist(playListOwner, playListId).build();
        ArrayList<SampleTrackData> data = new ArrayList<SampleTrackData>();
        Playlist playlist = null;
        try {
            playlist = request.get();

            System.out.println("Retrieved playlist " + playlist.getName());
            System.out.println("It contains " + playlist.getTracks().getTotal() + " tracks");

        } catch (Exception e) {
            System.out.println("Something went wrong! when loading track might be auth token" + e.getMessage());
        }

        return playlist;
    }

}
