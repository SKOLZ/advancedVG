varying vec4 v_final_pos;

void main() {
    gl_FragColor = pack(v_final_pos);
}