#version 120

uniform sampler2D u_texture;

varying vec2 v_texCoords;

//%include shaders/utils.glsl

float unpack(const vec4 rgba_depth)
{
    const vec4 bit_shift = vec4(1.0/(256.0*256.0*256.0), 1.0/(256.0*256.0), 1.0/256.0, 1.0);
    float depth = dot(rgba_depth, bit_shift);
    return depth;
}

void main()
{
    float z = unpack(texture2D(u_texture, v_texCoords));
    gl_FragColor = vec4(z, z, z, 1);
}