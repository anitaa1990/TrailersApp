package com.an.trailers.ui.detail.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.an.trailers.data.local.entity.MovieEntity
import com.an.trailers.databinding.SimilarMoviesListItemBinding
import com.squareup.picasso.Picasso

class SimilarMoviesListAdapter(private val activity: Activity, private val movies: List<MovieEntity>) :
    RecyclerView.Adapter<SimilarMoviesListAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = SimilarMoviesListItemBinding.inflate(layoutInflater, parent, false)
        return CustomViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val movie = getItem(position)
        val imageUrl = movie.getFormattedPosterPath()
        Picasso.get().load(imageUrl).into(holder.binding.itemImg)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun getItem(position: Int): MovieEntity {
        return movies[position]
    }

    inner class CustomViewHolder(internal val binding: SimilarMoviesListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}