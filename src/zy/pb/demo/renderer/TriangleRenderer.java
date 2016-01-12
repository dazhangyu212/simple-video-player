package zy.pb.demo.renderer;


import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class TriangleRenderer implements Renderer{
	//设置顶点坐标
	private FloatBuffer triangleFloatBuffer = FloatBuffer.wrap(new float[]{
		-0.5f,0.0f,0.0f,
		0.0f,-0.5f,0.0f,//每三个为一组,共同作为定点坐标
		0.5f,0.0f,0.0f,
	});//最后一个数多一个逗号?
	//定义顶点其实颜色
	int one = 0X10000;
	private IntBuffer colorBuffer = IntBuffer.wrap(new int[]{
			one,0,0,one,
			0,one,0,one,
			0,0,one,one,
	});
	private float roty;
	private float rotx;
	private float rotz;
	/**
	 * 绘制每一帧的方法
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		Log.i("zy***", "onDrawFrame");
		//清楚屏幕和深度缓存
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BUFFER_BIT);
		//开启顶点数组坐标
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		//开启颜色渲染功能
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
//		gl.glColor4f(1.0f, 0.0f, 0.0f, 0.0f);单调着色
		//重置矩阵
		gl.glLoadIdentity();
		//指定顶点数组坐标
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, triangleFloatBuffer);
		//指定颜色坐标数组
		gl.glColorPointer(4, GL10.GL_FIXED, 0, colorBuffer);
		//设置旋转轴
		gl.glRotatef(rotz, 0, 0, 1);
		gl.glRotatef(rotx, 1, 0, 0);
		gl.glRotatef(roty, 0, 1, 0);
		//绘制三角形
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
		//关闭定点坐标
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		//关闭颜色渲染器
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		//设置不断变化的旋转变量
		roty +=1.5f;
		rotx +=0.5f;
		rotz += 1.0f;
	}
	/**
	 * 窗口尺寸发生变化时调用 ,至少执行一次
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		Log.i("zy***", "onSurfaceChanged");
		//设置场景大小
		gl.glViewport(0, 0, width, height);
		//宽高比
		float ratio = (float)width/height;
		//设置矩阵模型为投影矩阵
		gl.glMatrixMode(GL10.GL_PROJECTION);
		//重置矩阵模型
		gl.glLoadIdentity();
		//设置可视区域
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
	}
	/**
	 * 创建或重建窗口是调用
	 */
	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
		Log.i("zy***", "onSurfaceCreated");
		
	}

}
