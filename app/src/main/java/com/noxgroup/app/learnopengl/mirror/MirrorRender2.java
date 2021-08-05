package com.noxgroup.app.learnopengl.mirror;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.noxgroup.app.learnopengl.utils.GLESUtils;
import com.noxgroup.app.learnopengl.utils.Utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author huangjian
 * @create 2021/7/30
 * @Description
 */
public class MirrorRender2 extends BaseMirrorRender {
    private Bitmap mBitmap;

    public static final String SHADER_VSH = "shaders/simpleMatrix.vsh";
    public static final String SHADER_FSH = "shaders/hormirror2.fsh";
    private static final float zValue = 0.0f;
    private static final float[] mVertex = {
            -1.0f, 1.0f, zValue,
            -1.0f, -1.0f, zValue,
            1.0f, 1.0f, zValue,
            1.0f, -1.0f, zValue
    };

    private static int zoom = 33;
        private static final float[] mTextureCoord = {
            0.0f, 0.0f,
            0.0f, 1.0f * zoom,
            1.0f * zoom, 0.0f,
            1.0f * zoom, 1.0f * zoom,
    };
//    private static final float[] mTextureCoord = {
//            1.0f * zoom, 1.0f * zoom,
//            1.0f * zoom, 0.0f,
//            0.0f, 1.0f * zoom,
//            0.0f, 0.0f,
//    };

    private float[] mModelMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTextureCoordBuffer;

    private int mProgram;
    private int mPositionHandle;
    private int mTextureCoordHandle;
    private int mTextureHandle;
    private int mMatrixHandle;
    private int mTextureId;

    public MirrorRender2(Bitmap bitmap) {
        this.mBitmap = bitmap;
        mVertexBuffer = ByteBuffer.allocateDirect(mVertex.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(mVertex);
        mVertexBuffer.position(0);

        mTextureCoordBuffer = ByteBuffer.allocateDirect(mTextureCoord.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(mTextureCoord);
        mTextureCoordBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 0.7f, 0.9f, 1.0f);

        String vertexShaderSource = Utils.loadFromAssetsFile(SHADER_VSH);
        String fragmentShaderSource = Utils.loadFromAssetsFile(SHADER_FSH);
        mProgram = GLESUtils.buildProgram(vertexShaderSource, fragmentShaderSource);
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "position");
        mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        mMatrixHandle = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        mTextureHandle = GLES20.glGetUniformLocation(mProgram, "inputImageTexture");
        mTextureId = GLESUtils.createTexture(mBitmap);
    }

    float left;
    float right;
    float bottom;
    float top;

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        left = -1;
        right = 1;
        bottom = -1;
        top = 1;

        float zoom = 1 / 3.0f;
        left *= zoom;
        right *= zoom;
        bottom *= zoom;
        top *= zoom;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glUseProgram(mProgram);
        mVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        mTextureCoordBuffer.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 0, mTextureCoordBuffer);
        GLES20.glEnableVertexAttribArray(mTextureCoordHandle);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        GLES20.glUniform1i(mTextureHandle, 0);

        //纹理的坐标系统叫做ST坐标系统，和xy坐标系统一样，s对应x，t对应y
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_MIRRORED_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_MIRRORED_REPEAT);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.setIdentityM(mProjectMatrix, 0);
        Matrix.setIdentityM(mMVPMatrix, 0);
        if (transX != 0 || transY != 0) {
            Matrix.translateM(mModelMatrix, 0, transX, transY, 0);
        }

        if (rotateXValue != 0) {
            Matrix.rotateM(mModelMatrix, 0, rotateXValue, 1, 0, 0);
        }
        if (rotateYValue != 0) {
            Matrix.rotateM(mModelMatrix, 0, rotateYValue, 0, 1, 0);
        }
        if (rotateZValue != 0) {
            Matrix.rotateM(mModelMatrix, 0, rotateZValue, 0, 0, 1);
        }
        if (scale != 1) {
            Matrix.scaleM(mModelMatrix, 0, scale, scale, 1);
        }
        if (isFrust) {
            Matrix.frustumM(mProjectMatrix, 0, left, right, bottom, top, 1, 9);
        } else {
            Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1, 1, 1, 9);
        }
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 3.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordHandle);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }


    private final float defaultScale = zoom;
    private final float defaultTransY = 0;
    private float rotateXValue;
    private float rotateYValue;
    private float rotateZValue;
    private float scale = defaultScale;
    private float transX;
    private float transY = defaultTransY;
    private boolean increase;//是否是变大
    private boolean isFrust = true;//是否是透视投影

    public void changeRotateDirce() {
        increase = !increase;
    }

    public void changeProjection() {
        isFrust = !isFrust;
    }

    public void reset() {
        rotateXValue = 0;
        rotateYValue = 0;
        rotateZValue = 0;
        scale = defaultScale;
        transX = 0;
        transY = defaultTransY;
    }

    public void rotateX() {
        if (increase) {
            rotateXValue += 3;
        } else {
            rotateXValue -= 3;
        }
    }

    public void rotateY() {
        if (increase) {
            rotateYValue += 3;
        } else {
            rotateYValue -= 3;
        }
    }

    public void rotateZ() {
        if (increase) {
            rotateZValue += 3;
        } else {
            rotateZValue -= 3;
        }
    }

    public void scale() {
        if (increase) {
            scale += 0.1;
        } else {
            scale -= 0.1;
        }
    }

    public void transX() {
        if (increase) {
            transX += 0.1;
        } else {
            transX -= 0.1;
        }
    }

    public void transY() {
        if (increase) {
            transY += 0.1;
        } else {
            transY -= 0.1;
        }
        Log.d("hj", "MirrorRender2.transY: " + transY);
    }
}
