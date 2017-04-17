package com.yasin.round.card;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by leo on 17/4/1.
 */

public class RoundRectDrawableWithShadow extends Drawable {
    //下移的偏移量
    private static final float SHADOW_MULTIPLIER = 1.5f;
    //画圆角矩形帮助类
    public static RoundRectHelper mRoundRectHelper;
    //背景颜色
    private ColorStateList mBgColor;
    //四周shadow起始颜色
    private int mShadowStartColor;
    //四周shadow结束颜色
    private int mShadowEndColor;
    //圆角半径
    private float mCornerRadius;
    //阴影的宽度
    private float mShadowSize;
    //是否需要进行初始化（防止多次调用初始化方法）
    private boolean mDirty = true;
    //内容部分的矩形范围
    private RectF mCardBounds = new RectF();
    //画背景用的画笔
    private Paint mPaint;
    //画边角阴影的画笔
    private Paint mCornerShadowPaint;
    //画四周shadow的画笔
    private Paint mEdgeShadowPaint;
    //边角path路径
    private Path mCornerShadowPath;

    public RoundRectDrawableWithShadow(ColorStateList bgColor, int shadowStartColor, int shadowEndColor, float shadowSize, float radius) {
        this.mShadowStartColor = shadowStartColor;
        this.mShadowEndColor = shadowEndColor;
        this.mShadowSize = shadowSize;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        //设置view的背景颜色
        setBackground(bgColor);
        mCornerShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mCornerShadowPaint.setStyle(Paint.Style.FILL);
        //为了能使设置的corner更接近一个整数值
        this.mCornerRadius = (int) (radius + .5f);
        mCardBounds = new RectF();
        mEdgeShadowPaint = new Paint(mCornerShadowPaint);
        mEdgeShadowPaint.setAntiAlias(false);
    }

    private void setBackground(ColorStateList color) {
        //如果用户有设置背景色的就用设置的背景色，如果没有就用透明色
        mBgColor = (color == null) ? ColorStateList.valueOf(Color.TRANSPARENT) : color;
        //把当前背景色给mpaint画笔
        mPaint.setColor(mBgColor.getColorForState(getState(), mBgColor.getDefaultColor()));
    }

    @Override
    public void draw(Canvas canvas) {
        //避免多次调用初始化方法
        if (mDirty) {
            //初始化一些数据
            buildComponents(getBounds());
            mDirty = false;
        }
        //给画布垂直平移阴影宽度的一半
        canvas.translate(0, mShadowSize / 2);
        drawShadow(canvas);
        //还原画布
        canvas.translate(0, -mShadowSize / 2);
        //中间的圆角矩形交给mRoundRectHelper，而mRoundRectHelper赋值在IRoundView的initStatic方法
        mRoundRectHelper.drawRoundRect(canvas, mCardBounds, mCornerRadius, mPaint);
    }

    private void drawShadow(Canvas canvas) {
        final float edgeShadowTop = -mCornerRadius - mShadowSize;
        final float inset = mCornerRadius+ mShadowSize / 2;
        final boolean drawHorizontalEdges = mCardBounds.width() - 2 * inset > 0;
        final boolean drawVerticalEdges = mCardBounds.height() - 2 * inset > 0;
        // LT 画左上的角落跟顶部的阴影
        int saved = canvas.save();
        canvas.translate(mCardBounds.left + inset, mCardBounds.top + inset);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        if (drawHorizontalEdges) {
            canvas.drawRect(0, edgeShadowTop,
                    mCardBounds.width() - 2 * inset, -mCornerRadius,
                    mEdgeShadowPaint);
        }
        canvas.restoreToCount(saved);
        // RB 画右下的角落跟右边的阴影
        saved = canvas.save();
        canvas.translate(mCardBounds.right - inset, mCardBounds.bottom - inset);
        canvas.rotate(180f);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        if (drawHorizontalEdges) {
            canvas.drawRect(0, edgeShadowTop,
                    mCardBounds.width() - 2 * inset, -mCornerRadius ,
                    mEdgeShadowPaint);
        }
        canvas.restoreToCount(saved);
        // LB 画左下的边角跟左边的阴影
        saved = canvas.save();
        canvas.translate(mCardBounds.left + inset, mCardBounds.bottom - inset);
        canvas.rotate(270f);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        if (drawVerticalEdges) {
            canvas.drawRect(0, edgeShadowTop,
                    mCardBounds.height() - 2 * inset, -mCornerRadius, mEdgeShadowPaint);
        }
        canvas.restoreToCount(saved);
        // RT 画右上的边角跟右边的阴影
        saved = canvas.save();
        canvas.translate(mCardBounds.right - inset, mCardBounds.top + inset);
        canvas.rotate(90f);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        if (drawVerticalEdges) {
            canvas.drawRect(0, edgeShadowTop,
                    mCardBounds.height() - 2 * inset, -mCornerRadius, mEdgeShadowPaint);
        }
        canvas.restoreToCount(saved);
    }

