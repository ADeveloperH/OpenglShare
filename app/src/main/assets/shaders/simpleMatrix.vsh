//顶点着色器属性:顶点坐标
attribute vec4 position;
//顶点着色器属性:纹理坐标
attribute vec4 inputTextureCoordinate;
//传入片元着色器的变量
varying vec2 textureCoordinate;
//外部传入变量：变化矩阵 projection * view * model
uniform mat4 vMatrix;

//程序入口
void main(){
    textureCoordinate = inputTextureCoordinate.xy;
    //gl_Position:内置的顶点着色器输出变量
//    gl_Position = vMatrix * vec4(position.xyz * 0.5, 1.0);
    gl_Position = vMatrix * vec4(position.xyz, 1.0);
}