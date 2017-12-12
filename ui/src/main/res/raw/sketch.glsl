precision mediump float;
varying highp vec2 textureCoordinate;
uniform sampler2D inputImageTexture;
void main() {
    vec3 centralColor = texture2D(inputImageTexture, textureCoordinate).rgb;
    gl_FragColor = vec4(0.299*centralColor.r+0.587*centralColor.g+0.114*centralColor.b);
}