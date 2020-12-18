package com.slappsm.android.ui.lyrics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.slappsm.android.R;
import com.slappsm.android.model.Friend;
import com.slappsm.android.model.Lyrics;
import com.slappsm.android.service.GeniusService;
import com.slappsm.android.service.LastfmService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LyricsFragment extends Fragment {

    public static String BASEURL = "https://songlyricsapi.herokuapp.com/api/genius/";
    private TextView textViewLyrics;

    ImageButton backBtn;
    public LyricsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_song_text, container, false);

        TextView title = rootView.findViewById(R.id.textViewLyricsCurrSong);
        Bundle bundle=getArguments();
        if(bundle != null){
            String myInt = bundle.getString("title", null);
            title.setText(myInt);
        }

        textViewLyrics = rootView.findViewById(R.id.textViewLyrics);

        this.getLyrics();

        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backBtn = view.findViewById(R.id.backButton);
        backBtn.setOnClickListener(v -> goBack(v));
    }
    public void goBack(View v){
        getActivity().getSupportFragmentManager().popBackStack();
    }

    void getLyrics() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASEURL).addConverterFactory(GsonConverterFactory.create()).build();
        GeniusService geniusService = retrofit.create(GeniusService.class);
        Call<Lyrics> call = geniusService.getLyrics();
        call.enqueue(new Callback<Lyrics>() {
            @Override
            public void onResponse(Call<Lyrics> call, Response<Lyrics> response) {
                if(!response.isSuccessful()) {
                    System.out.println("Server Error");
                } else {
                    Lyrics lyrics = response.body();
                    System.out.println(lyrics.toString());
                    textViewLyrics.setText(lyrics.getLyrics());
                }
            }

            @Override
            public void onFailure(Call<Lyrics> call, Throwable t) {
                System.out.println("Internet Error");
            }
        });
    }

}
