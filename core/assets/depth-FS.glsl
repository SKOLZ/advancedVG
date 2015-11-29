varying vec4 v_final_pos;

void main() {


    const vec4 bitSh = vec4(256.0*256.0*256.0, 256.0*256.0, 256.0, 1.0);
    const vec4 bitMsk = vec4(0.0, 1.0/256.0, 1.0/256.0, 1.0/256.0);
    vec4 res = fract(v_final_pos.z * bitSh);
    res -= res.xxyz * bitMsk;
    gl_FragColor = res;
}