package com.ruiyuan.airHockey.objects;

import com.ruiyuan.airHockey.util.Geometry.Cylinder;
import com.ruiyuan.airHockey.util.Geometry.Circle;


public class ObjectBuilder {
	private static final int FLOATS_PER_VERTEX = 3;
	private final float[] vertexData;
	private int offset = 0;
	
	private ObjectBuilder(int sizeInVertices){
		vertexData = new float[sizeInVertices * FLOATS_PER_VERTEX];
	}
	
	private static int sizeOfCircleInVertices(int numPoints){
		return 1+numPoints+1;//中心+首尾点重复
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
	    
	    return builder.build;
	}
	
	private void appendCircle(Circle circle , int numPoints){
		
	}
	

	
}
