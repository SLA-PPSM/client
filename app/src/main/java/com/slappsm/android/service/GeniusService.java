package com.slappsm.android.service;

import com.slappsm.android.model.Lyrics;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GeniusService {
    @GET("getLyrics/2827371")
    Call<Lyrics> getLyrics();
}
