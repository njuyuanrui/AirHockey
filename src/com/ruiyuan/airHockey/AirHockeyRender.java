package com.ruiyuan.airHockey;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.ruiyuan.airHockey.objects.Mallet;
import com.ruiyuan.airHockey.objects.Puck;
import com.ruiyuan.airHockey.objects.Table;
import com.ruiyuan.airHockey.programs.ColorShaderProgram;
import com.ruiyuan.airHockey.programs.TextureShaderProgram;
import com.ruiyuan.airHockey.util.Geometry;
import com.ruiyuan.airHockey.util.Geometry.Point;
import com.ruiyuan.airHockey.util.Geometry.Ray;
import com.ruiyuan.airHockey.util.Geometry.Sphere;
import com.ruiyuan.airHockey.util.Geometry.Vector;
import com.ruiyuan.airHockey.util.Geometry.Plane;
import com.ruiyuan.airHockey.util.MatrixHelper;
import com.ruiyuan.airHockey.util.TextureHelper;

public class AirHockeyRender implements Renderer {
	private final Context context;
	
	private final float[] projectionMatrix= new float[16];
	private final float[] modelMatrix = new float[16];
	
	private final float[] viewMatrix = new float[16];
	private final float[] viewProjectMatrix = new float[16];
	private final float[] modelViewProjectMatrix = new float[16];
	
	private final float[] invertedViewProjectionMatrix = new float[16];
	
	private final float leftBound = -0.5f;
	private final float rightBound=0.5f;
	private final float farBound = -0.8f;
	private final float nearBound = 0.8f;
	
	private Table table;
	private Mallet mallet;
	private Puck puck;
	
	private TextureShaderProgram textureProgram;
	private ColorShaderProgram colorProgram;
	
	private int texture;
	
	private boolean malletPressed =false;
	private Point blueMalletPositionPoint;
	private Point previousBlueMalletPosition;
	private Point puckPosition;
	private Vector puckVector;
	
	
	
	public AirHockeyRender(Context context){
		this.context = context;
	}
	
	
	@Override
	public void onDrawFrame(GL10 arg0) {
		// TODO Auto-generated method stub
		puckPosition = puckPosition.translate(puckVector);
		if((puckPosition.x<leftBound+puck.radius)||
			(puckPosition.x>rightBound-puck.radius)){
			puckVector = new Vector(-puckVector.x, puckVector.y, puckVector.z);
			puckVector = puckVector.scale(0.9f);
		}
		
		if((puckPosition.z<farBound+puck.radius)||
				(puckPosition.z>nearBound-puck.radius)){
				puckVector = new Vector(puckVector.x, puckVector.y, -puckVector.z);
				puckVector = puckVector.scale(0.9f);
		}
		
		
		puckPosition = new Point(clamp(puckPosition.x, leftBound+puck.radius, rightBound-puck.radius),
								 puckPosition.y,
								 clamp(puckPosition.z, farBound+puck.radius, nearBound-puck.radius));
		
		puckVector = puckVector.scale(0.99f);
		glClear(GL_COLOR_BUFFER_BIT);
		multiplyMM(viewProjectMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
		invertM(invertedViewProjectionMatrix, 0, viewProjectMatrix, 0);
		
		positionTableInScene();
		textureProgram.useProgram();
		textureProgram.setUniforms(modelViewProjectMatrix, texture);
		table.bindData(textureProgram);
		table.draw();
		
		positionObjectInScene(0f,mallet.height/2f,-0.4f);
		colorProgram.useProgram();
		colorProgram.setUniforms(modelViewProjectMatrix, 1f, 0f, 0f);
		mallet.bindData(colorProgram);
		mallet.draw();
		
		positionObjectInScene(blueMalletPositionPoint.x,blueMalletPositionPoint.y,blueMalletPositionPoint.z);
		colorProgram.setUniforms(modelViewProjectMatrix, 0f, 0f, 1f);
		mallet.draw();
		
		positionObjectInScene(puckPosition.x,puckPosition.y	,puckPosition.z);
		colorProgram.setUniforms(modelViewProjectMatrix, 0.8f,0.8f, 1f);
		puck.bindData(colorProgram);
		puck.draw();
		
	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height) {
		// TODO Auto-generated method stub
		glViewport(0,0,width,height);
		
		MatrixHelper.perspectiveM(projectionMatrix, 45, (float)width/(float)height, 1f, 10f);
		setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.5f, 0f, 0f, 0f, 0f, 1f, 0f);
		/*setIdentityM(modelMatrix, 0);
		translateM(modelMatrix, 0, 0f, 0f, -2.7f);
		rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);
		final float[] temp = new float[16];
		multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
		System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);*/
		
		
		
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
		// TODO Auto-generated method stub
		glClearColor(0.0f,0.0f,0.0f,0.0f);
		
		table = new Table();
		mallet = new Mallet(0.08f,0.15f,32);
		puck = new Puck(0.06f, 0.02f, 32);
		puckPosition = new Point(0f, puck.height/2f, 0f);
		puckVector = new Vector(0f, 0f, 0f);
		
		textureProgram = new TextureShaderProgram(context);
		colorProgram = new ColorShaderProgram(context);
		
		texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
		blueMalletPositionPoint = new Point(0f, mallet.height/2f, 0.4f);
		
		
	}

