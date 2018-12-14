package com.an.trailers.ui.base.custom;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.an.trailers.utils.AnimUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class BackgroundSwitcherView extends ImageSwitcher {
    private final int[] NORMAL_ORDER = new int[]{0, 1};

    private int bgImageGap;
    private int bgImageWidth;

    private Animation bgImageInLeftAnimation;
    private Animation bgImageOutLeftAnimation;

    private Animation bgImageInRightAnimation;
    private Animation bgImageOutRightAnimation;

    private int movementDuration = 500;
    private int widthBackgroundImageGapPercent = 12;

    private AnimationDirection currentAnimationDirection;

    public BackgroundSwitcherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateAndInit(context);
    }

    public BackgroundSwitcherView(Context context) {
        super(context);
        inflateAndInit(context);
    }

    private void inflateAndInit(final Context context) {
        setChildrenDrawingOrderEnabled(true);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        bgImageGap = (displayMetrics.widthPixels / 100) * widthBackgroundImageGapPercent;
        bgImageWidth = displayMetrics.widthPixels + bgImageGap * 2;

        this.setFactory(() -> {
            ImageView myView = new ImageView(context);
            myView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            myView.setLayoutParams(new LayoutParams(bgImageWidth, LayoutParams.MATCH_PARENT));
            myView.setTranslationX(-bgImageGap);
            return myView;
        });

        bgImageInLeftAnimation = AnimUtils.createBgImageInAnimation(bgImageGap, 0, movementDuration);
        bgImageOutLeftAnimation = AnimUtils.createBgImageOutAnimation(0, -bgImageGap, movementDuration);
        bgImageInRightAnimation = AnimUtils.createBgImageInAnimation(-bgImageGap, 0, movementDuration);
        bgImageOutRightAnimation = AnimUtils.createBgImageOutAnimation(0, bgImageGap, movementDuration);
    }


    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return NORMAL_ORDER[i];
    }

    private synchronized void setImageBitmapWithAnimation(Bitmap newBitmap, AnimationDirection animationDirection) {
        if (animationDirection == AnimationDirection.LEFT) {
            this.setInAnimation(bgImageInLeftAnimation);
            this.setOutAnimation(bgImageOutLeftAnimation);
            this.setImageBitmap(newBitmap);

        } else if (animationDirection == AnimationDirection.RIGHT) {
            this.setInAnimation(bgImageInRightAnimation);
            this.setOutAnimation(bgImageOutRightAnimation);
            this.setImageBitmap(newBitmap);
        }
    }



    public void updateCurrentBackground(String imageUrl) {

        this.currentAnimationDirection = AnimationDirection.RIGHT;
        ImageView image = (ImageView) this.getNextView();
        image.setImageDrawable(null);
        showNext();

        if(imageUrl == null) return;

        Picasso.get().load(imageUrl)
                .noFade().noPlaceholder()
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        setImageBitmapWithAnimation(bitmap, currentAnimationDirection);
                    }
                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        System.out.println("@#@#@#@#@" + e.getMessage());
                    }
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) { }
                });
    }


    private void setImageBitmap(Bitmap bitmap) {
        ImageView image = (ImageView) this.getNextView();
        image.setImageDrawable(null);

        int duration = 0;
        animate().alpha(0.0f).setDuration(duration).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                image.setImageBitmap(bitmap);
                new Handler().postDelayed(() -> animate().alpha(0.4f).setDuration(duration), 200);
            }

            @Override
            public void onAnimationCancel(Animator animation) { }

            @Override
            public void onAnimationRepeat(Animator animation) { }
        });
        showNext();
    }

    public void clearImage() {
        ImageView image = (ImageView) this.getNextView();
        image.setImageDrawable(null);
        showNext();
    }

    public enum AnimationDirection {
        LEFT, RIGHT
    }
}
