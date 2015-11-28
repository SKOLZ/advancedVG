varying vec4 v_color;
varying vec4 v_normal;
varying vec2 v_texCoords;
varying vec4 v_position;

// MODEL VARIABLES
uniform sampler2D u_texture;
uniform mat4 u_model;
uniform float u_shininess;

// CAMERA VARIABLES
uniform vec4 u_camera_pos;

// LIGHT VARIABLES

// Light position
uniform vec4 u_light_pos;

// Light color
uniform vec4 u_light_c;

// Light intensity
uniform float u_light_in;

// Light direction
uniform vec4 u_light_dir;

// Light max angle
uniform float u_light_max_angle_cos;

void main() {
    vec4 texture_color = texture2D(u_texture, v_texCoords);
	vec4 position = v_position;
	vec4 normal = normalize(v_normal);
	vec4 direction = normalize(u_light_pos - position);

    float inside_light = 1.0f;
    float angle_cos = dot(direction, normalize(u_light_dir));
	if(angle_cos < u_light_max_angle_cos) {
	    if(u_light_max_angle_cos > 0.0001f) {
	        inside_light = angle_cos * 0.25f;
	    }
	    if(inside_light < 0.0f) {
	        inside_light = 0.0f;
	    }
	}

	//diffuse
	vec4 light = max(0, (dot(normal.xyz, normalize(direction.xyz)))) * texture_color * u_light_in;
    vec4 diffuse = light * u_light_c;

    //specular
    vec4 v_vec = normalize(position * u_camera_pos - (u_model * position));
    vec4 r_vec = reflect(-normalize(direction), normal);
    vec4 resulting_light = max(0, pow(dot(r_vec, v_vec), u_shininess)) * texture_color;
    vec4 specular = resulting_light * u_light_c;

    gl_FragColor = vec4(diffuse.xyz * inside_light + specular.xyz * inside_light, 1);
}
