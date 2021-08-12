package com.noxgroup.app.learnopengl.interpolator;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.noxgroup.app.learnopengl.R;

/**
 * @author huangjian
 * @create 2021/8/12
 * @Description
 */
public class InterpolatorActivity extends AppCompatActivity {

    private View viewLinear;
    private View viewBezier;
    private View viewDecay;
    private LinearInterpolator linearInterpolator;
    private BezierInterpolaotr bezierInterpolaotr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("OPenGLES Demo");
        setContentView(R.layout.activity_interpolator_layout);

        initView();
    }

    private void initView() {
        viewLinear = findViewById(R.id.view_linear);
        viewBezier = findViewById(R.id.view_bezier);
        viewDecay = findViewById(R.id.view_decay);
        linearInterpolator = new LinearInterpolator();
        bezierInterpolaotr = new BezierInterpolaotr(0, 1, 1, 0);
        viewLinear.post(new Runnable() {
            @Override
            public void run() {
                startY = viewLinear.getY();
            }
        });
    }

    private float startY = 0;
    private float moveDistance = 1000;

    public void startAnim(View view) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                changeTrans(progress);
            }
        });
        valueAnimator.setDuration(1500);
        valueAnimator.start();
    }

    private void changeTrans(float progress) {
        viewLinear.setY(startY + getTransValue(linearInterpolator.getInterpolation(progress)));
        viewBezier.setY(startY + getTransValue(bezierInterpolaotr.getInterpolation(progress)));
        viewDecay.setY(startY + moveDistance / 2 + getDecayEvaluator(moveDistance / 2, progress));
    }


    private float getTransValue(float progress) {
        float startValue = 0;
        float endValue = moveDistance;
        return startValue + progress * (endValue - startValue);
    }


    private float getDecayEvaluator(float crest, float progress) {
        return (float) (crest / 0.036211574 * Math.sin(progress * 3 * Math.PI * 2) / Math.exp(5 * progress) / (3 * Math.PI * 2));
    }
}