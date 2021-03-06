attribute vec4 a_position;
attribute vec4 a_normal;
attribute vec2 a_texCoord0;

uniform mat4 u_mvp;
uniform mat4 u_rot;
uniform mat4 u_model;
uniform mat4 u_light_bias_mvp;

varying vec4 v_shadow_coord;
varying vec4 v_position;
varying vec4 v_normal;
varying vec2 v_texCoords;
varying vec4 v_final_pos;

void main()
{
    v_texCoords = a_texCoord0;
    v_position = u_model * a_position;
    v_normal = u_rot * a_normal;
    gl_Position =  u_mvp * a_position;
    v_final_pos = gl_Position;
    v_shadow_coord = u_light_bias_mvp * a_position;
}