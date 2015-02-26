package com.ruiyuan.airHockey.programs;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;

import com.ruiyuan.airHockey.R;

import android.content.Context;

public class ColorShaderProgram extends ShaderProgram {
	private final int uMatrixLocation;
	
	private final int aPositionLocation;
	private final int uColorLocation;
	
	public ColorShaderProgram(Context context){
		super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);
		
		uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		uColorLocation = glGetAttribLocation(program, U_COLOR);
		
	}
	
	public void setUniforms(float[] matrix,float r,float g ,float b){
		glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
		glUniform4f(uColorLocation, r, g, b, 1f);
		
	}
	public int getPositionAttributeLocation(){
		return aPositionLocation;
	}
	public int getColorAttributeLocation(){
		return uColorLocation;
	}
}
