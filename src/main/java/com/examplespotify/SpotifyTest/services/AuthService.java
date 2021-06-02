package com.examplespotify.SpotifyTest.services;

import com.examplespotify.SpotifyTest.ClientKey;
import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.data.browse.GetListOfNewReleasesRequest;
import com.wrapper.spotify.requests.data.library.GetUsersSavedTracksRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/api/get-user-code/");

    public Artist[] getUserTopArtists(Integer limit, Integer offset, String time_range, SpotifyApi spotifyApi) {
        try{
            final GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists()
                    .limit(limit)
                    .offset(offset)
                    .time_range(time_range)
                    .build();
            final Paging<Artist> artistPaging = getUsersTopArtistsRequest.execute();
            System.out.println("Total: " + artistPaging.getTotal());
            return artistPaging.getItems();
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return new Artist[0];
    }

    public Track[] getUserTopTracks(Integer limit, Integer offset, String time_range, SpotifyApi spotifyApi) {
        try {
            final GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyApi.getUsersTopTracks()
                    .limit(limit)
                    .offset(offset)
                    .time_range(time_range)
                    .build();
            final Paging<Track> trackPaging = getUsersTopTracksRequest.execute();
            System.out.println("Total: " + trackPaging.getTotal());
            return trackPaging.getItems();
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return new Track[0];
    }

    public AlbumSimplified[] getCountryNewReleases(String country, SpotifyApi spotifyApi) {
        try {
            final GetListOfNewReleasesRequest getListOfNewReleasesRequest = spotifyApi.getListOfNewReleases()
                    .country(CountryCode.US)
                    .limit(50)
                    .offset(0)
                    .build();
            final Paging<AlbumSimplified> albumSimplifiedPaging = getListOfNewReleasesRequest.execute();
            System.out.println("Total: " + albumSimplifiedPaging.getTotal());
            return albumSimplifiedPaging.getItems();
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return new AlbumSimplified[0];
    }

    public SavedTrack[] getSavedTracks(Integer limit, Integer offset, SpotifyApi spotifyApi) {
        try {
            final GetUsersSavedTracksRequest getUsersSavedTracksRequest = spotifyApi.getUsersSavedTracks()
                    .limit(limit)
                    .offset(offset)
                    .build();
            final Paging<SavedTrack> savedTrackPaging = getUsersSavedTracksRequest.execute();
            System.out.println("Total: " + savedTrackPaging.getTotal());
            return savedTrackPaging.getItems();
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return new SavedTrack[0];
    }

    public HashSet<SavedTrack> getAllSavedTracks(SpotifyApi spotifyApi) {
        HashSet<SavedTrack> savedTracks = new HashSet<>();
        SavedTrack[] allSavedTracks = new SavedTrack[0];
        Integer totalTracks = 0;
        try {
            final GetUsersSavedTracksRequest getUsersSavedTracksRequest = spotifyApi.getUsersSavedTracks()
                    .limit(50)
                    .offset(0)
                    .build();
            final Paging<SavedTrack> savedTrackPaging = getUsersSavedTracksRequest.execute();
            System.out.println("Total: " + savedTrackPaging.getTotal());
            totalTracks = savedTrackPaging.getTotal();
            Arrays.stream(savedTrackPaging.getItems()).map(savedTrack -> savedTracks.add(savedTrack));
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        for( Integer i = 50; savedTracks.size() == totalTracks; i+=50) {
            SavedTrack[] newSavedTracks = getSavedTracks(50,i,spotifyApi);
            Arrays.stream(newSavedTracks).map(savedTrack -> savedTracks.add(savedTrack));
            System.out.println(savedTracks);
        }
        System.out.println(savedTracks);
        return savedTracks;
    }
}
