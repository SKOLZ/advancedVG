attribute vec4 a_position;
attribute vec4 a_normal;
attribute vec2 a_texCoord0;

attribute vec2 a_boneWeight0;
attribute vec2 a_boneWeight1;
attribute vec2 a_boneWeight2;
attribute vec2 a_boneWeight3;
attribute vec2 a_boneWeight4;
attribute vec2 a_boneWeight5;
attribute vec2 a_boneWeight6;
attribute vec2 a_boneWeight7;
attribute vec2 a_boneWeight8;
attribute vec2 a_boneWeight9;
attribute vec2 a_boneWeight10;
attribute vec2 a_boneWeight11;

uniform mat4 u_mvp;
uniform mat4 u_model;

varying vec4 v_position;
varying vec4 v_normal;
varying vec2 v_texCoords;

uniform mat4 u_bones[12];

void main()
{
    mat4 skinning = mat4(0.0);
    skinning += (a_boneWeight0.y) * u_bones[int(a_boneWeight0.x)];
    skinning += (a_boneWeight1.y) * u_bones[int(a_boneWeight1.x)];
    skinning += (a_boneWeight2.y) * u_bones[int(a_boneWeight2.x)];
    skinning += (a_boneWeight3.y) * u_bones[int(a_boneWeight3.x)];
    skinning += (a_boneWeight4.y) * u_bones[int(a_boneWeight4.x)];
    skinning += (a_boneWeight5.y) * u_bones[int(a_boneWeight5.x)];
   	skinning += (a_boneWeight6.y) * u_bones[int(a_boneWeight6.x)];
   	skinning += (a_boneWeight7.y) * u_bones[int(a_boneWeight7.x)];
   	skinning += (a_boneWeight8.y) * u_bones[int(a_boneWeight8.x)];
   	skinning += (a_boneWeight9.y) * u_bones[int(a_boneWeight9.x)];
   	skinning += (a_boneWeight10.y) * u_bones[int(a_boneWeight10.x)];
	skinning += (a_boneWeight11.y) * u_bones[int(a_boneWeight11.x)];

    vec4 pos = skinning * vec4(a_position.xyz, 1.0);
    gl_Position =  u_mvp * pos;

    v_texCoords = a_texCoord0;
    v_position = pos;
    v_normal = skinning * vec4(a_normal.xyz, 0);
    v_position = gl_Position;
}