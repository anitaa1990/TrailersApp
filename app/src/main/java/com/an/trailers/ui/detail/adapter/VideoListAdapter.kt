package com.an.trailers.ui.detail.adapter


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.an.trailers.AppConstants
import com.an.trailers.R
import com.an.trailers.data.remote.model.Video
import com.an.trailers.databinding.VideoListItemBinding
import com.an.trailers.utils.AppUtils
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView

class VideoListAdapter(context: Context, private val videoList: List<Video>) :
    RecyclerView.Adapter<VideoListAdapter.CustomViewHolder>() {

    private val screenWidth: Int = AppUtils.getScreenWidth(context)
    private val screenHeight: Int = AppUtils.getScreenHeight(context)
    private val ASPECT_RATIO_WIDTH = 61.1
    private val ASPECT_RATIO_HEIGHT = 20.27

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = VideoListItemBinding.inflate(layoutInflater, parent, false)
        return CustomViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        holder.binding.youtubeThumbnail.initialize(
            AppConstants.YOUTUBE_API_KEY,
            object : YouTubeThumbnailView.OnInitializedListener {
                override fun onInitializationSuccess(
                    youTubeThumbnailView: YouTubeThumbnailView,
                    youTubeThumbnailLoader: YouTubeThumbnailLoader
                ) {
                    val item = getItem(position)
                    youTubeThumbnailLoader.setVideo(item.key)
                    youTubeThumbnailView.setImageBitmap(null)

                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(object :
                        YouTubeThumbnailLoader.OnThumbnailLoadedListener {

                        override fun onThumbnailLoaded(youTubeThumbnailView: YouTubeThumbnailView, s: String) {

                            youTubeThumbnailView.visibility = View.VISIBLE
                            holder.binding.vidFrame.visibility = View.VISIBLE
                            holder.binding.btnPlay.setImageResource(R.drawable.ic_play)
                            youTubeThumbnailLoader.release()
                        }

                        override fun onThumbnailError(
                            youTubeThumbnailView: YouTubeThumbnailView,
                            errorReason: YouTubeThumbnailLoader.ErrorReason
                        ) {

                        }
                    })
                }

                override fun onInitializationFailure(
                    youTubeThumbnailView: YouTubeThumbnailView,
                    youTubeInitializationResult: YouTubeInitializationResult
                ) {
                    //write something for failure
                }
            })
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    fun getItem(position: Int): Video {
        return videoList[position]
    }

    inner class CustomViewHolder(internal val binding: VideoListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {

            val lp = binding.youtubeThumbnail.getLayoutParams()

            val width = Math.ceil(ASPECT_RATIO_WIDTH * screenWidth / 100)
            val height = Math.ceil(ASPECT_RATIO_HEIGHT * screenHeight / 100)
            lp.width = width.toInt()
            lp.height = height.toInt()
            binding.youtubeThumbnail.setLayoutParams(lp)
        }
    }
}
