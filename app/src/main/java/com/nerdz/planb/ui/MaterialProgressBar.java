package com.nerdz.planb.ui;
import android.content.Context;
import android.util.AttributeSet;


/**
 * Created by orcunozyurt on 12/2/17.
 */



public class MaterialProgressBar extends android.support.v7.widget.AppCompatImageView {
    private MaterialProgressDrawable mDrawable;

    public MaterialProgressBar(Context context) {
        super(context);
        setupDrawable(context);
    }

    public MaterialProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawable(context);
    }

    public MaterialProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupDrawable(context);
    }

    private void setupDrawable(Context context) {
        this.mDrawable = new MaterialProgressDrawable(context, this);
        this.mDrawable.setColorSchemeColors(-1);
        this.mDrawable.setBackgroundColor(0);
        this.mDrawable.setAlpha(255);
        setImageDrawable(this.mDrawable);
        this.mDrawable.start();
    }

    public void setVisibility(int visibility) {
        if (visibility == INVISIBLE || visibility == GONE) {
            clearAnimation();
        } else {
            setupDrawable(getContext());
        }
        super.setVisibility(visibility);
    }
}