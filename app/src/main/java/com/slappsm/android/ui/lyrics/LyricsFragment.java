package com.slappsm.android.ui.lyrics;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.slappsm.android.R;
import com.slappsm.android.model.Lyrics;
import com.slappsm.android.model.Search;
import com.slappsm.android.service.GeniusService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LyricsFragment extends Fragment {

    public static String BASEURL = "https://songlyricsapi.herokuapp.com/api/genius/";
    private TextView title;
    private TextView textViewLyrics;
    private String song;
    private String id;
    String navigatedFromFriends;
    private View loadingPanel;
    private View lyricsView;


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

        title = rootView.findViewById(R.id.textViewLyricsCurrSong);
        Bundle bundle=getArguments();
        if(bundle != null){
            song = bundle.getString("title", null);
            id = bundle.getString("id", null);
            navigatedFromFriends = bundle.getString("navFriend",null);
            title.setText(song);
        }

        textViewLyrics = rootView.findViewById(R.id.textViewLyrics);
        loadingPanel=rootView.findViewById(R.id.loadingPanel);
        lyricsView=rootView.findViewById(R.id.lyricsView);
        if(id == null) {
            this.searchSong();
        } else {
            this.getLyrics();
        }

        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backBtn = view.findViewById(R.id.backButton);
        backBtn.setOnClickListener(v -> goBack());
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    getParentFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }
    public void goBack(){
        FragmentManager fm = getParentFragmentManager();
        fm.popBackStack();

    }

    void getLyrics() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASEURL).addConverterFactory(GsonConverterFactory.create()).build();
        GeniusService geniusService = retrofit.create(GeniusService.class);
        Call<Lyrics> call = geniusService.getLyrics(Integer.parseInt(id));
        call.enqueue(new Callback<Lyrics>() {
            @Override
            public void onResponse(Call<Lyrics> call, Response<Lyrics> response) {
                if(!response.isSuccessful()) {
                    System.out.println("Server Error");
                } else {
                    Lyrics lyrics = response.body();
                    System.out.println(lyrics.toString());
                    textViewLyrics.setText(lyrics.getLyrics() + "\n\n\n\n");
                    loadingPanel.setVisibility(View.GONE);
                    lyricsView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Lyrics> call, Throwable t) {
                System.out.println("Internet Error");
            }
        });
    }

    void searchSong() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASEURL).addConverterFactory(GsonConverterFactory.create()).build();
        GeniusService geniusService = retrofit.create(GeniusService.class);
        Call<List<Search>> call = geniusService.searchSong(song);
        call.enqueue(new Callback<List<Search>>() {
            @Override
            public void onResponse(Call<List<Search>> call, Response<List<Search>> response) {
                if(!response.isSuccessful()) {
                    System.out.println("Server Error");
                } else {
                    List<Search> searchList = response.body();
                    if(searchList.size() > 0) {
                        id = String.valueOf(searchList.get(0).getId());
                        getLyrics();
                    } else {
                        goBack();
                        Toast.makeText(getActivity(), "Lyrics not found", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Search>> call, Throwable t) {
                System.out.println("Internet Error");
            }
        });
    }

}
