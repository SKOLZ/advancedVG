attribute vec4 a_position;attribute vec4 a_normal;attribute vec2 a_texCoord0;uniform mat4 u_mvp;uniform mat4 u_model;uniform mat4 u_rot;varying vec4 v_color;varying vec2 v_texCoords;varying vec4 v_normal;varying vec4 v_position;void main(){    v_color = vec4(1, 1, 1, 1);    v_texCoords = a_texCoord0;    v_normal = u_rot * a_normal;    v_position = u_model * a_position;    gl_Position =  u_mvp * a_position;}