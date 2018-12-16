package com.an.trailers.ui.main.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.an.trailers.R;
import com.an.trailers.data.local.entity.MovieEntity;
import com.an.trailers.databinding.MoviesListItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.CustomViewHolder> {

    private Activity activity;
    private List<MovieEntity> movies;
    public MoviesListAdapter(Activity activity) {
        this.activity = activity;
        this.movies = new ArrayList<>();
    }

    @NonNull
    @Override
    public MoviesListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MoviesListItemBinding itemBinding = MoviesListItemBinding.inflate(layoutInflater, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(itemBinding);
        return viewHolder;
    }

    public void setItems(List<MovieEntity> movies) {
        int startPosition = this.movies.size();
        this.movies.addAll(movies);
        notifyItemRangeChanged(startPosition, movies.size());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public MovieEntity getItem(int position) {
        return movies.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesListAdapter.CustomViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    protected class CustomViewHolder extends RecyclerView.ViewHolder {

        private MoviesListItemBinding binding;
        public CustomViewHolder(MoviesListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;

            itemView.setLayoutParams(new RecyclerView.LayoutParams(new Float(width * 0.85f).intValue(),
                    RecyclerView.LayoutParams.WRAP_CONTENT));
        }

        public void bindTo(MovieEntity movie) {
            Picasso.get().load(movie.getPosterPath())
                    .placeholder(R.drawable.ic_placeholder)
                    .into(binding.image);
        }
    }
}
