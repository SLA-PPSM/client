package com.slappsm.android.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.slappsm.android.R;
import com.slappsm.android.model.Search;
import com.slappsm.android.service.GeniusService;
import com.slappsm.android.ui.lyrics.LyricsFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment implements SearchRecyclerViewAdapter.ItemClickListener {

    public static String BASEURL = "https://songlyricsapi.herokuapp.com/api/genius/";
    private SearchViewModel searchViewModel;

    RecyclerView recyclerView;
    SearchRecyclerViewAdapter adapter;
    ArrayList<Search> searchList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        searchList = new ArrayList<>();
        recyclerView = root.findViewById(R.id.recyclerViewSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SearchRecyclerViewAdapter(getContext(), searchList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


        SearchView searchView = root.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadSearchResult(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return root;
    }

    void loadSearchResult(String query) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASEURL).addConverterFactory(GsonConverterFactory.create()).build();
        GeniusService geniusService = retrofit.create(GeniusService.class);
        Call<List<Search>> call = geniusService.searchSong(query);
        call.enqueue(new Callback<List<Search>>() {
            @Override
            public void onResponse(Call<List<Search>> call, Response<List<Search>> response) {
                if(!response.isSuccessful()) {
                    System.out.println("Server Error");
                } else {
                    List<Search> searches = response.body();
                    System.out.println(searches.toString());
                    searchList.clear();
                    searchList.addAll(searches);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Search>> call, Throwable t) {
                System.out.println("Internet Error");
            }
        });
    }

    public void showLyrics(int id, String song) {
        Bundle bun = new Bundle();
        bun.putString("id", String.valueOf(id));
        bun.putString("title", song);
        LyricsFragment lyricsfg = new LyricsFragment();
        lyricsfg.setArguments(bun);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.search_layout, lyricsfg, "findThisFragment")
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onItemClick(View view, int position) {
        Search search = adapter.getItem(position);
        showLyrics(search.getId(), search.getTitle() + " - " + search.getArtist());
    }
}