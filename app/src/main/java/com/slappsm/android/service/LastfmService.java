package com.slappsm.android.service;

import com.slappsm.android.model.Friend;
import com.slappsm.android.model.Profile;
import com.slappsm.android.model.Song;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LastfmService {
    @GET("getRecentTracks/freakinlikemj")
    Call<List<Song>> getRecentTracks();

    @GET("getProfile/freakinlikemj")
    Call<Profile> getProfile();

    @GET("getCurrentTrack/freakinlikemj")
    Call<Song> getCurrentTrack();

    @GET("getFriends/freakinlikemj")
    Call<List<Friend>> getFriends();
}
