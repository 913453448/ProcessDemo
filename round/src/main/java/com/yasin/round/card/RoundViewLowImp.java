package com.yasin.round.card;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by leo on 17/4/1.
 */

public class RoundViewLowImp implements IRoundView {
    @Override
    public void initialize(IRoundViewDelegate roundView) {
        RoundRectDrawableWithShadow backgroundDrawable = createDrawable(roundView);
        roundView.setCardBackground(backgroundDrawable);
    }

    private RoundRectDrawableWithShadow createDrawable(IRoundViewDelegate roundView) {
        return new RoundRectDrawableWithShadow(
                roundView.getBackgroundColor(),
                roundView.getShadowStartColor(),
                roundView.getShadowEndColor(),
                roundView.getElevation(),
                roundView.getRadius());
    }

    @Override
    public void initStatic() {
        RoundRectDrawableWithShadow.mRoundRectHelper = new RoundRectDrawableWithShadow.RoundRectHelper() {
            @Override
            public void drawRoundRect(Canvas canvas, RectF bounds, float cornerRadius, Paint paint) {

            }
        };
    }

    @Override
    public void setRadius(IRoundViewDelegate cardView, float radius) {

    }

    @Override
    public float getRadius(IRoundViewDelegate cardView) {
        return 0;
    }

    @Override
    public void setElevation(IRoundViewDelegate cardView, float elevation) {

    }

    @Override
    public float getElevation(IRoundViewDelegate cardView) {
        return 0;
    }

    private RoundRectDrawableWithShadow getShadowBackground(IRoundViewDelegate cardView) {
        return (RoundRectDrawableWithShadow) cardView.getCardBackground();
    }
}
