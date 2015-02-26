package com.ruiyuan.airHockey.objects;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.Matrix.length;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.os.Build;
import android.util.FloatMath;

import com.ruiyuan.airHockey.util.Geometry.Cylinder;
import com.ruiyuan.airHockey.util.Geometry.Circle;
import com.ruiyuan.airHockey.util.Geometry.Point;


public class ObjectBuilder {
	
	static interface DrawCommand{
		void draw();
	}
	
	static class GenerateData{
		final float[] vertexData;
		final List<DrawCommand> drawList;
		
		public GenerateData(float[] vertexData , List<DrawCommand> drawList) {
			this.vertexData=vertexData;
			this.drawList=drawList;
		}
		
	}
	
	private static final int FLOATS_PER_VERTEX = 3;
	private final float[] vertexData;
	private final List<DrawCommand> drawList = new ArrayList<DrawCommand>();
	private int offset = 0;
	
	private ObjectBuilder(int sizeInVertices){
		vertexData = new float[sizeInVertices * FLOATS_PER_VERTEX];
	}
	
	private static int sizeOfCircleInVertices(int numPoints){
		return 1+numPoints+1;//����+��β���ظ�
	}
	
	private static int sizeOfOpenCylinderInVertices(int numPoints){
		return (1+numPoints)*2;
	}
	
	static GenerateData createPuck(Cylinder puck,int numPoints){
		int size = sizeOfCircleInVertices(numPoints)
		  		   +sizeOfOpenCylinderInVertices(numPoints);
	    ObjectBuilder builder = new ObjectBuilder(size);
	    
	    Circle puckTop = new Circle(
	    	puck.center.translateY(puck.height/2f),puck.radius);
	    
	    builder.appendCircle(puckTop,numPoints);
	    builder.appendOpenCylinder(puck ,numPoints);
	    
	    return builder.build();
	}
	
	static GenerateData createMallet(Point center,float radius,float height,int numPoints){
		int size = sizeOfCircleInVertices(numPoints)*2
		  		   +sizeOfOpenCylinderInVertices(numPoints)*2;
		float baseHeight = height*0.25f;
		ObjectBuilder builder = new ObjectBuilder(size);
		
		Circle baseCircle  = new Circle(center.translateY(-baseHeight), radius);
		Cylinder baseCylinder = new Cylinder(baseCircle.center.translateY(-baseHeight/2f), radius, baseHeight);
		
		builder.appendCircle(baseCircle, numPoints);
		builder.appendOpenCylinder(baseCylinder, numPoints);
		
		float handleHeight = height*0.75f;
		float handleRadius = radius/3f;
		
		Circle handleCircle = new Circle(center.translateY(height*0.5f),handleRadius);
		Cylinder handleCylinder = new Cylinder(handleCircle.center.translateY(-handleHeight/2f), handleRadius, handleHeight);
		
		builder.appendCircle(handleCircle, numPoints);
		builder.appendOpenCylinder(handleCylinder, numPoints);
		
		return builder.build();
		
	}
	
	public GenerateData build(){
		
		return new GenerateData(vertexData, drawList);
	}
	
	private void appendCircle(Circle circle , int numPoints){
		final int startVertex = offset/FLOATS_PER_VERTEX;
		final int numVertices = sizeOfCircleInVertices(numPoints);
		
		vertexData[offset++] = circle.center.x;
		vertexData[offset++] = circle.center.y;
		vertexData[offset++] = circle.center.z;
		
		for(int i=0;i<=numPoints;i++){
			float angleInRadians = 
					((float)i/(float)numPoints)*((float)Math.PI*2f);
			
			vertexData[offset++]=circle.center.x+circle.radius*FloatMath.cos(angleInRadians);
			vertexData[offset++]=circle.center.y;
			vertexData[offset++]=circle.center.z+circle.radius*FloatMath.sin(angleInRadians);
		}
		drawList.add(new DrawCommand() {
			
			@Override
			public void draw() {
				glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices);
				
			}
		});
		
	}
	
	private void appendOpenCylinder(Cylinder cylinder, int numPoints){
		final int startVertex = offset/FLOATS_PER_VERTEX;
		final int numVertices = sizeOfOpenCylinderInVertices(numPoints);
		final float yStart = cylinder.center.y-(cylinder.height/2f);
		final float yEnd = cylinder.center.y+(cylinder.height/2f);
		
		for(int i = 0 ; i<= numPoints; i++){
			float angleInRadians = 
					((float)i/(float)numPoints)*((float)Math.PI*2f);
			
			float xPosition = cylinder.center.x+cylinder.radius*FloatMath.cos(angleInRadians);
			float zPosition = cylinder.center.z+cylinder.radius*FloatMath.sin(angleInRadians);
			

			vertexData[offset++]=xPosition;
			vertexData[offset++]=yStart;
			vertexData[offset++]=zPosition;
			
			vertexData[offset++]=xPosition;
			vertexData[offset++]=yEnd;
			vertexData[offset++]=zPosition;
			
			
		}
		
		drawList.add(new DrawCommand() {
			
			@Override
			public void draw() {
				glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices);				
			}
		});
		
		
	}
	
	
	

	
}
