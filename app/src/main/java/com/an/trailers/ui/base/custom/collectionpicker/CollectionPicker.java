package com.an.trailers.ui.base.custom.collectionpicker;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.an.trailers.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CollectionPicker extends LinearLayout {

    private List<String> genresList = Arrays.asList("#febf9b", "#f47f87", "#6ac68d", "#fbe0a5");

    private ViewTreeObserver mViewTreeObserver;
    private LayoutInflater mInflater;

    private List<String> mItems = new ArrayList<>();
    private LinearLayout mRow;
    private HashMap<String, Object> mCheckedItems = new HashMap<>();
    private OnItemClickListener mClickListener;
    private int mWidth;
    private int mItemMargin = 10;
    private int textPaddingLeft = 5;
    private int textPaddingRight = 5;
    private int textPaddingTop = 5;
    private int texPaddingBottom = 5;
    private int mAddIcon = android.R.drawable.ic_menu_add;
    private int mCancelIcon = android.R.drawable.ic_menu_close_clear_cancel;
    private int mLayoutBackgroundColorNormal = R.color.blue;
    private int mLayoutBackgroundColorPressed = R.color.red;
    private int mTextColor = android.R.color.white;
    private int mRadius = 5;
    private boolean mInitialized;
    private Typeface tf;

    private boolean simplifiedTags;
    private boolean useRandomColor;

    public CollectionPicker(Context context) {
        this(context, null);
    }

    public CollectionPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    @SuppressLint("ResourceAsColor")
    public CollectionPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.CollectionPicker);
        this.mItemMargin = (int) typeArray.getDimension(R.styleable.CollectionPicker_cp_itemMargin, dpToPx(this.getContext(), mItemMargin));
        this.textPaddingLeft = (int) typeArray.getDimension(R.styleable.CollectionPicker_cp_textPaddingLeft, dpToPx(this.getContext(), textPaddingLeft));
        this.textPaddingRight = (int) typeArray.getDimension(R.styleable.CollectionPicker_cp_textPaddingRight, dpToPx(this.getContext(), textPaddingRight));
        this.textPaddingTop = (int) typeArray.getDimension(R.styleable.CollectionPicker_cp_textPaddingTop, dpToPx(this.getContext(), textPaddingTop));
        this.texPaddingBottom = (int) typeArray.getDimension(R.styleable.CollectionPicker_cp_textPaddingBottom, dpToPx(this.getContext(), texPaddingBottom));
        this.mAddIcon = typeArray.getResourceId(R.styleable.CollectionPicker_cp_addIcon, mAddIcon);
        this.mCancelIcon = typeArray.getResourceId(R.styleable.CollectionPicker_cp_cancelIcon, mCancelIcon);
        this.mLayoutBackgroundColorNormal = typeArray.getColor(R.styleable.CollectionPicker_cp_itemBackgroundNormal, mLayoutBackgroundColorNormal);
        this.mLayoutBackgroundColorPressed = typeArray.getColor(R.styleable.CollectionPicker_cp_itemBackgroundPressed, mLayoutBackgroundColorPressed);
        this.mRadius = (int) typeArray.getDimension(R.styleable.CollectionPicker_cp_itemRadius, mRadius);
        this.mTextColor = typeArray.getColor(R.styleable.CollectionPicker_cp_itemTextColor, mTextColor);
        this.simplifiedTags = typeArray.getBoolean(R.styleable.CollectionPicker_cp_simplified, false);
        this.tf = ResourcesCompat.getFont(getContext(), R.font.gt_medium);
        typeArray.recycle();

        setOrientation(VERTICAL);
        setGravity(Gravity.LEFT);

        mViewTreeObserver = getViewTreeObserver();
        mViewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!mInitialized) {
                    mInitialized = true;
                    drawItemView();
                }
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
    }

    /**
     * Selected flags
     */
    public void setCheckedItems(HashMap<String, Object> checkedItems) {
        mCheckedItems = checkedItems;
    }

    public HashMap<String, Object> getCheckedItems() {
        return mCheckedItems;
    }

    public void drawItemView() {
        if (!mInitialized) {
            return;
        }

        clearUi();

        float totalPadding = getPaddingLeft() + getPaddingRight();
        int indexFrontView = 0;

        LayoutParams itemParams = getItemLayoutParams();

        for (int i = 0; i < mItems.size(); i++) {
            final String item = mItems.get(i);

            final int position = i;
            final View itemLayout = createItemView(item);


            TextView itemTextView = (TextView) itemLayout.findViewById(R.id.item_text);
            itemTextView.setAllCaps(true);
            itemTextView.setTypeface(tf);
            itemTextView.setTextSize(10);
            itemTextView.setText(item);
            itemTextView.setPadding(textPaddingLeft, textPaddingTop, textPaddingRight,
                    texPaddingBottom);
            itemTextView.setTextColor(getResources().getColor(mTextColor));

            float itemWidth = itemTextView.getPaint().measureText(item) + textPaddingLeft
                    + textPaddingRight;

            itemWidth += dpToPx(getContext(), 20) + textPaddingLeft
                    + textPaddingRight;
            if (mWidth <= (itemWidth + totalPadding)) {
                totalPadding = getPaddingLeft() + getPaddingRight();
                indexFrontView = i;
                addItemView(itemLayout, itemParams, true, i);
            } else {
                if (i != indexFrontView) {
                    itemParams.rightMargin = mItemMargin;
                    totalPadding += mItemMargin;
                }
                addItemView(itemLayout, itemParams, false, i);
            }
            totalPadding += itemWidth;
        }
        // }
    }

    private View createItemView(String s) {
        View view = mInflater.inflate(R.layout.list_item_genre, this, false);
        if (isJellyBeanAndAbove()) {
            view.setBackground(getSelector(s));
        } else {
            view.setBackgroundDrawable(getSelector(s));
        }

        return view;
    }

    private LayoutParams getItemLayoutParams() {
        LayoutParams itemParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        itemParams.bottomMargin = mItemMargin / 2;
        itemParams.topMargin = 0;
        itemParams.rightMargin = mItemMargin;

        return itemParams;
    }

    private int getItemIcon(Boolean isSelected) {
        return isSelected ? mCancelIcon : mAddIcon;
    }

    private void clearUi() {
        removeAllViews();
        mRow = null;
    }

    private void addItemView(View itemView, ViewGroup.LayoutParams chipParams, boolean newLine,
                             int position) {
        if (mRow == null || newLine) {
            mRow = new LinearLayout(getContext());
            mRow.setGravity(Gravity.LEFT);
            mRow.setOrientation(HORIZONTAL);

            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            mRow.setLayoutParams(params);

            addView(mRow);
        }

        mRow.addView(itemView, chipParams);
        animateItemView(itemView, position);
    }

    private StateListDrawable getSelector(String s) {
        return getSelectorNormal();
    }


    @SuppressLint("ResourceAsColor")
    private StateListDrawable getSelectorNormal() {
        StateListDrawable states = new StateListDrawable();

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(mLayoutBackgroundColorPressed);
        gradientDrawable.setCornerRadius(mRadius);

        states.addState(new int[]{android.R.attr.state_pressed}, gradientDrawable);

        gradientDrawable = new GradientDrawable();
        int index = new Random().nextInt(genresList.size());
        if (useRandomColor) mLayoutBackgroundColorNormal = Color.parseColor(genresList.get(index));
        gradientDrawable.setColor(mLayoutBackgroundColorNormal);
        gradientDrawable.setCornerRadius(mRadius);

        states.addState(new int[]{}, gradientDrawable);

        return states;
    }

    public void setSelector(int colorCode) {
        this.mLayoutBackgroundColorNormal = colorCode;
        getSelectorNormal();
    }

    public boolean isUseRandomColor() {
        return useRandomColor;
    }

    public void setUseRandomColor(boolean useRandomColor) {
        this.useRandomColor = useRandomColor;
    }


    @SuppressLint("ResourceAsColor")
    private StateListDrawable getSelectorSelected() {
        StateListDrawable states = new StateListDrawable();
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(mLayoutBackgroundColorNormal);
        gradientDrawable.setCornerRadius(mRadius);

        states.addState(new int[]{android.R.attr.state_pressed}, gradientDrawable);

        gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(mLayoutBackgroundColorPressed);
        gradientDrawable.setCornerRadius(mRadius);

        states.addState(new int[]{}, gradientDrawable);

        return states;
    }

    public List<String> getItems() {
        return mItems;
    }

    public void setItems(List<String> items) {
        mItems = items;
        drawItemView();
    }

    public void clearItems() {
        mItems.clear();
    }

    public void setTextColor(int color) {
        this.mTextColor = color;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    private boolean isJellyBeanAndAbove() {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN;
    }

    private void animateView(final View view) {
        view.setScaleY(1f);
        view.setScaleX(1f);

        view.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(100)
                .setStartDelay(0)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        reverseAnimation(view);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }

    private void reverseAnimation(View view) {
        view.setScaleY(1.2f);
        view.setScaleX(1.2f);

        view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(100)
                .setListener(null)
                .start();
    }

    private void animateItemView(View view, int position) {
        long animationDelay = 600;

        animationDelay += position * 30;

        view.setScaleY(0);
        view.setScaleX(0);
        view.animate()
                .scaleY(1)
                .scaleX(1)
                .setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(null)
                .setStartDelay(animationDelay)
                .start();
    }

    private static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public interface OnItemClickListener {
        void onClick(String s, int position);
    }
}