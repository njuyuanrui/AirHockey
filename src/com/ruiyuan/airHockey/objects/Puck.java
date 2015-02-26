package com.ruiyuan.airHockey.objects;

import java.util.List;



import com.ruiyuan.airHockey.data.VertexArray;
import com.ruiyuan.airHockey.objects.ObjectBuilder.DrawCommand;
import com.ruiyuan.airHockey.objects.ObjectBuilder.GenerateData;
import com.ruiyuan.airHockey.programs.ColorShaderProgram;
import com.ruiyuan.airHockey.util.Geometry.Cylinder;
import com.ruiyuan.airHockey.util.Geometry.Point;

public class Puck {
	
	private static final int POSITION_COMPONENT_COUNT=3;
	public final float radius,height;
	
	private final VertexArray vertexArray;
	private final List<DrawCommand> drawList;
	
	public Puck(float radius,float height,int numPointsAroundPuck){
		GenerateData generateData = ObjectBuilder.createPuck(new Cylinder(new Point(0f, 0f,0f), radius, height), numPointsAroundPuck);
		this.radius = radius;
		this.height = height;
		
		vertexArray = new VertexArray(generateData.vertexData);
		drawList = generateData.drawList;
	
	
	}
	
	public void bindData(ColorShaderProgram colorShaderProgram){
		vertexArray.setVertexAttribPoint(0, colorShaderProgram.getPositionAttributeLocation(),POSITION_COMPONENT_COUNT , 0);
		
	}
	
	public void draw(){
		for(DrawCommand drawCommand : drawList){
			drawCommand.draw();
		}
		
	}
}
