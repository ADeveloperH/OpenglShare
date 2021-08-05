package com.noxgroup.app.learnopengl.base;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.noxgroup.app.learnopengl.R;

/**
 * @author huangjian
 * @create 2021/7/30
 * @Description
 */
public class BaseSurfaceViewActivity extends BaseActivity {
    protected GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_glsurfaceview);
        glSurfaceView = findViewById(R.id.glsurface_view);
    }

    public void rotateX(View view) {
    }

    public void rotateY(View view) {

    }

    public void rotateZ(View view) {
    }

    public void rotateDirection(View view) {
    }

    public void reset(View view) {
    }

    public void changeProjection(View view) {
    }

    public void scale(View view) {
    }

    public void transX(View view) {
    }

    public void transY(View view) {
    }
}
