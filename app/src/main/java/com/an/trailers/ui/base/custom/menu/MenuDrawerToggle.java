package com.an.trailers.ui.base.custom.menu;

import android.content.res.Configuration;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.List;


public abstract class MenuDrawerToggle extends ActionBarDrawerToggle implements ViewAnimator.ViewAnimatorListener {

    private AppCompatActivity activity;
    private ViewAnimator viewAnimator;
    private LinearLayout layoutView;
    public MenuDrawerToggle(AppCompatActivity activity,
                            DrawerLayout drawerLayout,
                            Toolbar toolbar,
                            LinearLayout layoutView,
                            int openDrawerContentDescRes,
                            int closeDrawerContentDescRes,
                            List<SlideMenuItem> slideMenuItems) {

        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        this.activity = activity;
        this.layoutView = layoutView;
        viewAnimator =new ViewAnimator(activity, slideMenuItems, drawerLayout, this);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, slideOffset);
        if (slideOffset > 0.6 && layoutView.getChildCount() == 0) {
            viewAnimator.displayMenuContent();
        }
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        layoutView.removeAllViews();
        layoutView.invalidate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void updateHomeButton(boolean enabled) {
        activity.getSupportActionBar().setHomeButtonEnabled(enabled);
    }

    @Override
    public void addViewToContainer(View view) {
        layoutView.addView(view);
    }

    public void onDestroy() {
        activity = null;
    }

    public int getSelectedPosition() {
        return viewAnimator.getSelectedPosition();
    }
}
