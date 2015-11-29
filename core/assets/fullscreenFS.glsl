#version 120

uniform sampler2D u_texture;

varying vec2 v_texCoords;

//%include shaders/utils.glsl

float unpack(vec4 packedZValue)
{
	const vec4 unpackFactors = vec4(1, 1/255.0, 1/(255.0 * 255.0), 1/(255.0 * 255.0 * 255.0));
	return dot(packedZValue, unpackFactors);
}

void main()
{
    float z = texture2D(u_texture, v_texCoords).x;
    gl_FragColor = vec4(z, z, z, 1);
}