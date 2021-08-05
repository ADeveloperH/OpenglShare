package com.noxgroup.app.learnopengl.image;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.noxgroup.app.learnopengl.base.BaseSurfaceViewActivity;
import com.noxgroup.app.learnopengl.utils.Utils;

/**
 * @author huangjian
 * @create 2021/7/30
 * @Description
 */
public class ImageActivity extends BaseSurfaceViewActivity {

    private ImageRender renderer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView.setEGLContextClientVersion(2);
        renderer = new ImageRender(Utils.getImageFromAssetsFile("images/image2.png"));
        glSurfaceView.setRenderer(renderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void rotateDirection(View view) {
        renderer.changeRotateDirce();
    }

    @Override
    public void reset(View view) {
        renderer.resetRotate();
        glSurfaceView.requestRender();
    }

    @Override
    public void rotateX(View view) {
        renderer.rotateX();
        glSurfaceView.requestRender();
    }

    @Override
    public void rotateY(View view) {
        renderer.rotateY();
        glSurfaceView.requestRender();
    }

    @Override
    public void rotateZ(View view) {
        renderer.rotateZ();
        glSurfaceView.requestRender();
    }

    @Override
    public void changeProjection(View view) {
        renderer.changeProjection();
        glSurfaceView.requestRender();
    }
}
