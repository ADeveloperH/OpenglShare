precision mediump float;
varying vec2 textureCoordinate;
uniform sampler2D inputImageTexture;
uniform float scaleValue;
uniform float inputWidth;
uniform float inputHeight;
uniform float rotateValue;//角度
uniform float transXValue;//x 轴移动的大小
uniform float transYValue;//y 轴移动的大小

//z 轴中心旋转效果
vec2 rotate2dCenter(vec2 newTextureCoordinate, float inputWidth, float inputHeight, float radianAngle)
{
    vec2 resolution = vec2(inputWidth, inputHeight);
    vec2 rotateCenter = resolution * vec2(0.5);
    vec2 realCoord = newTextureCoordinate * resolution;
    float radian = radians(radianAngle);

    float cos=cos(radian);
    float sin=sin(radian);
    mat3 rotateMat=mat3(cos, -sin, 0.0,
    sin, cos, 0.0,
    0.0, 0.0, 1.0);
    vec3 deltaOffset;
    deltaOffset = rotateMat*vec3(realCoord.x- rotateCenter.x, realCoord.y- rotateCenter.y, 1.0);
    realCoord.x = deltaOffset.x+rotateCenter.x;
    realCoord.y = deltaOffset.y+rotateCenter.y;
    return realCoord/resolution;
}

vec2 scale(vec2 newTextureCoordinate, float scaleValueX, float scaleValueY){
    vec2 scaleCenter = vec2(0.5);
    if (scaleValueX != 0.0){
        scaleValueX = 1.0/scaleValueX;
    }
    if (scaleValueY != 0.0){
        scaleValueY = 1.0/scaleValueY;
    }
    newTextureCoordinate -= scaleCenter;
    newTextureCoordinate = mat2(scaleValueX, 0.0, 0.0, scaleValueY) * newTextureCoordinate;
    newTextureCoordinate += scaleCenter;
    return newTextureCoordinate;
}

//水平镜像处理
vec2 horMirror(vec2 newTextureCoordinate){
    if (newTextureCoordinate.x > 1.0 || newTextureCoordinate.x < 0.0){
        if(newTextureCoordinate.x < 0.0){
            while(newTextureCoordinate.x < 0.0){
                newTextureCoordinate.x += 2.0;
            }
            if(newTextureCoordinate.x > 1.0){
                newTextureCoordinate.x = 2.0 - newTextureCoordinate.x;
            }
        }
        if(newTextureCoordinate.x > 1.0){
            while(newTextureCoordinate.x > 1.0){
                newTextureCoordinate.x -= 2.0;
            }
            newTextureCoordinate.x = abs(newTextureCoordinate.x);
        }
    }
    return newTextureCoordinate;
}

void main(){
    vec2 newTextureCoordinate = textureCoordinate;
    newTextureCoordinate-=vec2(transXValue, transYValue);
    newTextureCoordinate = rotate2dCenter(newTextureCoordinate, inputWidth, inputHeight, rotateValue);
    newTextureCoordinate = scale(newTextureCoordinate, scaleValue, scaleValue);
    if (newTextureCoordinate.y >= 0.0 && newTextureCoordinate.y <= 1.0){
        //坐标进行水平镜像处理
//        newTextureCoordinate = horMirror(newTextureCoordinate);
        vec4 resultColor = texture2D(inputImageTexture, newTextureCoordinate);
        gl_FragColor = resultColor;
    } else {
        discard;
    }

}