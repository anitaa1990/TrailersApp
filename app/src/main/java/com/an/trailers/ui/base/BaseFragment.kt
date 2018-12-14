package com.an.trailers.ui.base

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import com.an.trailers.AppConstants

open class BaseFragment : Fragment(), AppConstants {

    protected lateinit var activity: Activity

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.activity = context as Activity
    }
}
