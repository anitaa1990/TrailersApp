package com.an.trailers.ui.detail.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.an.trailers.data.remote.model.Review;
import com.an.trailers.databinding.ReviewItemBinding;

import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.CustomViewHolder> {

    private List<Review> reviews;
    public ReviewListAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ReviewItemBinding itemBinding = ReviewItemBinding.inflate(layoutInflater, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(itemBinding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int i) {
        Review review = getItem(i);
        holder.binding.itemReviewTitle.setText(review.getAuthor());
        holder.binding.itemReviewDesc.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public Review getItem(int position) {
        return reviews.get(position);
    }


    protected class CustomViewHolder extends RecyclerView.ViewHolder {
        private ReviewItemBinding binding;

        public CustomViewHolder(ReviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
