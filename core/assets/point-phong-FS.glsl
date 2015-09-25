varying vec4 v_color;
varying vec4 v_normal;
varying vec2 v_texCoords;
varying vec4 v_position;

// MODEL VARIABLES
uniform sampler2D u_texture;
uniform mat4 u_model;

// LIGHT VARIABLES

// Light position
uniform vec4 u_light_pos;

// Light color
uniform vec4 u_light_c;

void main() {
    vec4 texture_color = texture2D(u_texture, v_texCoords);
	vec4 position = v_position;
	vec4 normal = normalize(v_normal);
	vec3 direction = normalize(u_light_pos.xyz - position.xyz);

	vec4 light = max(0, (dot(normal.xyz, direction))) * texture_color;
    vec4 diffuse = light * u_light_c;
    gl_FragColor = vec4(diffuse.xyz, 1);
}
