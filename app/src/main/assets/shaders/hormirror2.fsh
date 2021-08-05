precision mediump float;
varying vec2 textureCoordinate;
uniform sampler2D inputImageTexture;

const float zoom = 33.0;
void main(){
    vec2 newTextureCoordinate = textureCoordinate;
    highp float value = zoom / 2.0;
    if (newTextureCoordinate.y >= value - 0.5 && newTextureCoordinate.y <= value + 0.5){
        vec4 resultColor = texture2D(inputImageTexture, newTextureCoordinate);
        gl_FragColor = resultColor;
    } else {
        discard;
    }

}