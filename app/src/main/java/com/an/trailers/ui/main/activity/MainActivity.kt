package com.an.trailers.ui.main.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RadioGroup;

import com.an.trailers.R;
import com.an.trailers.databinding.MainActivityBinding;
import com.an.trailers.ui.base.BaseActivity;
import com.an.trailers.ui.base.custom.menu.MenuDrawerToggle;
import com.an.trailers.utils.AppUtils;
import com.an.trailers.utils.NavigationUtils;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends BaseActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;


    private MainActivityBinding binding;
    private MenuDrawerToggle menuDrawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initialiseView();
    }

    private void initialiseView() {
        setSupportActionBar(binding.includedLayout.toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.includedLayout.radioGroup.setOnCheckedChangeListener(this::onCheckedChanged);
        menuDrawerToggle = new MenuDrawerToggle(this,
                binding.drawerLayout, binding.includedLayout.toolbar,
                binding.leftDrawer, R.string.drawer_open, R.string.drawer_close,
                AppUtils.getMenuList(getApplicationContext())) {
            @Override
            public void onSwitch(int selectedPosition, int topPosition) {
                onCheckedChanged(binding.includedLayout.radioGroup, binding.includedLayout.radioGroup.getCheckedRadioButtonId());
            }
        };
        menuDrawerToggle.syncState();
        binding.drawerLayout.addDrawerListener(menuDrawerToggle);
    }


    public void displayToolbar() {
        binding.includedLayout.toolbar.setVisibility(View.VISIBLE);
    }

    public void hideToolbar() {
        binding.includedLayout.toolbar.setVisibility(View.INVISIBLE);
    }

    public void updateBackground(String url) {
        binding.overlayLayout.updateCurrentBackground(url);
    }

    public void clearBackground() {
        binding.overlayLayout.clearImage();
    }

    public void handleSearchIconClick(View view) {
        switch (binding.includedLayout.radioGroup.getCheckedRadioButtonId()) {
            case R.id.btn_movie:
                NavigationUtils.redirectToMovieSearchScreen(this);
                break;

            case R.id.btn_tv:
                NavigationUtils.redirectToTvSearchScreen(this);
                break;
        }
    }

    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.btn_movie:
                NavigationUtils.replaceFragment(this, R.id.moviesListFragment, menuDrawerToggle.getSelectedPosition());
                break;

            case R.id.btn_tv:
                NavigationUtils.replaceFragment(this, R.id.tvListFragment, menuDrawerToggle.getSelectedPosition());
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        menuDrawerToggle.onDestroy();
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
