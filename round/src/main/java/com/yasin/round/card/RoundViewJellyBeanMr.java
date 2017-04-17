package com.yasin.round.card;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by leo on 17/4/1.
 */

public class RoundViewJellyBeanMr extends RoundViewLowImp {

    @Override
    public void initStatic() {
        RoundRectDrawableWithShadow.mRoundRectHelper=new RoundRectDrawableWithShadow.RoundRectHelper() {
            @Override
            public void drawRoundRect(Canvas canvas, RectF bounds, float cornerRadius, Paint paint) {
                canvas.drawRoundRect(bounds,cornerRadius,cornerRadius,paint);
            }
        };
    }
}
