precision highp float;

varying vec2		vTexcoords;
varying vec4		vColor;

uniform sampler2D	samplerY;
uniform sampler2D	samplerU;
uniform sampler2D	samplerV;

void main() 
{
	mediump vec3 yuv;

	yuv.x = texture2D(samplerY, vTexcoords).x;
	yuv.y = texture2D(samplerU, vTexcoords).x - 0.5;
    yuv.z = texture2D(samplerV, vTexcoords).x - 0.5;

	mediump vec3 rgb = mat3(	1,		1,			1
							,	0,		-.34413,	1.772
							,	1.402,	-.71414,	0) * yuv;


	gl_FragColor = vec4(rgb, 1.0);
}
