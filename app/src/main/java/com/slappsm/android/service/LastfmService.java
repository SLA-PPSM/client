package com.slappsm.android.service;

import com.slappsm.android.model.Friend;
import com.slappsm.android.model.Profile;
import com.slappsm.android.model.Song;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LastfmService {
    @GET("getRecentTracks/{username}")
    Call<List<Song>> getRecentTracks(@Path("username") String username);

    @GET("getProfile/{username}")
    Call<Profile> getProfile(@Path("username") String username);

    @GET("getCurrentTrack/{username}")
    Call<Song> getCurrentTrack(@Path("username") String username);

    @GET("getFriends/{username}")
    Call<List<Friend>> getFriends(@Path("username") String username);
}