	private void positionTableInScene(){
		setIdentityM(modelMatrix, 0);
		rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f);
		multiplyMM(modelViewProjectMatrix, 0, viewProjectMatrix, 0, modelMatrix, 0);
	}

	private void positionObjectInScene(float x, float y, float z){
		setIdentityM(modelMatrix, 0);
		translateM(modelMatrix, 0, x, y, z);
		multiplyMM(modelViewProjectMatrix, 0, viewProjectMatrix, 0, modelMatrix, 0);
	}


	public void handleRouchPress(float normalizedX, float normalizedY) {
		Ray ray = convertNormalized2DPointToRay(normalizedX,normalizedY);
		
		Sphere malletBoundingSphere = new Sphere(new Point(blueMalletPositionPoint.x, 
														   blueMalletPositionPoint.y,
														   blueMalletPositionPoint.z),
												mallet.height/2f);
		malletPressed = Geometry.intersects(malletBoundingSphere,ray);
		//Log.v("raycasting", malletPressed?"True":"False");
	}


	public void handleTouchDrag(float normalizedX, float normalizedY) {
		if(malletPressed){
			Ray ray = convertNormalized2DPointToRay(normalizedX, normalizedY);
			Plane plane = new Plane(new Point(0,0,0),new Vector(0,1,0));
			Point touchedPoint = Geometry.intersectionPoint(ray,plane);
			previousBlueMalletPosition = blueMalletPositionPoint;
			blueMalletPositionPoint = new Point(
							clamp(touchedPoint.x,leftBound+mallet.radius,rightBound-mallet.radius),
							mallet.height/2f,
							clamp(touchedPoint.z,0f+mallet.radius,nearBound-mallet.radius));
			float distance = Geometry.vectorBetween(blueMalletPositionPoint, puckPosition).length();
			
			if(distance<(puck.radius+mallet.radius)){
				puckVector = Geometry.vectorBetween(previousBlueMalletPosition, blueMalletPositionPoint);
			}
			
			
		}		
	}
	
	private Ray convertNormalized2DPointToRay(float normalizedX,float normalizedY ){
		final float[] nearPointNdc = {normalizedX,normalizedY,-1,1};
		final float[] farPointNdc = {normalizedX,normalizedY,1,1};
		
		final float[] nearPointWorld = new float[4];
		final float[] farPointWorld = new float[4];
		
		multiplyMV(nearPointWorld, 0, invertedViewProjectionMatrix, 0, nearPointNdc, 0);
		multiplyMV(farPointWorld, 0, invertedViewProjectionMatrix, 0, farPointNdc, 0);
		divideByW(farPointWorld);
		divideByW(nearPointWorld);
		
		Point nearPointRay = new Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);
		Point farPointRay = new Point(farPointWorld[0], farPointWorld[1], farPointWorld[2]);
		return new Ray(nearPointRay,Geometry.vectorBetween(nearPointRay,farPointRay));
		
	}
	
	
	private void divideByW(float[] vector){
		vector[0]/=vector[3];
		vector[1]/=vector[3];
		vector[2]/=vector[3];
	}
	
	private float clamp(float value,float min,float max){
		return Math.min(max,Math.max(value, min));
	}
}
