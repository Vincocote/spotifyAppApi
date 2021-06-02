package com.examplespotify.SpotifyTest;

import com.examplespotify.SpotifyTest.services.AuthService;
import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.artists.GetArtistRequest;
import com.wrapper.spotify.requests.data.browse.GetListOfNewReleasesRequest;
import com.wrapper.spotify.requests.data.library.GetUsersSavedTracksRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    AuthService authService;

    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/api/get-user-code/");
    private String code = "";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(ClientKey.CLIENT_ID.getClientKey())
            .setClientSecret(ClientKey.CLIENT_SECRET.getClientKey())
            .setRedirectUri(redirectUri)
            .build();

    @GetMapping("login")
    @ResponseBody
    public String spotifyLogin() {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .scope("user-read-private, user-read-email, user-top-read, playlist-read-private, user-library-read")
                .show_dialog(true)
                .build();
        final URI uri = authorizationCodeUriRequest.execute();
        return uri.toString();
    }

    @GetMapping("get-user-code")
    public String getSpotifyUserCode(@RequestParam("code") String userCode, HttpServletResponse response) throws IOException {
        code = userCode;
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
            .build();

        try{
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Expires in: "+ authorizationCodeCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.out.println("Error: "+ e.getMessage());
        }

        response.sendRedirect("http://localhost:8080/top-artists");
        return spotifyApi.getAccessToken();
    }


    @GetMapping("user-top-artists")
    public Artist[] getUserTopArtists() {
        return authService.getUserTopArtists(50,0,"long_term", spotifyApi);
    }

    @GetMapping("user-top-tracks")
    public Track[] getUserTopTracks() {
        return authService.getUserTopTracks(20,0, "short_term", spotifyApi);
    }

/*
    @GetMapping("new-releases/{country}")
    public AlbumSimplified[] getCountryNewReleases(@PathVariable("country") String country) {
        try {

            final GetListOfNewReleasesRequest getListOfNewReleasesRequest = spotifyApi.getListOfNewReleases()
                    .country(CountryCode.US)
                    .limit(30)
                    .offset(0)
                    .build();
            final Paging<AlbumSimplified> albumSimplifiedPagingPaging = getListOfNewReleasesRequest.execute();

            System.out.println("Total: " + albumSimplifiedPagingPaging.getTotal());
            return albumSimplifiedPagingPaging.getItems();
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return new AlbumSimplified[0];
    }

 */
    public Artist getArtist(String id) throws IOException, ParseException, SpotifyWebApiException {
        GetArtistRequest getArtistRequest = spotifyApi.getArtist(id)
                .build();
        return getArtistRequest.execute();
    }

    public Paging<AlbumSimplified> getCountryNewReleases(Integer limit, Integer offset) throws IOException, ParseException, SpotifyWebApiException {
        GetListOfNewReleasesRequest getListOfNewReleasesRequest = spotifyApi.getListOfNewReleases()
                .country(CountryCode.US)
                .limit(limit)
                .offset(offset)
                .build();
        return getListOfNewReleasesRequest.execute();
    }

    @GetMapping("new-releases/{country}")
    public AlbumSimplified[] getCountryNewReleases(@PathVariable("country") String country) {
        return authService.getCountryNewReleases(country, spotifyApi);
    }

    @GetMapping("new-releases-rap/{country}")
    public Set<AlbumSimplified> getCountryNewRapReleases(@PathVariable("country") String country){
        try {
            HashSet<AlbumSimplified> lastRapAlbumReleased = new HashSet<AlbumSimplified>();
            Paging<AlbumSimplified> albumSimplifiedPaging = getCountryNewReleases(50,0);

            Set<AlbumSimplified> rapAlbumSimplifiedPaging = Arrays.stream(albumSimplifiedPaging.getItems())
                    .filter(album -> {
                        try {
                            return Arrays.stream(getArtist(album.getArtists()[0].getId()).getGenres())
                                    .collect(Collectors.toSet())
                                    .contains("rap");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (SpotifyWebApiException e) {
                            e.printStackTrace();
                        }
                        return false;})
                    .collect(Collectors.toSet());

            rapAlbumSimplifiedPaging.stream()
                    .map(rapAlbum -> lastRapAlbumReleased.add(rapAlbum))
                    .collect(Collectors.toSet());

            System.out.println("Total: " + albumSimplifiedPaging.getTotal());

            return lastRapAlbumReleased;
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return Collections.emptySet();
    }

    @GetMapping("saved-tracks")
    public HashSet<SavedTrack> getSavedTracks() {
        return authService.getAllSavedTracks(spotifyApi);
    }
}
