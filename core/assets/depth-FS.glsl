varying vec4 v_final_pos;

void main() {
    vec3 shadow_coord = (v_final_pos.xyz + vec3(1,1,1)) / 2.0;
    const vec4 bitSh = vec4(256.0*256.0*256.0, 256.0*256.0, 256.0, 1.0);
    const vec4 bitMsk = vec4(0.0, 1.0/256.0 , 1.0/256.0, 1.0/256.0);
    vec4 res = fract(shadow_coord.z * bitSh);
    res -= res.xxyz * bitMsk;
    gl_FragColor = res;
}