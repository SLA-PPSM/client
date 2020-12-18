package com.slappsm.android.model;

public class Song {
    String artist;
    String title;
    String image;
    String date;

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }


    public String getImage() {
        return image;
    }


    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Song{" +
                "artist='" + artist + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}