package com.an.trailers.ui.main.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.RadioGroup
import com.an.trailers.R
import com.an.trailers.databinding.MainActivityBinding
import com.an.trailers.ui.base.BaseActivity
import com.an.trailers.ui.base.custom.menu.MenuDrawerToggle
import com.an.trailers.utils.AppUtils
import com.an.trailers.utils.NavigationUtils
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : BaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var binding: MainActivityBinding
    private lateinit var menuDrawerToggle: MenuDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initialiseView()
    }

    private fun initialiseView() {
        setSupportActionBar(binding.includedLayout.toolbar)
        supportActionBar!!.setHomeButtonEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.includedLayout.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            this.onCheckedChanged(radioGroup, i)
        }
        menuDrawerToggle = object : MenuDrawerToggle(
            this,
            binding.drawerLayout, binding.includedLayout.toolbar,
            binding.leftDrawer, R.string.drawer_open, R.string.drawer_close,
            AppUtils.getMenuList(applicationContext)
        ) {
            override fun onSwitch(selectedPosition: Int, topPosition: Int) {
                onCheckedChanged(
                    binding.includedLayout.radioGroup,
                    binding.includedLayout.radioGroup.checkedRadioButtonId
                )
            }
        }
        menuDrawerToggle.syncState()
        binding.drawerLayout.addDrawerListener(menuDrawerToggle)
    }


    fun displayToolbar() {
        binding.includedLayout.toolbar.visibility = View.VISIBLE
    }

    fun hideToolbar() {
        binding.includedLayout.toolbar.visibility = View.INVISIBLE
    }

    fun updateBackground(url: String?) {
        binding.overlayLayout.updateCurrentBackground(url)
    }

    fun clearBackground() {
        binding.overlayLayout.clearImage()
    }

    fun handleSearchIconClick(view: View) {
        when (binding.includedLayout.radioGroup.checkedRadioButtonId) {
            R.id.btn_movie -> NavigationUtils.redirectToMovieSearchScreen(this)

            R.id.btn_tv -> NavigationUtils.redirectToTvSearchScreen(this)
        }
    }

    fun onCheckedChanged(radioGroup: RadioGroup, i: Int) {
        binding.overlayLayout.clearImage()
        when (i) {
            R.id.btn_movie -> NavigationUtils.replaceFragment(
                this,
                R.id.moviesListFragment,
                menuDrawerToggle.selectedPosition
            )

            R.id.btn_tv -> NavigationUtils.replaceFragment(
                this,
                R.id.tvListFragment,
                menuDrawerToggle.selectedPosition
            )
        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        menuDrawerToggle.onDestroy()
    }

    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }
}
