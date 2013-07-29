precision highp float;

attribute	vec4	aVertices;
attribute	vec2	aTexcoords;
attribute	vec4	aColor;
attribute	float	aWidth;
attribute	float	aHeight;

varying		vec2	vTexcoords;
varying		vec4	vColor;
varying		float	vWidth;
varying		float	vHeight;

void main()
{
	vec4 vertices = aVertices;
	vertices.y = -1.0 * vertices.y;
	
	vWidth = aWidth;
	vHeight = aHeight;
	
	gl_Position = vertices;
	
	vTexcoords = aTexcoords;
	
	vColor = aColor;
}
