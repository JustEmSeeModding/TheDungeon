#version 150

#moj_import <matrix.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

uniform float GameTime;
uniform int EndPortalLayers;
uniform vec4 VertexColor;

in vec4 texProj0;


const vec3[] COLORS = vec3[](
vec3(0.27, 0.29, 0.30),
vec3(0.25, 0.27, 0.26),
vec3(0.27, 0.28, 0.28),
vec3(0.30, 0.31, 0.32),
vec3(0.31, 0.32, 0.31),
vec3(0.30, 0.30, 0.31),
vec3(0.34, 0.35, 0.36),
vec3(0.34, 0.35, 0.34),
vec3(0.36, 0.37, 0.38),
vec3(0.35, 0.36, 0.37),
vec3(0.37, 0.38, 0.38),
vec3(0.43, 0.44, 0.44),
vec3(0.44, 0.44, 0.45),
vec3(0.48, 0.48, 0.48),
vec3(0.57, 0.58, 0.57),
vec3(0.62, 0.63, 0.63)
);

const mat4 SCALE_TRANSLATE = mat4(
    0.5, 0.0, 0.0, 0.25,
    0.0, 0.5, 0.0, 0.25,
    0.0, 0.0, 1.0, 0.0,
    0.0, 0.0, 0.0, 1.0
);

mat4 end_portal_layer(float layer) {
    mat4 translate = mat4(
        1.0, 0.0, 0.0, 17.0 / layer,
        0.0, 1.0, 0.0, (2.0 + layer / 1.5) * (GameTime * 1.5),
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    );

    mat2 rotate = mat2_rotate_z(radians((layer * layer * 4321.0 + layer * 9.0) * 2.0));

    mat2 scale = mat2((4.5 - layer / 4.0) * 2.0);

    return mat4(scale * rotate) * translate * SCALE_TRANSLATE;
}

out vec4 fragColor;

void main() {
    vec3 color = textureProj(Sampler0, texProj0).rgb * COLORS[0];
    for (int i = 0; i < EndPortalLayers; i++) {
        color += textureProj(Sampler1, texProj0 * end_portal_layer(float(i + 1))).rgb * COLORS[i];
    }
    fragColor = vec4(color * VertexColor.rgb, VertexColor.a);
}
