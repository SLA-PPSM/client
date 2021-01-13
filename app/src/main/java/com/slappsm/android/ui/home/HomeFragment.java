package com.slappsm.android.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.slappsm.android.MainActivity;
import com.slappsm.android.R;
import com.slappsm.android.SetNameActivity;
import com.slappsm.android.model.Profile;
import com.slappsm.android.model.Song;
import com.slappsm.android.service.LastfmService;
import com.slappsm.android.ui.lyrics.LyricsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment implements HomeRecyclerViewAdapter.ItemClickListener {

    private HomeViewModel homeViewModel;
    private TextView title;
    private ImageButton settingsButton;
    private View loadingPanel;
    private View homeView;
    public static String BASEURL = "https://songlyricsapi.herokuapp.com/api/lastfm/";
    private TextView textNickname;
    private TextView textScrobbles;
    private ImageView imageAvatar;
    private TextView textViewLyricsCurrSong;
    private String username;


    RecyclerView recyclerView;
    HomeRecyclerViewAdapter adapter;
    ArrayList<Song> recentTracksList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        textNickname = root.findViewById(R.id.textNickname);
        textScrobbles = root.findViewById(R.id.textScrobbles);
        imageAvatar = root.findViewById(R.id.imageAvatar);
        textViewLyricsCurrSong = root.findViewById(R.id.textViewLyricsCurrSong);
        loadingPanel=root.findViewById(R.id.loadingPanel);
        homeView=root.findViewById(R.id.homeView);
        recentTracksList = new ArrayList<>();

        // set up the RecyclerView
        recyclerView = root.findViewById(R.id.recyclerViewHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HomeRecyclerViewAdapter(getContext(), recentTracksList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        username= MainActivity.username;

        this.loadProfile();
        this.loadCurrentTrack();
        this.loadRecentTracks();

        return root;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = getView().findViewById(R.id.textViewLyricsCurrSong);
        title.setOnClickListener(v -> showLyrics(title.getText().toString()));
        settingsButton = view.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> goToSettings(v));
        System.out.println(username);
    }

    public void showLyrics(String song) {
        Bundle bun = new Bundle();
        bun.putString("title", song);
        LyricsFragment lyricsfg = new LyricsFragment();
        lyricsfg.setArguments(bun);
        FragmentManager fm = getParentFragmentManager();

        fm.beginTransaction()
                .replace(R.id.nav_host_fragment, lyricsfg, "findThisFragment")
                .addToBackStack(null)
                .commit();

    }

    void loadRecentTracks() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASEURL).addConverterFactory(GsonConverterFactory.create()).build();
        LastfmService lastfmService = retrofit.create(LastfmService.class);
        Call<List<Song>> call = lastfmService.getRecentTracks(username);
        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if(!response.isSuccessful()) {
                    System.out.println("Server Error");
                } else {
                    List<Song> recentTracks = response.body();
                    recentTracksList.addAll(recentTracks);
                    adapter.notifyDataSetChanged();
                    loadingPanel.setVisibility(View.GONE);
                    homeView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                System.out.println("Internet Error");
                System.out.println("XD" + t.getMessage());

            }
        });
    }

    void loadProfile() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASEURL).addConverterFactory(GsonConverterFactory.create()).build();
        LastfmService lastfmService = retrofit.create(LastfmService.class);
        Call<Profile> call = lastfmService.getProfile(username);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if(!response.isSuccessful()) {
                    System.out.println("Server Error");
                } else {
                    Profile profile = response.body();
                    System.out.println(profile.toString());
                    textNickname.setText(profile.getNick());
                    textScrobbles.setText(profile.getScrobbles() + " scrobbles");

                    Picasso.get().setLoggingEnabled(true);
                    Picasso.get().load(profile.getAvatar()).into(imageAvatar);
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                System.out.println("Internet Error");
            }
        });
    }

    void loadCurrentTrack() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASEURL).addConverterFactory(GsonConverterFactory.create()).build();
        LastfmService lastfmService = retrofit.create(LastfmService.class);
        Call<Song> call = lastfmService.getCurrentTrack(username);
        call.enqueue(new Callback<Song>() {
            @Override
            public void onResponse(Call<Song> call, Response<Song> response) {
                if(!response.isSuccessful()) {
                    System.out.println("Server Error");
                } else {
                    Song song = response.body();
                    System.out.println(song.toString());
                    if(song.getArtist() == null){
                        textViewLyricsCurrSong.setText("nothing is playing");
                        textViewLyricsCurrSong.setClickable(false);
                    } else {
                        textViewLyricsCurrSong.setText(song.getTitle() + " - " + song.getArtist());
                        textViewLyricsCurrSong.setClickable(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<Song> call, Throwable t) {
                System.out.println("Internet Error");
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Song song = adapter.getItem(position);
        showLyrics(song.getTitle() + " - " + song.getArtist());
    }
    public void goToSettings(View v) {
        Intent intent = new Intent(getActivity(), SetNameActivity.class);
        startActivity(intent);
    }
}