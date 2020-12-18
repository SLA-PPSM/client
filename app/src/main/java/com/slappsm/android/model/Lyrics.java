package com.slappsm.android.model;

public class Lyrics {
    String id;
    String lyrics;

    public String getId() {
        return id;
    }

    public String getLyrics() {
        return lyrics;
    }

    @Override
    public String toString() {
        return "Lyrics{" +
                "id='" + id + '\'' +
                ", lyrics='" + lyrics + '\'' +
                '}';
    }
}
