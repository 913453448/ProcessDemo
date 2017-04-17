package com.yasin.round.card;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by leo on 17/3/31.
 */

public interface IRoundViewDelegate {
    void setCardBackground(Drawable drawable);

    Drawable getCardBackground();
    View getCardView();
    int getShadowStartColor();
    int getShadowEndColor();
    ColorStateList getBackgroundColor();
    float getRadius();
    float getElevation();
}