    private void buildComponents(Rect bounds) {
        //使整个阴影部分往下平移mShadowSize * SHADOW_MULTIPLIER距离（使其看起来有z轴偏移的效果）
        final float verticalOffset = mShadowSize * SHADOW_MULTIPLIER;
        //中间内容部分的矩形（也就是我们demo红色部分的范围）
        mCardBounds.set(bounds.left + mShadowSize, bounds.top + verticalOffset,
                bounds.right - mShadowSize, bounds.bottom - verticalOffset);
        //初始化一个边角corner的模版
        buildShadowCorners();
    }

    /**
     * 创建一个阴影的边角
     */
    private void buildShadowCorners() {
        //边角外部弧度矩形
        RectF innerBounds = new RectF(-mCornerRadius, -mCornerRadius, mCornerRadius, mCornerRadius);
        //边角内部弧度矩形
        RectF outerBounds = new RectF(innerBounds);
        outerBounds.inset(-mShadowSize, -mShadowSize);

        if (mCornerShadowPath == null) {
            mCornerShadowPath = new Path();
        } else {
            mCornerShadowPath.reset();
        }
        mCornerShadowPath.setFillType(Path.FillType.EVEN_ODD);
        //将path的点移动至（-mCornerRadius，0）
        mCornerShadowPath.moveTo(-mCornerRadius, 0);
        //再向左移动mShadowSize距离
        mCornerShadowPath.rLineTo(-mShadowSize, 0);
        // outer arc（外部弧度）
        mCornerShadowPath.arcTo(outerBounds, 180f, 90f, false);
        // inner arc（内部弧度）
        mCornerShadowPath.arcTo(innerBounds, 270f, -90f, false);
        //闭合path
        mCornerShadowPath.close();
        //设置四个角落的阴影为RadialGradient，RadialGradient的中心点为0，0半径为 mCornerRadius + mShadowSize
        //向外部发射
        float startRatio = mCornerRadius / (mCornerRadius + mShadowSize);
        mCornerShadowPaint.setShader(new RadialGradient(0, 0, mCornerRadius + mShadowSize,
                new int[]{mShadowStartColor, mShadowStartColor, mShadowEndColor},
                new float[]{0f, startRatio, 1f}
                , Shader.TileMode.CLAMP));

        // we offset the content shadowSize/2 pixels up to make it more realistic.
        // this is why edge shadow shader has some extra space
        // When drawing bottom edge shadow, we use that extra space.
        mEdgeShadowPaint.setShader(new LinearGradient(0, -mCornerRadius + mShadowSize, 0,
                -mCornerRadius - mShadowSize,
                new int[]{mShadowStartColor, mShadowStartColor, mShadowEndColor},
                new float[]{0f, .5f, 1f}, Shader.TileMode.CLAMP));
        mEdgeShadowPaint.setAntiAlias(false);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    static interface RoundRectHelper {
        void drawRoundRect(Canvas canvas, RectF bounds, float cornerRadius, Paint paint);
    }
}
