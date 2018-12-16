package com.an.trailers.ui.main.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.an.trailers.R;
import com.an.trailers.data.local.entity.TvEntity;
import com.an.trailers.databinding.MoviesListItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TvListAdapter extends RecyclerView.Adapter<TvListAdapter.CustomViewHolder> {

    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    private Activity activity;
    private List<TvEntity> tvEntities;
    public TvListAdapter(Activity activity) {
        this.activity = activity;
        this.tvEntities = new ArrayList<>();
    }

    @NonNull
    @Override
    public TvListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MoviesListItemBinding itemBinding = MoviesListItemBinding.inflate(layoutInflater, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(itemBinding);
        return viewHolder;
    }

    public void setItems(List<TvEntity> tvEntities) {
        int startPosition = this.tvEntities.size();
        this.tvEntities.addAll(tvEntities);
        notifyItemRangeChanged(startPosition, tvEntities.size());
    }

    @Override
    public int getItemCount() {
        return tvEntities.size();
    }

    public TvEntity getItem(int position) {
        return tvEntities.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull TvListAdapter.CustomViewHolder holder, int position) {
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

        public void bindTo(TvEntity tvEntity) {
            Picasso.get().load(tvEntity.getPosterPath())
                    .placeholder(R.drawable.ic_placeholder)
                    .into(binding.image);
        }
    }
}
