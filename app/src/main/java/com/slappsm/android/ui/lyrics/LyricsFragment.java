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

public class LyricsFragment extends Fragment {
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

}
