package com.examplespotify.SpotifyTest;

import lombok.Getter;

@Getter
public enum ClientKey {
    CLIENT_ID("2fe7a947dc39429d8461d49479bb2fa5"),
    CLIENT_SECRET("e113a3936ff24550b9daba4b9b9d9186");

    private final String clientKey;

    ClientKey(String clientKey) {
        this.clientKey = clientKey;
    }
}
