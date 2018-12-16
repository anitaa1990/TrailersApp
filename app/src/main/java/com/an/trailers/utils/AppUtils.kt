package com.an.trailers.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.an.trailers.AppConstants.Companion.MOVIE_STATUS_RELEASED
import com.an.trailers.R
import com.an.trailers.data.remote.model.Genre
import com.an.trailers.ui.base.custom.menu.SlideMenuItem

import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*
import com.an.trailers.data.local.entity.MovieEntity
import com.an.trailers.data.local.entity.TvEntity


object AppUtils {

    private fun getDate(aDate: String?): Date {
        val pos = ParsePosition(0)
        val simpledateformat = SimpleDateFormat("yyyy-MM-dd")
        val stringDate = simpledateformat.parse(aDate, pos)
        return stringDate

    }

    fun getFormattedDate(dateString: String?): String? {
        val date = getDate(dateString)
        val cal = Calendar.getInstance()
        cal.time = date

        val day = cal.get(Calendar.DATE)
        return when (day % 10) {
            1 -> SimpleDateFormat("MMMM d'st', yyyy").format(date)
            2 -> SimpleDateFormat("MMMM d'nd', yyyy").format(date)
            3 -> SimpleDateFormat("MMMM d'rd', yyyy").format(date)
            else -> SimpleDateFormat("MMMM d'th', yyyy").format(date)
        }
    }

    fun getGenres(genres: List<Genre>): MutableList<String> {
        val genreNames = ArrayList<String>(genres.size)
        for (obj in genres) {
            genreNames.add(obj.name.toString())
        }
        return genreNames
    }


    fun getScreenWidth(mContext: Context): Int {
        val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val width1: Int
        width1 = if (Build.VERSION.SDK_INT > 12) {
            val size = Point()
            display.getSize(size)
            size.x
        } else {
            display.width
        }

        return width1
    }

    fun getScreenHeight(mContext: Context): Int {
        val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val height1: Int
        height1 = if (Build.VERSION.SDK_INT > 12) {
            val size = Point()
            display.getSize(size)
            size.y
        } else {
            display.height
        }

        return height1
    }


    fun getMenuList(context: Context): List<SlideMenuItem> {
        val slideMenuItems = ArrayList<SlideMenuItem>()
        val menuTitles = Arrays.asList(*context.resources.getStringArray(R.array.menu_names))
        val menuIcons = context.resources.obtainTypedArray(R.array.menu_icons)

        for (i in menuTitles.indices) {
            val slideMenuItem = SlideMenuItem(menuTitles[i], menuIcons.getResourceId(i, -1))
            slideMenuItems.add(slideMenuItem)
        }

        menuIcons.recycle()
        return slideMenuItems
    }

    fun getMoviesByType(type: String,
                        movieEntities: List<MovieEntity>): List<MovieEntity> {
        val finalList: MutableList<MovieEntity> = ArrayList()
        for (movieEntity in movieEntities) {
            var add = false
            if(movieEntity.categoryTypes != null) {
                for (categoryType in movieEntity.categoryTypes!!) {
                    if (type.equals(categoryType, ignoreCase = true)) {
                        add = true
                    }
                }
            }
            if (add) finalList.add(movieEntity)
        }
        return finalList
    }


    fun getTvsByType(type: String,
                     tvEntities: List<TvEntity>): List<TvEntity> {
        val finalList: MutableList<TvEntity> = ArrayList()
        for (tvEntity in tvEntities) {
            var add = false
            if(tvEntity.categoryTypes != null) {
                for (categoryType in tvEntity.categoryTypes!!) {
                    if (type.equals(categoryType, ignoreCase = true)) {
                        add = true
                    }
                }
            }
            if (add) finalList.add(tvEntity)
        }
        return finalList
    }

    fun getRunTimeInMins(
        status: String?,
        runtime: Long?,
        releaseDate: String?
    ): String? {

        return if (MOVIE_STATUS_RELEASED.equals(status) && runtime != null)
            String.format("%s mins", runtime.toString())
        else getFormattedDate(releaseDate)
    }

    fun getSeasonNumber(seasonNumber: Long?): String {
        return String.format("Season %s", seasonNumber)
    }

    fun closeKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isActive) {
            if (activity.currentFocus != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
            }
        }
    }
}
