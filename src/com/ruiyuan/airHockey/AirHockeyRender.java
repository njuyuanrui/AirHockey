package com.ruiyuan.airHockey;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import com.ruiyuan.airHockey.objects.Mallet;
import com.ruiyuan.airHockey.objects.Table;
import com.ruiyuan.airHockey.programs.ColorShaderProgram;
import com.ruiyuan.airHockey.programs.TextureShaderProgram;
import com.ruiyuan.airHockey.util.MatrixHelper;
import com.ruiyuan.airHockey.util.TextureHelper;

public class AirHockeyRender implements Renderer {
	private final Context context;
	
	private final float[] projectionMatrix= new float[16];
	private final float[] modelMatrix = new float[16];
	
	private Table table;
	private Mallet mallet;
	
	private TextureShaderProgram textureProgram;
	private ColorShaderProgram colorProgram;
	
	private int texture;
	
	public AirHockeyRender(Context context){
		this.context = context;
	}
	
	
	@Override
	public void onDrawFrame(GL10 arg0) {
		// TODO Auto-generated method stub
		glClear(GL_COLOR_BUFFER_BIT);
		
		textureProgram.useProgram();
		textureProgram.setUniforms(projectionMatrix, texture);
		table.bindData(textureProgram);
		table.draw();
		
		colorProgram.useProgram();
		colorProgram.setUniforms(projectionMatrix);
		mallet.bindData(colorProgram);
		mallet.draw();
		
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
		
		table = new Table();
		mallet = new Mallet();
		
		textureProgram = new TextureShaderProgram(context);
		colorProgram = new ColorShaderProgram(context);
		
		texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
		
		
		
	}

	

}
