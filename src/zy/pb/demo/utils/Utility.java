package zy.pb.demo.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class Utility {
	public static int tick = 0; // 计时器
	public static int target = 0; // 计时器的终点
	
	public static FloatBuffer createFloatBuffer(float data[]){
		ByteBuffer vbb = ByteBuffer.allocateDirect(data.length*4);
		//因为一个浮点数要占四个字节
		vbb.order(ByteOrder.nativeOrder());
		FloatBuffer outBuffer = vbb.asFloatBuffer();
		//填充浮点数组
		outBuffer.put(data).position(0);
		return outBuffer;
		
	}
	public static IntBuffer createIntBuffer(int[] data){
		ByteBuffer vbb = ByteBuffer.allocateDirect(data.length*4);
		
		//因为一个浮点数要占四个字节
		vbb.order(ByteOrder.nativeOrder());
//		FloatBuffer outBuffer = vbb.asFloatBuffer();
		IntBuffer outBuffer = vbb.asIntBuffer();
		//填充int数组
		outBuffer.put(data).position(0);
		return outBuffer;
	}
	/**
	 * 转化位图
	 * @param context
	 * @param resourceId
	 * @return
	 */
	public static Bitmap getTexTureFromBitmapResource(Context context,int resourceId){
		Bitmap bitmap = null;
		Matrix yFlipMatrix = new Matrix();//让图片绕Y轴旋转
		yFlipMatrix.postScale(1, -1);//旋转180度
		try {
			bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
			return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),yFlipMatrix,false);
			
		} catch (Exception e) {
			return null;
		}finally{
			if (bitmap!=null) {
				bitmap.recycle();
			}
		}
		
	}
}
