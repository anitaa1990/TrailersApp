package com.an.trailers.ui.detail.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.an.trailers.data.local.entity.MovieEntity;
import com.an.trailers.databinding.SimilarMoviesListItemBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SimilarMoviesListAdapter extends RecyclerView.Adapter<SimilarMoviesListAdapter.CustomViewHolder> {

    private Activity activity;
    private List<MovieEntity> movies;
    public SimilarMoviesListAdapter(Activity activity, List<MovieEntity> movies) {
        this.activity = activity;
        this.movies = movies;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        SimilarMoviesListItemBinding itemBinding = SimilarMoviesListItemBinding.inflate(layoutInflater, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(itemBinding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        MovieEntity movie = getItem(position);
        String imageUrl = movie.getPosterPath();
        Picasso.get().load(imageUrl).into(holder.binding.itemImg);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public MovieEntity getItem(int position) {
        return movies.get(position);
    }

    protected class CustomViewHolder extends RecyclerView.ViewHolder {
        private SimilarMoviesListItemBinding binding;

        public CustomViewHolder(SimilarMoviesListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}