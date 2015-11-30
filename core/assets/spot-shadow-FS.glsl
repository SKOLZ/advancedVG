#version 120

varying vec4 v_color;
varying vec4 v_normal;
varying vec2 v_texCoords;
varying vec4 v_position;
varying vec4 v_shadow_coord;

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

uniform sampler2D u_shadow_map;


float unpack(const vec4 rgba_depth) {
    const vec4 bit_shift = vec4(1.0/(256.0*256.0*256.0), 1.0/(256.0*256.0), 1.0/256.0, 1.0);
    float depth = dot(rgba_depth, bit_shift);
    return depth;
}

void main() {
  const vec2 poissonDisk[9] = vec2[](
      vec2(0.95581, -0.18159), vec2(0.50147, -0.35807), vec2(0.69607, 0.35559),
      vec2(-0.0036825, -0.59150), vec2(0.15930, 0.089750), vec2(-0.65031, 0.058189),
      vec2(0.11915, 0.78449), vec2(-0.34296, 0.51575), vec2(-0.60380, -0.41527)
    );

  vec4 texture_color = texture2D(u_texture, v_texCoords);
  vec4 position = v_position;
  vec4 normal = normalize(v_normal);
  vec4 direction = normalize(u_light_pos - position);
  float inside_light = 1.0f;
  float bias = 0.0005; // * tan(acos(clamp(dot(normal, u_light_dir), 0, 1)));
  vec3 convertedShadowCoord = (v_shadow_coord.xyz + vec3(1,1,1)) / 2.0;
       // bias = clamp(bias, 0, 0.0005);
   //+ poissonDisk[i]/700.0)));
  float shadow = 1.0;
  if(v_shadow_coord.x <= 1.0 && v_shadow_coord.x >= -1.0 && v_shadow_coord.y <= 1.0 && v_shadow_coord.y >= -1.0){
      if ( convertedShadowCoord.z - bias > unpack(texture2D(u_shadow_map, convertedShadowCoord.xy))){
        shadow -= 0.7;
      }
  }

  float angle_cos = dot(direction, normalize(u_light_dir));
  if(angle_cos < u_light_max_angle_cos) {
      if(u_light_max_angle_cos > 0.0001f) {
          inside_light = angle_cos * 0.25f;
      }
      
  }
  inside_light *= shadow;
  if(inside_light < 0.0f) {
          inside_light = 0.0f;
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
