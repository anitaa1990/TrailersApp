package com.an.trailers.ui.base.custom.menu;

import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.an.trailers.R;
import com.an.trailers.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;


public class ViewAnimator<T> {

    private final int ANIMATION_DURATION = 175;
    private final double ASPECT_RATIO_WIDTH = 11.1;
    private final double ASPECT_RATIO_HEIGHT = 6.756;
    private final double ASPECT_RATIO_CONTAINER_HEIGHT = 15.9;

    private int screenWidth;
    private int screenHeight;
    private int selectedPosition = 0;
    private DrawerLayout drawerLayout;
    private List<SlideMenuItem> slideMenuItems;
    private AppCompatActivity appCompatActivity;
    private ViewAnimatorListener animatorListener;

    private List<View> viewList = new ArrayList<>();

    public ViewAnimator(AppCompatActivity activity,
                        List<SlideMenuItem> items,
                        DrawerLayout drawerLayout,
                        ViewAnimatorListener animatorListener) {
        this.appCompatActivity = activity;

        this.slideMenuItems = items;
        this.drawerLayout = drawerLayout;
        this.animatorListener = animatorListener;
        this.screenWidth = AppUtils.getScreenWidth(activity);
        this.screenHeight = AppUtils.getScreenHeight(activity);
    }

    private void setViewsClickable(boolean clickable) {
        animatorListener.updateHomeButton(false);
        for (View view : viewList) {
            view.setEnabled(clickable);
        }
    }

    public void displayMenuContent() {
        setViewsClickable(false);
        viewList.clear();

        double size = slideMenuItems.size();
        for (int i = 0; i < size; i++) {
            final View viewMenu = appCompatActivity.getLayoutInflater().inflate(R.layout.list_item_menu, null);

            updateMenuItemImageView(i, viewMenu);
            updateMenuItemContainerView(i, viewMenu);

            viewList.add(viewMenu);
            animatorListener.addViewToContainer(viewMenu);
            if(i == selectedPosition) {
                viewList.get(i).setSelected(true);
            }

            animateMenuItem((double)i, size);

        }
    }


    private void updateMenuItemImageView(int position,
                                         View viewMenu) {
        ImageView iv = viewMenu.findViewById(R.id.menu_item_image);

        ViewGroup.LayoutParams lp = iv.getLayoutParams();
        Double width = Math.ceil((ASPECT_RATIO_WIDTH * screenWidth)/100);
        Double height = Math.ceil((ASPECT_RATIO_HEIGHT * screenHeight)/100);
        lp.width = width.intValue();
        lp.height = height.intValue();
        iv.setLayoutParams(lp);

        iv.setImageResource(slideMenuItems.get(position).getImageRes());
    }

    private void updateMenuItemContainerView(int position,
                                             View viewMenu) {
        View container = viewMenu.findViewById(R.id.menu_item_container);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        container.setLayoutParams(layoutParams);
        Double containerHeight = Math.ceil((ASPECT_RATIO_CONTAINER_HEIGHT * screenHeight)/100);
        layoutParams.height = containerHeight.intValue();
        container.setLayoutParams(layoutParams);

        viewMenu.setVisibility(View.GONE);
        viewMenu.setEnabled(true);

        viewMenu.setOnClickListener(v -> {
            int[] location = {0, 0};
            v.getLocationOnScreen(location);
            switchItem(v, position, location[1] + v.getHeight() / 2);
        });
    }


    private void animateMenuItem(double position,
                                 double totalSize) {
        final double delay = 3 * ANIMATION_DURATION * (position / totalSize);
        new Handler().postDelayed(() -> {
            if (position < viewList.size()) {
                animateView((int) position);
            }
        }, (long) delay);
    }

    private void hideMenuContent() {
        setViewsClickable(false);
        double size = slideMenuItems.size();

        for (int i = slideMenuItems.size(); i >= 0; i--) {

            final double position = i;
            final double delay = 3 * ANIMATION_DURATION * (position / size);

            new Handler().postDelayed(() -> {
                if (position < viewList.size()) {
                    animateHideView((int) position);
                }
            }, (long) delay);
        }

    }

    private void animateView(int position) {
        final View view = viewList.get(position);
        view.setVisibility(View.VISIBLE);
        FlipAnimation rotation =
                new FlipAnimation(90, 0, 0.0f, view.getHeight() / 2.0f);
        rotation.setDuration(ANIMATION_DURATION);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(rotation);
    }


    private void animateHideView(final int position) {
        final View view = viewList.get(position);
        FlipAnimation rotation =
                new FlipAnimation(0, 90, 0.0f, view.getHeight() / 2.0f);
        rotation.setDuration(ANIMATION_DURATION);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.INVISIBLE);
                if (position == viewList.size() - 1) {
                    animatorListener.updateHomeButton(true);
                    drawerLayout.closeDrawers();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(rotation);
    }


    private void switchItem(View view, int selectedPosition, int topPosition) {
        if(getSelectedPosition() != selectedPosition && selectedPosition != viewList.size() - 1) {
            view.setSelected(true);
            updateSelectedView(view);
            this.selectedPosition = selectedPosition;
             animatorListener.onSwitch(selectedPosition, topPosition);
        }
        hideMenuContent();
    }

    private void updateSelectedView(View view) {
        for(int i =0; i<viewList.size(); i++) {
            if(view.getId() != viewList.get(0).getId()) {
                viewList.get(i).setSelected(false);
            }
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public interface ViewAnimatorListener {
        void onSwitch(int selectedPosition, int topPosition);
        void updateHomeButton(boolean enabled);
        void addViewToContainer(View view);
    }
}