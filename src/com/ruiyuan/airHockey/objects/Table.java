package com.ruiyuan.airHockey.objects;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;

import com.ruiyuan.airHockey.Constants;
import com.ruiyuan.airHockey.data.VertexArray;

public class Table {
	private static final int POSITION_COMPONENT_COUNT = 2;
	private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
	private static final int STRIDE = 
			(POSITION_COMPONENT_COUNT+TEXTURE_COORDINATES_COMPONENT_COUNT)*Constants.BYTES_PER_FLOAT;
	
	private static final float[] VERTEX_DATA = {
		//X Y S T
		
		   0f,	 0f,	0.5f, 0.5f,
		-0.5f,-0.8f,      0f, 0.9f,
		 0.5f,-0.8f,      1f, 0.9f,
		 0.5f,-0.8f,      1f, 0.1f,
		-0.5f, 0.8f,      0f, 0.1f,
		-0.5f,-0.8f,      0f, 0.9f
		
	};
	
	private final VertexArray vertexArray;
	
	public Table(){
		vertexArray = new VertexArray(VERTEX_DATA);
	}
	
	public void bindData(TextureShaderProgram textureShaderPorgram){
		vertexArray.setVertexAttribPoint(0,
										 textureShaderPorgram.getPositionAttributeLocation(),
										 POSITION_COMPONENT_COUNT,
										 STRIDE);
		vertexArray.setVertexAttribPoint(POSITION_COMPONENT_COUNT, 
										 textureShaderPorgram.getTextureCoordinatesAttributeLocation,
										 TEXTURE_COORDINATES_COMPONENT_COUNT,
										 STRIDE);		
		
	}
	public void draw(){
		glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
	}
}
