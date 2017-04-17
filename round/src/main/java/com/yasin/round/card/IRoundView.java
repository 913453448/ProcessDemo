package com.yasin.round.card;

/**
 * Created by leo on 17/3/31.
 */

public interface IRoundView {
    /**
     * 初始化view
     */
    void initialize(IRoundViewDelegate roundView);

    /**
     * 设置圆角半径
     */
    void setRadius(IRoundViewDelegate cardView, float radius);

    float getRadius(IRoundViewDelegate cardView);

    /**
     * 设置z轴的偏移量
     */
    void setElevation(IRoundViewDelegate cardView, float elevation);

    float getElevation(IRoundViewDelegate cardView);

    /**
     *圆角功能具体实现方法
     */
    void initStatic();
}
