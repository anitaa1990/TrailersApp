package com.an.trailers.ui.main.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import com.an.trailers.R
import com.an.trailers.data.local.entity.TvEntity
import com.an.trailers.databinding.MoviesListItemBinding
import com.squareup.picasso.Picasso

import java.util.ArrayList

class TvListAdapter(private val activity: Activity) : RecyclerView.Adapter<TvListAdapter.CustomViewHolder>() {
    private var tvEntities: MutableList<TvEntity> = mutableListOf()

    init {
        this.tvEntities = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvListAdapter.CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = MoviesListItemBinding.inflate(layoutInflater, parent, false)
        return CustomViewHolder(itemBinding)
    }

    fun setItems(tvEntities: List<TvEntity>) {
        val startPosition = this.tvEntities.size
        this.tvEntities.addAll(tvEntities)
        notifyItemRangeChanged(startPosition, tvEntities.size)
    }

    override fun getItemCount(): Int {
        return tvEntities.size
    }

    fun getItem(position: Int): TvEntity {
        return tvEntities[position]
    }

    override fun onBindViewHolder(holder: TvListAdapter.CustomViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    inner class CustomViewHolder(private val binding: MoviesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels

            itemView.layoutParams = RecyclerView.LayoutParams(
                (width * 0.85f).toInt(),
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
        }

        fun bindTo(tvEntity: TvEntity) {
            Picasso.get().load(tvEntity.getFormattedPosterPath())
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.image)
        }
    }
}
