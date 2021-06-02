function topArtists() {
        fetch("http://localhost:8080/api/user-top-artists")
        .then((response) => response.json())
        .then((data) => {
            data.map((artists) => {
                console.log(artists);
                console.log(artists.name)
                var tag = document.createElement("p");
                var text = document.createTextNode(artists.name);
                tag.appendChild(text);
                var element = document.getElementById("artists");
                element.appendChild(tag);
            })
        })
        .then ((response) => console.log("response: "+response))
}

function topTracks() {
        fetch("http://localhost:8080/api/user-top-tracks")
        .then((response) => response.json())
        .then((data) => {
            data.map((tracks) => {
                console.log(tracks);
                console.log(tracks.name)
                var tag = document.createElement("p");
                var text = document.createTextNode(tracks.name);
                tag.appendChild(text);
                var element = document.getElementById("tracks");
                element.appendChild(tag);
            })
        })
        .then ((response) => console.log("response: "+response))
}

function newReleasesCountry () {
        fetch("http://localhost:8080/api/new-releases/france")
        .then((response) => response.json())
        .then((data) => {
            data.map((albums) => {
                console.log(albums);
                var tag = document.createElement("p");
                var album = document.createTextNode("Album: " + albums.name);
                var artist = document.createTextNode(" / Artist: " + albums.artists[0].name);
                tag.appendChild(album);
                tag.appendChild(artist);
                var element = document.getElementById("newReleases");
                element.appendChild(tag);
            })
        })
        .then ((response) => console.log("response: "+response))
}

function newRapReleasesCountry () {
        fetch("http://localhost:8080/api/new-releases-rap/US")
        .then((response) => response.json())
        .then((data) => {
            data.map((albums) => {
                console.log(albums);
                var tag = document.createElement("p");
                var album = document.createTextNode("Album: " + albums.name);
                var artist = document.createTextNode(" / Artist: " + albums.artists[0].name);
                tag.appendChild(album);
                tag.appendChild(artist);
                var element = document.getElementById("newRapReleases");
                element.appendChild(tag);
            })
        })
        .then ((response) => console.log("response: "+response))
}

function savedTracks () {
        fetch("http://localhost:8080/api/saved-tracks")
        .then((response) => response.json())
        .then((data) => {
            data.map((savedTrack) => {
                console.log(savedTrack);
                var tag = document.createElement("p");
                var track = document.createTextNode("Track: " + savedTrack.track.name);
                var artist = document.createTextNode(" / Artist: " + savedTrack.track.artists[0].name);
                tag.appendChild(track);
                tag.appendChild(artist);
                var element = document.getElementById("newRapReleases");
                element.appendChild(tag);
            })
        })
        .then ((response) => console.log("response: "+response))
}

function hello() {
    alert("hello");
}