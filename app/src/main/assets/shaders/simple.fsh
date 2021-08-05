//精度限定符：lowp:低精度 mediump:中精度 highp:高精度
precision mediump float;
//顶点着色器传入
varying vec2 textureCoordinate;
//外部传入变量：二维纹理  samplerExternalOES:视频/相机纹理
uniform sampler2D inputImageTexture;

//程序入口
void main(){
    //texture2D 内置函数：采样器，获取纹理上指定位置的颜色值。
    vec4 resultColor = texture2D(inputImageTexture, textureCoordinate);
    //内建变量：输出颜色
    gl_FragColor = resultColor;
}