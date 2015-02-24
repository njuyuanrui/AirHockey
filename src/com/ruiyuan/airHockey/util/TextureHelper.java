package com.ruiyuan.airHockey.util;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameterf;
import static android.opengl.GLUtils.texImage2D;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class TextureHelper {
	private static final String TAG = "TextureHelper";
	public static int loadTexture(Context context , int resourceID){
		final int[] textureObjectIDs = new int[1];
		glGenTextures(1, textureObjectIDs, 0);
		
		if(textureObjectIDs[0]==0){
			if(LoggerConfig.ON){
				Log.w(TAG, "Could not generate a new OpenGL texture object.");
			}
			return 0;
		}
		
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;
		
		final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceID, options);
		if(bitmap==null){
			if(LoggerConfig.ON){
				Log.w(TAG,"ResourceID "+resourceID+"could not be decode.");
			}
			glDeleteTextures(1, textureObjectIDs, 0);
			return 0;
		}
		glBindTexture(GL_TEXTURE_2D, textureObjectIDs[0]);
		glTexParameterf(GL_TEXTURE_2D , GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameterf(GL_TEXTURE_2D , GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
		
		glGenerateMipmap(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, 0);
		
		return textureObjectIDs[0];
		
		
	}
	
	
}
