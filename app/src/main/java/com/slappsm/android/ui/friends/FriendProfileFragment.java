package com.slappsm.android.ui.friends;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.slappsm.android.R;
import com.slappsm.android.model.Profile;
import com.slappsm.android.model.Song;
import com.slappsm.android.service.LastfmService;
import com.slappsm.android.ui.home.HomeRecyclerViewAdapter;
import com.slappsm.android.ui.lyrics.LyricsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FriendProfileFragment extends Fragment implements HomeRecyclerViewAdapter.ItemClickListener {

    private TextView title;
    public static String BASEURL = "https://songlyricsapi.herokuapp.com/api/lastfm/";
    private TextView textNickname;
    private TextView textScrobbles;
    private ImageView imageAvatar;
    private TextView textViewLyricsCurrSong;
    private String username;
    private View loadingPanel;
    private View homeView;

    RecyclerView recyclerView;
    HomeRecyclerViewAdapter adapter;
    ArrayList<Song> recentTracksList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_friend_profile, container, false);
        Bundle bundle=getArguments();
        if(bundle != null){
            username = bundle.getString("username", null);
        }
        textNickname = root.findViewById(R.id.textNickname);
        textScrobbles = root.findViewById(R.id.textScrobbles);
        imageAvatar = root.findViewById(R.id.imageAvatar);
        textViewLyricsCurrSong = root.findViewById(R.id.textViewLyricsCurrSong);
        loadingPanel=root.findViewById(R.id.loadingPanel);
        homeView=root.findViewById(R.id.friendProfileView);

        recentTracksList = new ArrayList<>();

        // set up the RecyclerView
        recyclerView = root.findViewById(R.id.recyclerViewHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HomeRecyclerViewAdapter(getContext(), recentTracksList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


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
        System.out.println(username);
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

    public void showLyrics(String song) {
        Bundle bun = new Bundle();
        bun.putString("title", song);
        bun.putString("navFriend","Friends");
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
                    for (Song song: recentTracks) {
                        recentTracksList.add(song);
                    }
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
}
