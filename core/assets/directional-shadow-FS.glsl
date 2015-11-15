#version 120

uniform vec4 u_light_dir;
uniform vec4 u_light_in;
uniform vec4 u_light_c;

uniform vec4 u_camera_pos;
uniform int u_shininess;

uniform mat4 u_model;
uniform mat4 u_rot;
uniform sampler2D u_texture;

uniform sampler2D u_shadow_map;

varying vec4 v_position;
varying vec4 v_normal;
varying vec2 v_texCoords;
varying vec4 v_shadow_coord;

void main()
{
	vec4 tex_color = texture2D(u_texture, v_texCoords);
	vec4 normal_w = normalize(u_rot * v_normal);

	const vec2 poissonDisk[9] = vec2[](
    	vec2(0.95581, -0.18159), vec2(0.50147, -0.35807), vec2(0.69607, 0.35559),
    	vec2(-0.0036825, -0.59150),	vec2(0.15930, 0.089750), vec2(-0.65031, 0.058189),
    	vec2(0.11915, 0.78449),	vec2(-0.34296, 0.51575), vec2(-0.60380, -0.41527)
    );

	/* DIFFUSE */
	vec4 after_light = max(0, (dot(normal_w, normalize(u_light_dir)))) * tex_color;
	vec4 diffuse_component = after_light * u_light_c * u_light_in;

	/* SPECULAR */
	vec4 v_vec = normalize(u_camera_pos - (u_model * v_position));
	vec4 r_vec = reflect(-normalize(u_light_dir), normal_w);
	vec4 after_light_spec = max(0, pow(dot(r_vec, v_vec), u_shininess)) * tex_color;
	vec4 specular_component = after_light_spec * u_light_c * u_light_in;

	float bias = 0.0005 * tan(acos(clamp(dot(normal_w, u_light_dir), 0, 1)));
	bias = clamp(bias, 0, 0.0005);
    float visibility = 1.0;
	const vec4 unpackFactors = vec4(1, 1/255.0, 1/(255.0 * 255.0), 1/(255.0 * 255.0 * 255.0));
	for (int i=0; i < 9; i++)
	{
		vec4 txt = texture2D(u_shadow_map, ((v_shadow_coord.xy + poissonDisk[i]/700.0)));
       	float pDotU = dot(txt, unpackFactors);
    	if (pDotU < (v_shadow_coord.z - bias))
    	{
    		visibility -= 0.1;
		}
    }

	gl_FragColor = vec4(diffuse_component.xyz * visibility + specular_component.xyz * visibility, 1);
}