package com.an.trailers.ui.detail.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.an.trailers.AppConstants
import com.an.trailers.R
import com.an.trailers.data.remote.model.Cast
import com.an.trailers.data.remote.model.Crew
import com.an.trailers.databinding.CreditListWithItemBinding
import com.squareup.picasso.Picasso
import com.an.trailers.AppConstants.Companion.CREDIT_CAST

class CreditListAdapter : RecyclerView.Adapter<CreditListAdapter.CustomViewHolder> {

    private var type: String
    private var context: Context
    private var casts: List<Cast>
    private var crews: List<Crew>

    private val isCast: Boolean
        get() =  (type.equals(CREDIT_CAST, ignoreCase = true))

    constructor(context: Context, type: String) {
        this.type = type
        this.context = context
        this.casts = emptyList()
        this.crews = emptyList()
    }

    constructor(context: Context, casts: List<Cast>) {
        this.type = CREDIT_CAST
        this.context = context
        this.casts = casts
        this.crews = emptyList()
    }


    constructor(context: Context, type: String, crews: List<Crew>) {
        this.type = type
        this.context = context
        this.casts = emptyList()
        this.crews = crews
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = CreditListWithItemBinding.inflate(layoutInflater, parent, false)
        return CustomViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        if (isCast) {
            val cast = getCastItem(position)
            Picasso.get().load(String.format(AppConstants.IMAGE_URL, cast.profilePath))
                .error(R.drawable.ic_placeholder_profile)
                .into(holder.binding.profileImage)
            holder.binding.txtName.text = cast.name
            holder.binding.txtInfo.text = cast.character

        } else {
            val crew = getCrewItem(position)
            Picasso.get().load(String.format(AppConstants.IMAGE_URL, crew.profilePath))
                .error(R.drawable.ic_placeholder_profile)
                .into(holder.binding.profileImage)
            holder.binding.txtName.text = crew.name
            holder.binding.txtInfo.text = crew.job
        }
    }

    override fun getItemCount(): Int {
        return if (isCast) casts.size else crews.size
    }

    private fun getCastItem(position: Int): Cast {
        return casts[position]
    }

    private fun getCrewItem(position: Int): Crew {
        return crews[position]
    }


    inner class CustomViewHolder(internal val binding: CreditListWithItemBinding) : RecyclerView.ViewHolder(binding.root)
}
