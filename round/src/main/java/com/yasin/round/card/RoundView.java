package com.yasin.round.card;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.yasin.round.R;

/**
 * Created by leo on 17/3/31.
 */

public class RoundView extends FrameLayout {
    /**
     * 阴影的起始颜色
     */
    private int mShadowStartColor;
    /**
     * 阴影的结束颜色
     */
    private int mShadowEndColor;
    /**
     * 圆角半径
     */
    private float mRadius;
    /**
     * 阴影的起始颜色
     */
    private float mElevation;
    /**
     * 控件背景颜色
     */
    private ColorStateList mBackgroundColor;
    private static IRoundView roundViewImp;
    static {
        if (Build.VERSION.SDK_INT >= 17) {
            roundViewImp = new RoundViewJellyBeanMr();
        } else {
            roundViewImp = new RoundViewLowImp();
        }
        roundViewImp.initStatic();
    }

    public RoundView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public RoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public RoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundView, defStyleAttr, 0);
        //判断是否有背景颜色
        if (a.hasValue(R.styleable.RoundView_backgroundColor)) {
            mBackgroundColor = a.getColorStateList(R.styleable.RoundView_backgroundColor);
        } else {
            //获取系统的背景颜色
            TypedArray aa = context.obtainStyledAttributes(new int[]{android.R.attr.colorBackground});
            //获取系统的背景颜色
            int themeBackgroundColor = aa.getColor(0, 0);
            //获取背景颜色的hvs值（h色彩、s饱和度、v颜色值）
            float[] hsv = new float[3];
            Color.colorToHSV(themeBackgroundColor, hsv);
            //当饱和度>0.5的时候，我们获取自定义的cardview_dark_background颜色
            if (hsv[2] > 0.5) {
                mBackgroundColor = ColorStateList.valueOf(getResources().getColor(R.color.cardview_dark_background));
            } else {
                mBackgroundColor = ColorStateList.valueOf(getResources().getColor(R.color.cardview_dark_background));
            }
            aa.recycle();
        }
        mRadius = a.getDimensionPixelSize(R.styleable.RoundView_conerRadius, 0);
        mElevation = a.getDimensionPixelSize(R.styleable.RoundView_shadowSize, 0);
        mShadowStartColor = a.getColor(R.styleable.RoundView_shadowStartColor, getResources().getColor(R.color.cardview_shadow_start_color));
        mShadowEndColor = a.getColor(R.styleable.RoundView_shadowEndColor, getResources().getColor(R.color.cardview_shadow_end_color));
        a.recycle();
        roundViewImp.initialize(mRoundViewDelegate);
    }


    public int getShadowStartColor() {
        return mShadowStartColor;
    }

    public void setShadowStartColor(int shadowStartColor) {
        this.mShadowStartColor = shadowStartColor;
    }

    public int getShadowEndColor() {
        return mShadowEndColor;
    }

    public void setShadowEndColor(int shadowEndColor) {
        this.mShadowEndColor = shadowEndColor;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float mRadius) {
        this.mRadius = mRadius;
    }

    public float getElevation() {
        return mElevation;
    }

    public void setElevation(float mElevation) {
        this.mElevation = mElevation;
    }

    public ColorStateList getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(ColorStateList backgroundColor) {
        this.mBackgroundColor = backgroundColor;
    }

    private IRoundViewDelegate mRoundViewDelegate = new IRoundViewDelegate() {
        private Drawable bgDrawable;

        @Override
        public void setCardBackground(Drawable drawable) {
            this.bgDrawable = drawable;
            setBackgroundDrawable(drawable);
        }

        @Override
        public Drawable getCardBackground() {
            return bgDrawable;
        }

        @Override
        public View getCardView() {
            return RoundView.this;
        }

        @Override
        public int getShadowStartColor() {
            return RoundView.this.getShadowStartColor();
        }

        @Override
        public int getShadowEndColor() {
            return RoundView.this.getShadowEndColor();
        }

        @Override
        public ColorStateList getBackgroundColor() {
            return RoundView.this.getBackgroundColor();
        }

        @Override
        public float getRadius() {
            return RoundView.this.getRadius();
        }

        @Override
        public float getElevation() {
            return RoundView.this.getElevation();
        }
    };
}
