package com.yasin.processdemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.yasin.processdemo.view.ProcessView;

public class MainActivity extends AppCompatActivity {
    private ProcessView mProcessView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProcessView= (ProcessView) findViewById(R.id.id_process);
        startAni();
    }
    private void startAni() {
        ValueAnimator a = ValueAnimator.ofFloat(0, 0.67f);
        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                mProcessView.setProgress(progress);
            }
        });
        a.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mProcessView.startAni();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        a.setDuration(3000);
        a.setInterpolator(new AccelerateDecelerateInterpolator());
        a.start();
    }
}
