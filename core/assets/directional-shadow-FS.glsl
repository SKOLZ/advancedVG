#version 120

uniform vec4 u_light_dir;
uniform vec4 u_light_in;
uniform vec4 u_light_c;

uniform vec4 u_camera_pos;
uniform int u_shininess;

uniform mat4 u_model;
uniform sampler2D u_texture;

uniform sampler2D u_shadow_map;

varying vec4 v_position;
varying vec4 v_normal;
varying vec2 v_texCoords;
varying vec4 v_shadow_coord;

void main()
{
	vec4 texture_color = texture2D(u_texture, v_texCoords);
	vec4 normal = normalize(v_normal);

	const vec2 poissonDisk[9] = vec2[](
    	vec2(0.95581, -0.18159), vec2(0.50147, -0.35807), vec2(0.69607, 0.35559),
    	vec2(-0.0036825, -0.59150),	vec2(0.15930, 0.089750), vec2(-0.65031, 0.058189),
    	vec2(0.11915, 0.78449),	vec2(-0.34296, 0.51575), vec2(-0.60380, -0.41527)
    );

	//diffuse
  	vec4 light = max(0, (dot(normal.xyz, normalize(u_light_dir.xyz)))) * texture_color * u_light_in;
	vec4 diffuse = light * u_light_c;

	//specular
  	vec4 position = u_model * v_position;
  
	vec4 v_vec = normalize(u_camera_pos - position);
	vec4 r_vec = reflect(-normalize(u_light_dir), normal);
	vec4 resulting_light = max(0, pow(dot(r_vec, v_vec), u_shininess)) * texture_color;
	vec4 specular = resulting_light * u_light_c;
	
	float bias = 0.0005 * tan(acos(clamp(dot(normal, u_light_dir), 0, 1)));
  	bias = clamp(bias, 0, 0.0005);
  	float visibility = 1.0;
  	const vec4 unpackFactors = vec4(1, 1/255.0, 1/(255.0 * 255.0), 1/(255.0 * 255.0 * 255.0));
  	for (int i=0; i < 9; i++) {
    	vec4 txt = texture2D(u_shadow_map, ((v_shadow_coord.xy + poissonDisk[i]/700.0)));
    	float pDotU = dot(txt, unpackFactors);
		if (pDotU < (v_shadow_coord.z - bias)) {
	  		visibility -= 0.5;
		}
  	}
	gl_FragColor = vec4(diffuse.xyz * visibility + specular.xyz * visibility, 1);
}