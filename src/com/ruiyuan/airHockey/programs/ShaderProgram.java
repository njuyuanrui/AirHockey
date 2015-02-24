package com.ruiyuan.airHockey.programs;

import static android.opengl.GLES20.glUseProgram;

import com.ruiyuan.airHockey.util.ShaderHelper;
import com.ruiyuan.airHockey.util.TextResourceReader;

import android.R.integer;
import android.content.Context;

public class ShaderProgram {
	//uniform
	protected static final String U_MATRIX = "u_Matrix";
	protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
	
	//attribute
	protected static final String A_POSITION = "a_Position";
	protected static final String A_COLOR = "a_Color";
	protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
	
	//ShaderProgram
	protected final int program;
	protected ShaderProgram(Context context,int vertexShaderResourceID,int fragmentShaderResourceID){
		
		program = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context, vertexShaderResourceID),
											TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceID));
		
	}
	
	public void useProgram(){
		glUseProgram(program);
	}
	
	
}
