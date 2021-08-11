package com.noxgroup.app.learnopengl.image;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
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
public class ImageRender implements GLSurfaceView.Renderer {
    private Bitmap mBitmap;

    public static final String SHADER_VSH = "shaders/simpleMatrix.vsh";
    public static final String SHADER_FSH = "shaders/simple.fsh";
    private static final float zValue = 0.0f;
    private static final float[] mVertex = {
            -1.0f, 1.0f, zValue,
            -1.0f, -1.0f, zValue,
            1.0f, 1.0f, zValue,
            1.0f, -1.0f, zValue
    };
    private static final float[] mTextureCoord = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };

    //原始
//    private static final float[] mTextureCoord = {
//            0.0f, 1.0f,
//            0.0f, 0.0f,
//            1.0f, 1.0f,
//            1.0f, 0.0f,
//    };


    //==============================GL_TRIANGLE_FAN
//    private static final float[] mVertex = {
//            -1.0f, 1.0f, zValue,
//            -1.0f, -1.0f, zValue,
//            1.0f, -1.0f, zValue,
//            1.0f, 1.0f, zValue,
//    };
//
//    private static final float[] mTextureCoord = {
//            0.0f, 0.0f,
//            0.0f, 1.0f,
//            1.0f, 1.0f,
//            1.0f, 0.0f,
//    };

    //=====================三角形区域
//    private static final float[] mVertex = {
//            0.0f, 1.0f, zValue,
//            -1.0f, -1.0f, zValue,
//            1.0f, -1.0f, zValue
//    };
//
//    private static final float[] mTextureCoord = {
//            0.5f, 0.0f,
//            0.0f, 1.0f,
//            1.0f, 1.0f,
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

    public ImageRender(Bitmap bitmap) {
        this.mBitmap = bitmap;
        // 初始化 ByteBuffer，长度为 arr 数组的长度 * 4，因为一个 float 占4个字节
        mVertexBuffer = ByteBuffer.allocateDirect(mVertex.length * 4)
                //数组排列用 nativeOrder
                .order(ByteOrder.nativeOrder())
                //从 ByteBuffer 创建一个浮点缓冲区
                .asFloatBuffer()

                //将坐标添加到 FloatBuffer
                .put(mVertex);
        //设置缓冲区来读取第一个坐标
        mVertexBuffer.position(0);

        mTextureCoordBuffer = ByteBuffer.allocateDirect(mTextureCoord.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(mTextureCoord);
        mTextureCoordBuffer.position(0);
        //为什么数据需要转换格式呢？
        // 主要是因为 Java 的缓冲区数据存储结构为大端字节序(BigEdian)，而OpenGl的数据为小端字节序（LittleEdian）
        // 因为数据存储结构的差异，所以，在Android 中使用 OpenGl 的时候必须要进行下转换。
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 0.7f, 0.9f, 1.0f);

        String vertexShaderSource = Utils.loadFromAssetsFile(SHADER_VSH);
        String fragmentShaderSource = Utils.loadFromAssetsFile(SHADER_FSH);
        mProgram = GLESUtils.buildProgram(vertexShaderSource, fragmentShaderSource);
        //获取顶点着色器的位置的句柄
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
        Log.d("hj", "ImageRender.onSurfaceChanged: width:" + width + " height:" + height);
        GLES20.glViewport(0, 0, width, height);
//        GLES20.glViewport(100, 100, width, height);
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
        //normalized：是否进行归一化
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        mTextureCoordBuffer.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 0, mTextureCoordBuffer);
        GLES20.glEnableVertexAttribArray(mTextureCoordHandle);

        //激活图层0 GL_TEXTURE1
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        //0 对应的是图层的索引
        GLES20.glUniform1i(mTextureHandle, 0);

//        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
//        GLES20.glUniform1i(mTextureHandle, 1);


        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.setIdentityM(mProjectMatrix, 0);
        Matrix.setIdentityM(mMVPMatrix, 0);

        if (transZValue != 0) {
            //Z 轴平移需要在视景体内
            Matrix.translateM(mModelMatrix, 0, 0, 0, transZValue);
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

        //不管是正交投影还是透视投影，最终都是将视景体内的物体投影在近平面上
        if (isFrust) {
            /**
             * Matrix.frustumM (float[] m,         //接收透视投影的变换矩阵
             *                 int mOffset,        //变换矩阵的起始位置（偏移量）
             *                 float left,         //相对观察点近面的左边距
             *                 float right,        //相对观察点近面的右边距
             *                 float bottom,       //相对观察点近面的下边距
             *                 float top,          //相对观察点近面的上边距
             *                 float near,         //相对观察点近面距离
             *                 float far)          //相对观察点远面距离
             */
            Matrix.frustumM(mProjectMatrix, 0, left, right, bottom, top, 1, 9);
        } else {
            /**
             * Matrix.orthoM (float[] m,           //接收正交投影的变换矩阵
             *                 int mOffset,        //变换矩阵的起始位置（偏移量）
             *                 float left,         //相对观察点近面的左边距
             *                 float right,        //相对观察点近面的右边距
             *                 float bottom,       //相对观察点近面的下边距
             *                 float top,          //相对观察点近面的上边距
             *                 float near,         //相对观察点近面距离
             *                 float far)          //相对观察点远面距离
             */
            Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1, 1, 1, 9);
        }

        /**
         * Matrix.setLookAtM (float[] rm,      //接收相机变换矩阵
         *                 int rmOffset,       //变换矩阵的起始位置（偏移量）
         *                 float eyeX,float eyeY, float eyeZ,   //相机位置
         *                 float centerX,float centerY,float centerZ,  //观察点位置
         *                 float upX,float upY,float upZ)  //up向量在xyz上的分量
         */
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 3.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        /**
         * Matrix.multiplyMM (float[] result, //接收相乘结果
         *                 int resultOffset,  //接收矩阵的起始位置（偏移量）
         *                 float[] lhs,       //左矩阵
         *                 int lhsOffset,     //左矩阵的起始位置（偏移量）
         *                 float[] rhs,       //右矩阵
         *                 int rhsOffset)     //右矩阵的起始位置（偏移量）
         */
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mMVPMatrix, 0);
        //绘制模式
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, mTextureCoord.length /2);
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, mTextureCoord.length /2);
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mTextureCoord.length /2);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordHandle);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }


    //==========================================================================================================================//
    private float rotateXValue;
    private float rotateYValue;
    private float rotateZValue;
    private float transZValue;
    private boolean increase;//是否是变大
    private boolean isFrust = true;//是否是透视投影

    public void changeDirect() {
        increase = !increase;
    }

    public void changeProjection() {
        isFrust = !isFrust;
    }

    public void resetRotate() {
        rotateXValue = 0;
        rotateYValue = 0;
        rotateZValue = 0;
        transZValue = 0;
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
        Log.w("hj", "ImageRender.rotateZ: " + rotateZValue);
    }

    public void transZ() {
        if (increase) {
            transZValue += 1;
        } else {
            transZValue -= 1;
        }
        Log.w("hj", "ImageRender.transZ: " + transZValue);
    }

}
