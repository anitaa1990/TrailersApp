package com.an.trailers.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.an.trailers.AppConstants
import com.an.trailers.AppConstants.Companion.INTENT_CATEGORY
import com.an.trailers.AppConstants.Companion.INTENT_MOVIE
import com.an.trailers.AppConstants.Companion.INTENT_VIDEO_KEY
import com.an.trailers.R
import com.an.trailers.data.local.entity.MovieEntity
import com.an.trailers.data.local.entity.TvEntity
import com.an.trailers.ui.detail.activity.MovieDetailActivity
import com.an.trailers.ui.detail.activity.TvDetailActivity
import com.an.trailers.ui.detail.activity.VideoActivity
import com.an.trailers.ui.search.activity.MovieSearchActivity
import com.an.trailers.ui.search.activity.TvSearchActivity

class NavigationUtils : AppConstants {
    companion object {

        fun redirectToMovieSearchScreen(activity: Activity) {
            val intent = Intent(activity, MovieSearchActivity::class.java)
            activity.startActivity(intent)
        }

        fun redirectToTvSearchScreen(activity: Activity) {
            val intent = Intent(activity, TvSearchActivity::class.java)
            activity.startActivity(intent)
        }

        fun redirectToVideoScreen(
            context: Context,
            videoKey: String
        ) {
            val intent = Intent(context, VideoActivity::class.java)
            intent.putExtra(INTENT_VIDEO_KEY, videoKey)
            context.startActivity(intent)
        }


        fun redirectToDetailScreen(
            activity: Activity,
            movie: MovieEntity,
            options: ActivityOptionsCompat
        ) {
            val intent = Intent(activity, MovieDetailActivity::class.java)
            intent.putExtra(INTENT_MOVIE, movie)
            ActivityCompat.startActivity(activity, intent, options.toBundle())
        }

        fun redirectToTvDetailScreen(
            activity: Activity,
            tvEntity: TvEntity,
            options: ActivityOptionsCompat
        ) {
            val intent = Intent(activity, TvDetailActivity::class.java)
            intent.putExtra(INTENT_MOVIE, tvEntity)
            ActivityCompat.startActivity(activity, intent, options.toBundle())
        }

        fun replaceFragment(
            activity: Activity,
            navId: Int,
            selectedPosition: Int
        ) {
            val bundle = Bundle()
            bundle.putInt(INTENT_CATEGORY, selectedPosition)
            Navigation.findNavController(activity, R.id.fragment_nav_host)
                .navigate(
                    navId, bundle, NavOptions.Builder()
                        .setEnterAnim(R.anim.flip_right_in)
                        .setExitAnim(R.anim.flip_right_out)
                        .setPopEnterAnim(R.anim.flip_left_in)
                        .setPopExitAnim(R.anim.flip_left_out)
                        .build()
                )
        }
    }
}
