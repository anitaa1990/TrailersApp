package com.an.trailers.ui.detail.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.an.trailers.data.local.entity.TvEntity;
import com.an.trailers.databinding.SimilarMoviesListItemBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SimilarTvListAdapter extends RecyclerView.Adapter<SimilarTvListAdapter.CustomViewHolder> {

    private Activity activity;
    private List<TvEntity> tvEntities;
    public SimilarTvListAdapter(Activity activity, List<TvEntity> tvEntities) {
        this.activity = activity;
        this.tvEntities = tvEntities;
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
        TvEntity item = getItem(position);
        String imageUrl = item.getPosterPath();
        Picasso.get().load(imageUrl).into(holder.binding.itemImg);
    }

    @Override
    public int getItemCount() {
        return tvEntities.size();
    }

    public TvEntity getItem(int position) {
        return tvEntities.get(position);
    }

    protected class CustomViewHolder extends RecyclerView.ViewHolder {
        private SimilarMoviesListItemBinding binding;

        public CustomViewHolder(SimilarMoviesListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}