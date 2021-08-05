package com.noxgroup.app.learnopengl.mirror;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.noxgroup.app.learnopengl.base.BaseSurfaceViewActivity;
import com.noxgroup.app.learnopengl.utils.Utils;

/**
 * @author huangjian
 * @create 2021/7/30
 * @Description
 */
public class MirrorActivity extends BaseSurfaceViewActivity {

    private BaseMirrorRender renderer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView.setEGLContextClientVersion(2);
        renderer = new MirrorRender2(Utils.getImageFromAssetsFile("images/image2.png"));
        glSurfaceView.setRenderer(renderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void rotateDirection(View view) {
        renderer.changeRotateDirce();
    }

    @Override
    public void reset(View view) {
        renderer.reset();
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

    @Override
    public void scale(View view) {
        renderer.scale();
        glSurfaceView.requestRender();
    }

    @Override
    public void transX(View view) {
        renderer.transX();
        glSurfaceView.requestRender();
    }

    @Override
    public void transY(View view) {
        renderer.transY();
        glSurfaceView.requestRender();
    }
}
