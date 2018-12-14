package com.an.trailers.ui.detail.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast
import com.an.trailers.AppConstants
import com.an.trailers.AppConstants.Companion.INTENT_VIDEO_KEY
import com.an.trailers.R
import com.an.trailers.databinding.VideoActivityBinding
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer


class VideoActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private var videoKey: String? = null
    private var binding: VideoActivityBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialiseView()
    }

    private fun initialiseView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video)
        binding!!.youtubeView.initialize(AppConstants.YOUTUBE_API_KEY, this)
        videoKey = intent.getStringExtra(INTENT_VIDEO_KEY)
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider, youTubePlayer: YouTubePlayer, b: Boolean) {
        youTubePlayer.setFullscreen(true)
        youTubePlayer.loadVideo(videoKey) // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider,
        youTubeInitializationResult: YouTubeInitializationResult
    ) {
        if (youTubeInitializationResult.isUserRecoverableError) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show()
        } else {
            val error = String.format(getString(R.string.player_error), youTubeInitializationResult.toString())
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val RECOVERY_REQUEST = 1
    }
}
