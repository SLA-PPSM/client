package com.slappsm.android.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.slappsm.android.R;
import com.slappsm.android.model.Search;

import java.util.List;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {

    private final List<Search> mData;
    private final LayoutInflater mInflater;
    private SearchRecyclerViewAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    SearchRecyclerViewAdapter(Context context, List<Search> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public SearchRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.search_recyclerview_row, parent, false);
        return new SearchRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(SearchRecyclerViewAdapter.ViewHolder holder, int position) {
        Search search = mData.get(position);
        holder.textSearchSong.setText(search.getTitle() + " - " + search.getArtist());
        //Picasso.get().setLoggingEnabled(true);
        //Picasso.get().load(search.getAvatar()).into(holder.imageAvatar);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textSearchSong;
        ImageView imageSearchSong;

        ViewHolder(View itemView) {
            super(itemView);
            textSearchSong = itemView.findViewById(R.id.textSearchSong);
            imageSearchSong = itemView.findViewById(R.id.imageSearchSong);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Search getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(SearchRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
