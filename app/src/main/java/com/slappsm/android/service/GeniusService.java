package com.slappsm.android.service;

import com.slappsm.android.model.Lyrics;
import com.slappsm.android.model.Search;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GeniusService {
    @GET("getLyrics/{id}")
    Call<Lyrics> getLyrics(@Path("id") int id);

    @GET("search/{query}")
    Call<List<Search>> searchSong(@Path("query") String query);
}
