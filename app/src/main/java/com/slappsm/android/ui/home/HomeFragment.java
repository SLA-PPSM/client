package com.slappsm.android.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.slappsm.android.MainActivity;
import com.slappsm.android.R;
import com.slappsm.android.ui.lyrics.LyricsFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView title;
    private String username;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        username= MainActivity.username;

        return root;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = getView().findViewById(R.id.textViewLyricsCurrSong);
        title.setOnClickListener(v -> showLyrics(v));
        System.out.println(username);
    }

    public void showLyrics(View v) {

        Bundle bun = new Bundle();
        bun.putString("title", title.getText().toString());
        LyricsFragment lyricsfg = new LyricsFragment();
        lyricsfg.setArguments(bun);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_layout, lyricsfg, "findThisFragment")
                .addToBackStack(null)
                .commit();

    }
}