package com.ruiyuan.airHockey.util;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLineWidth;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;
import android.R.integer;
import android.util.Log;

public class ShaderHelper {
	 private static final String TAG = "ShaderHelper";
	 
	 public static int compileVertexShader(String shaderCode){
		 return compileShader(GL_VERTEX_SHADER,shaderCode);	 
	 }
	 
	 public static int compileFragmentShader(String shaderCode){
		 return compileShader(GL_FRAGMENT_SHADER,shaderCode);		 
		 
	 }
	 
	 public static int compileShader(int type, String shaderCode){
		 final int shaderObjectID = glCreateShader(type);
		 
		 if(shaderObjectID ==0){
			 if(LoggerConfig.ON){
				 Log.w(TAG,"Could not creat new shader."); 
			 }
			 return 0;
		 }
		 
		 glShaderSource(shaderObjectID, shaderCode);
		 glCompileShader(shaderObjectID);
		 
		 final int[] compileStatus = new int[1];
		 glGetShaderiv(shaderObjectID, GL_COMPILE_STATUS, compileStatus, 0);
		 
		 if(LoggerConfig.ON){
			 Log.v(TAG,"Results of compiling source: "+"\n"+shaderCode+"\n"+glGetShaderInfoLog(shaderObjectID));
			 
		 }
		 
		 if(compileStatus[0]==0){
			 glDeleteShader(shaderObjectID);
			 if(LoggerConfig.ON){
				 Log.w(TAG,"Compile of shader failed!"); 
			 }
			 return 0;
		 }
		 return shaderObjectID;
	 }
	 
	 public static int LinkProgram(int vertexShaderID , int frameShaderID){
		 final int programObjectID = glCreateProgram();
		 
		 //��������ʧ��
		 if(programObjectID==0){
			 if(LoggerConfig.ON){
				 Log.w(TAG,"Could not creat new program"); 
			 }
			 return 0;
		 }
		 
		 glAttachShader(programObjectID,vertexShaderID);
		 glAttachShader(programObjectID, frameShaderID);
		 
		 glLinkProgram(programObjectID);
		 
		 final int [] linkStatus = new int[1];
		 glGetProgramiv(programObjectID, GL_LINK_STATUS, linkStatus, 0);
		 
		 if(LoggerConfig.ON){
			 Log.v(TAG,"Results of linking program:\n"+glGetProgramInfoLog(programObjectID)); 
		 }
	
		 if(linkStatus[0]==0){
			 glDeleteProgram(programObjectID);
			 if(LoggerConfig.ON){
				 Log.w(TAG,"Linking of program failed"); 
			 }
			 return 0;
			 
		 }
		 return programObjectID;
		 
	 }
	 
	 public static boolean validateProgram(int programObjectID){
		 glValidateProgram(programObjectID);
		 final int[] validateStatus = new int[1];
		 glGetProgramiv(programObjectID, GL_VALIDATE_STATUS, validateStatus, 0);
		 Log.v(TAG,"Results of validating program: "+validateStatus[0]+"\nLog: "+glGetProgramInfoLog(programObjectID)); 
		 
		 return validateStatus[0]!=0;
	 }
	 
	 public static int buildProgram(String vertexShaderSource, String fragmentShaderSource){
		 int program;
		 
		 int vertexShader = compileVertexShader(vertexShaderSource);
		 int fragmentShader = compileFragmentShader(fragmentShaderSource);
		 
		 program = LinkProgram(vertexShader, fragmentShader);
		 
		 if(LoggerConfig.ON){
			validateProgram(program);	
		}
		 return program;
	 }

}
