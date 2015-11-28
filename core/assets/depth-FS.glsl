varying vec4 v_final_pos;

void main() {
    const vec4 packFactors = vec4(1 ,255.0, 255.0 * 255.0, 255.0 * 255.0 * 255.0);
    const vec4 bitMask     = vec4(1.0/255.0,1.0/255.0,1.0/255.0,0.0);

    float normalizedDistance  = v_final_pos.z / v_final_pos.w;
    normalizedDistance = (normalizedDistance + 1.0) / 2.0;

    vec4 packedValue = vec4(fract(packFactors*normalizedDistance));
    packedValue -= packedValue.yzww * bitMask;

    gl_FragColor = packedValue;
}