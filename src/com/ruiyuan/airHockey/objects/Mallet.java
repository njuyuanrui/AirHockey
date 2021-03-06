package com.ruiyuan.airHockey.objects;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;

import java.util.List;


import com.ruiyuan.airHockey.Constants;
import com.ruiyuan.airHockey.data.VertexArray;
import com.ruiyuan.airHockey.objects.ObjectBuilder.DrawCommand;
import com.ruiyuan.airHockey.objects.ObjectBuilder.GenerateData;
import com.ruiyuan.airHockey.programs.ColorShaderProgram;
import com.ruiyuan.airHockey.util.Geometry.Cylinder;
import com.ruiyuan.airHockey.util.Geometry.Point;

public class Mallet {
	private static final int POSITION_COMPONENT_COUNT = 3;
	
	public final float radius;
	public final float height;
	
	private final VertexArray vertexArray;
	private final List<DrawCommand> drawList;
	
	
	public Mallet(float radius,float height,int numPointsAroundMallet){
		GenerateData generateData = ObjectBuilder.createMallet(new Point(0f, 0f,0f), radius, height, numPointsAroundMallet);
		this.radius = radius;
		this.height = height;
		
		vertexArray = new VertexArray(generateData.vertexData);
		drawList = generateData.drawList;
		
	}

	public void bindData(ColorShaderProgram colorPorgram){
		vertexArray.setVertexAttribPoint(0, colorPorgram.getPositionAttributeLocation(),POSITION_COMPONENT_COUNT , 0);
		
	}
	public void draw(){
		for(DrawCommand drawCommand : drawList){
			drawCommand.draw();
		}
	}
	
}
