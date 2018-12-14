package com.an.trailers.ui.base.custom.menu

import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout


abstract class MenuDrawerToggle(
    private var activity: AppCompatActivity?,
    drawerLayout: DrawerLayout,
    toolbar: Toolbar,
    private val layoutView: LinearLayout,
    openDrawerContentDescRes: Int,
    closeDrawerContentDescRes: Int,
    slideMenuItems: List<SlideMenuItem>

) : ActionBarDrawerToggle(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes),
    ViewAnimator.ViewAnimatorListener {

    private var viewAnimator: ViewAnimator = ViewAnimator(
        activity!!, slideMenuItems, drawerLayout, this)

    val selectedPosition: Int
        get() = viewAnimator.selectedPosition

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        super.onDrawerSlide(drawerView, slideOffset)
        if (slideOffset > 0.6 && layoutView.childCount == 0) {
            viewAnimator.displayMenuContent()
        }
    }

    override fun onDrawerClosed(drawerView: View) {
        super.onDrawerClosed(drawerView)
        layoutView.removeAllViews()
        layoutView.invalidate()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun updateHomeButton(enabled: Boolean) {
        activity!!.supportActionBar!!.setHomeButtonEnabled(enabled)
    }

    override fun addViewToContainer(view: View) {
        layoutView.addView(view)
    }

    fun onDestroy() {
        activity = null
    }
}
