package com.noxgroup.app.learnopengl.glsurfaceview;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.noxgroup.app.learnopengl.base.BaseActivity;
import com.noxgroup.app.learnopengl.R;
import com.noxgroup.app.learnopengl.base.BaseSurfaceViewActivity;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author huangjian
 * @create 2021/7/30
 * @Description
 */
public class GLSurfaceViewActivity extends BaseSurfaceViewActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置 OPenGL ES 版本（2.0）
        glSurfaceView.setEGLContextClientVersion(2);
        /**
         * 设置渲染回调接口
         * 启动 GLThread ，构建 EGL 环境
         */
        glSurfaceView.setRenderer(new MyRender());
        /**
         * 设置刷新方式：在 setRenderer 后设置
         * RENDERMODE_WHEN_DIRTY：手动刷新，调用 requestRender 方法进行刷新
         * RENDERMODE_CONTINUOUSLY：自动刷新，自动回调 onDrawFrame 方法
         */
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    static class MyRender implements GLSurfaceView.Renderer {
        /**
         * 调用一次以设置视图的 OpenGL ES 环境。
         */
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            // Set the background frame color
            GLES20.glClearColor(1.0f, 0.7f, 0.9f, 1.0f);
            Log.d("hj", "MyRender.onSurfaceCreated: " + Thread.currentThread().getName());
        }

        /**
         * 当视图的几何图形发生变化（例如当设备的屏幕方向发生变化）时调用。
         *
         * @param gl
         * @param width
         * @param height
         */
        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Log.d("hj", "MyRender.onSurfaceChanged: width:" + width + " height:" + height);
//            GLES20.glViewport(0, 0, width, height);
            GLES20.glViewport(0, 0, width, height/2);
            Log.d("hj", "MyRender.onSurfaceChanged: " + Thread.currentThread().getName());
        }

        /**
         * 每次重新绘制视图时调用。
         *
         * @param gl
         */
        @Override
        public void onDrawFrame(GL10 gl) {
            Log.d("hj", "MyRender.onDrawFrame: " + Thread.currentThread().getName());
            // Redraw background color
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        }

    }
}
