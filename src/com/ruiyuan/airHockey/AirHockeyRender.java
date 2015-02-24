package com.ruiyuan.airHockey;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.ruiyuan.airHockey.util.LoggerConfig;
import com.ruiyuan.airHockey.util.MatrixHelper;
import com.ruiyuan.airHockey.util.ShaderHelper;
import com.ruiyuan.airHockey.util.TextResourceReader;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

public class AirHockeyRender implements Renderer {
	private final Context context;
	private static final int POSITION_COMPONENT_COUNT=2;
	private static final int BYTES_PER_FLOAT = 4;
	private final FloatBuffer vertexData;
	private int program;
	
	private static final String A_COLOR = "a_Color";
	private static final int COLOR_COMPONENT_COUNT=3;
	private static final int STRIDE = (POSITION_COMPONENT_COUNT+COLOR_COMPONENT_COUNT)*BYTES_PER_FLOAT;
	private int aColorLocation;
	
	private static final String A_POSITION = "a_Position";
	private int aPositionLocation;
	
	private static final String U_MATRIX ="u_Matrix";
	private final float[] projectionMatrix= new float[16];
	private int uMatrixLocation;
	
	private final float[] modelMatrix = new float[16];
	
	
	public AirHockeyRender(Context context){
		this.context = context;
		float[] tableVerticesWithTriangles = {
				
				//Order of coordinates X Y Z W R G B
				//triangle fan
				   0f,   0f,    1f,   1f,   1f,
				-0.5f,-0.8f,   0.7f, 0.7f, 0.7f,
				 0.5f,-0.8f,   0.7f, 0.7f, 0.7f,
				 0.5f, 0.8f,   0.7f, 0.7f, 0.7f,
				-0.5f, 0.8f,   0.7f, 0.7f, 0.7f,
				-0.5f,-0.8f,   0.7f, 0.7f, 0.7f,
				
				
				
				//line 1
				-0.5f,0f,  1f,  0f,  0f,
				0.5f,0f,   1f,  0f,  0f,
				
				//mallet
				0f,-0.4f,  0f,  0f,  1f,
				0f,0.4f,   1f,  0f,  0f
				
		};
		
		vertexData = ByteBuffer
				.allocateDirect(tableVerticesWithTriangles.length*BYTES_PER_FLOAT)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		vertexData.put(tableVerticesWithTriangles);
		
		
	}
	
	
	@Override
	public void onDrawFrame(GL10 arg0) {
		// TODO Auto-generated method stub
		glClear(GL_COLOR_BUFFER_BIT);
		glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);
		//绘制桌面
		glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
		//绘制分割线
		glDrawArrays(GL_LINES, 6, 2);
		//绘制木槌
		glDrawArrays(GL_POINTS, 8, 1);
		glDrawArrays(GL_POINTS, 9, 1);
		
		
	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height) {
		// TODO Auto-generated method stub
		glViewport(0,0,width,height);
		
		MatrixHelper.perspectiveM(projectionMatrix, 45, (float)width/(float)height, 1f, 10f);
		setIdentityM(modelMatrix, 0);
		translateM(modelMatrix, 0, 0f, 0f, -2.7f);
		rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);
		final float[] temp = new float[16];
		multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
		System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
		
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
		// TODO Auto-generated method stub
		glClearColor(0.0f,0.0f,0.0f,0.0f);
		String vertexShaderSource =
				TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);
		String fragmentShaderSource = 
				TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader);
		
		int vertexShader =	ShaderHelper.compileVertexShader(vertexShaderSource);
		int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
		
		//验证代码
		program = ShaderHelper.LinkProgram(vertexShader, fragmentShader);
		if(LoggerConfig.ON){
			ShaderHelper.validateProgram(program);
			
		}
		
		glUseProgram(program);
		aColorLocation = glGetAttribLocation(program, A_COLOR);
		aPositionLocation=glGetAttribLocation(program, A_POSITION);
		
		vertexData.position(0);
		glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
		glEnableVertexAttribArray(aPositionLocation);
		
		vertexData.position(POSITION_COMPONENT_COUNT);
		glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
		glEnableVertexAttribArray(aColorLocation);
		
		uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
		
	}

	

}
