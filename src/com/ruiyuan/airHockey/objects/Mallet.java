package com.ruiyuan.airHockey.objects;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;


import com.ruiyuan.airHockey.Constants;
import com.ruiyuan.airHockey.data.VertexArray;
import com.ruiyuan.airHockey.programs.ColorShaderProgram;

public class Mallet {
	private static final int POSITION_COMPONENT_COUNT = 2;
	private static final int COLOR_COMPONENT_COUNT = 3;
	private static final int STRIDE = 
			(POSITION_COMPONENT_COUNT+COLOR_COMPONENT_COUNT)*Constants.BYTES_PER_FLOAT;
	
	private static final float[] VERTEX_DATA =new float[] {
		//X Y R G B
		
		0f, -0.4f , 0f, 0f,1f,
		0f, 0.4f,   1f, 0f,0f,
	};
	
	private VertexArray vertexArray;
	
	public Mallet(){
		vertexArray = new VertexArray(VERTEX_DATA);
		
	}

	public void bindData(ColorShaderProgram colorPorgram){
		vertexArray.setVertexAttribPoint(0,
										 colorPorgram.getPositionAttributeLocation(),
										 POSITION_COMPONENT_COUNT,
										 STRIDE);
		vertexArray.setVertexAttribPoint(POSITION_COMPONENT_COUNT, 
										 colorPorgram.getColorAttributeLocation(),
										 COLOR_COMPONENT_COUNT,
										 STRIDE);		
		
	}
	public void draw(){
		glDrawArrays(GL_POINTS, 0, 2);
	}
	
}
