package com.slappsm.android.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.slappsm.android.MainActivity;
import com.slappsm.android.R;
import com.slappsm.android.model.Friend;
import com.slappsm.android.model.Song;
import com.slappsm.android.service.LastfmService;
import com.slappsm.android.ui.home.HomeRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FriendsFragment extends Fragment implements FriendsRecyclerViewAdapter.ItemClickListener {

    private FriendsViewModel friendsViewModel;
    public static String BASEURL = "https://songlyricsapi.herokuapp.com/api/lastfm/";
    private String username;

    RecyclerView recyclerView;
    FriendsRecyclerViewAdapter adapter;
    ArrayList<String> friendsList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        friendsViewModel =
                new ViewModelProvider(this).get(FriendsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_friends, container, false);

        this.loadFriends();
        username= MainActivity.username;

        friendsList = new ArrayList<>();
        recyclerView = root.findViewById(R.id.recyclerViewFriends);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FriendsRecyclerViewAdapter(getContext(), friendsList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


        return root;
    }

    void loadFriends() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASEURL).addConverterFactory(GsonConverterFactory.create()).build();
        LastfmService lastfmService = retrofit.create(LastfmService.class);
        Call<List<Friend>> call = lastfmService.getFriends();
        call.enqueue(new Callback<List<Friend>>() {
            @Override
            public void onResponse(Call<List<Friend>> call, Response<List<Friend>> response) {
                if(!response.isSuccessful()) {
                    System.out.println("Server Error");
                } else {
                    List<Friend> friends = response.body();
                    System.out.println(friends.toString());
                    for (Friend friend: friends) {
                        friendsList.add(friend.getNick());
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Friend>> call, Throwable t) {
                System.out.println("Internet Error");
            }
        });
    }


    @Override
    public void onItemClick(View view, int position) {

    }
}