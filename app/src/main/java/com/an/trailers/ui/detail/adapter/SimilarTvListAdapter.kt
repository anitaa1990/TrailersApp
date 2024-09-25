package com.an.trailers.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.an.trailers.data.local.entity.TvEntity
import com.an.trailers.databinding.SimilarMoviesListItemBinding
import com.squareup.picasso.Picasso

class SimilarTvListAdapter(private val tvEntities: List<TvEntity>) :
    RecyclerView.Adapter<SimilarTvListAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = SimilarMoviesListItemBinding.inflate(layoutInflater, parent, false)
        return CustomViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = getItem(position)
        val imageUrl = item.getFormattedPosterPath()
        Picasso.get().load(imageUrl).into(holder.binding.itemImg)
    }

    override fun getItemCount(): Int {
        return tvEntities.size
    }

    fun getItem(position: Int): TvEntity {
        return tvEntities[position]
    }

    inner class CustomViewHolder(internal val binding: SimilarMoviesListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
