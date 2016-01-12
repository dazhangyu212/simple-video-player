package zy.pb.demo.renderer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import zy.pb.demo.R;
import zy.pb.demo.utils.Utility;
import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.GLUtils;

public class OpenGL3DRenderer implements Renderer {
	private int[] texture;
	public int playNum = 0;
	public Boolean light = true;//开关灯按钮
	FloatBuffer mCubeVertexBuffer;
	FloatBuffer mcubeTexBuffer;
	Context context;
	private FloatBuffer lightAmbienet = FloatBuffer.wrap(new float[]{1f,1f,1f,1f});
	//定义漫射光1
	private FloatBuffer lightDiffuse = FloatBuffer.wrap(new float[]{1.0f,1.0f,1.0f,1.0f});
	//定义光源位置2
	private FloatBuffer lightPosition = FloatBuffer.wrap(new float[]{3.0f,0.0f,2.0f,1.0f});
	public OpenGL3DRenderer(Context context) {
		this.context = context;
	}
//	int[] res = {R.raw.one_1,R.raw.two_1,R.raw.three_1,R.raw.four_1,R.raw.five_1,R.raw.six_1};
	int[] res = {R.raw.opengl3d,R.raw.opengl3d,R.raw.opengl3d,R.raw.opengl3d,R.raw.opengl3d,R.raw.opengl3d,};
//	int[] res = {R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.four,R.drawable.five,R.drawable.six};
	float one = 0.5f;
	//立方体顶点坐标
	private float box[] = {
            // FRONT 每一面的正方形都等于两个三角形
            -one, -one,  one,
             one, -one,  one,
             one,  one,  one,
             -one,  one,  one,
            // BACK
            -one, -one, -one,
            -one,  one, -one,
             one, one, -one,
             one,  -one, -one,
            // TOP
             -one,  one,  -one,
              -one,  one,  one,
              one,  one, one,
              one,  one, -one,
            // BOTTOM
             -one, -one,  -one,
             one, -one, -one,
              one, -one,  one,
              -one, -one, one,
            // LEFT
              -one, -one, -one,
              -one,  -one, one,
              -one, one,  one,
              -one,  one,  -one,
            // RIGHT
              one, -one,  -one,
              one,  one,  -one,
              one, one, one,
              one,  -one, one,
    };
	private float rotx;
	private float roty;
	private float rotz;
	float two = 1.0f; 
	private float[] texCoords =	new float[]{
            // FRONT
            two,two,two,0,0,0,0,two,
            // BACK
             0,0,0,two,two,two,two,0,
            // TOP
             two,two,two,0,0,0,0,two,
            // BOTTOM
             0,two,two,two,two,0,0,0,
            // LEFT
             0,0,0,two,two,two,two,0,
            // RIGHT
            two,0,0,0,0,two,two,two,
			};
	//纹理号
	ByteBuffer indices1 = ByteBuffer.wrap(new byte[]{
			0,1,3,2,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			
	});
	ByteBuffer indices2 = ByteBuffer.wrap(new byte[]{
			0,0,0,0,
			4,5,7,6,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
	});
	ByteBuffer indices3 = ByteBuffer.wrap(new byte[]{
			0,0,0,0,
			0,0,0,0,
			8,9,11,10,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
	});
	ByteBuffer indices4 = ByteBuffer.wrap(new byte[]{
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			12,13,15,14,
			0,0,0,0,
			0,0,0,0,
	});
	ByteBuffer indices5 = ByteBuffer.wrap(new byte[]{
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			16,17,19,18,
			0,0,0,0,
	});
	ByteBuffer indices6 = ByteBuffer.wrap(new byte[]{
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			20,21,23,22,
	});
	private ByteBuffer[] indices = {indices1,indices2,indices3,indices4,indices5,indices6};
	@Override
	public void onDrawFrame(GL10 gl) {
		
		// 清除屏幕
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BUFFER_BIT);
		//重置模型矩阵
		gl.glLoadIdentity();
		//向屏幕移入5个单位
		gl.glTranslatef(0.0f, 0.0f, -5.0f);
		//设置旋转轴,以X轴为中心
		gl.glRotatef(rotx, 1, 0, 0);
		////设置旋转轴,以Y轴为中心
		gl.glRotatef(roty, 0, 1, 0);
		//设置旋转轴,以z轴为中心
		gl.glRotatef(rotz, 0, 0, 1);
		//必须启动GL_LIGHTING光,否则什么都看不见
		gl.glEnable(GL10.GL_LIGHTING);
		//开启顶点坐标功能
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		//设置顶点坐标
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mCubeVertexBuffer);
		//设置纹理坐标
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mcubeTexBuffer);
		
		//保存matrix当前状态
		gl.glPushMatrix();
		//绑定纹理坐标
		for (int i = 0; i < indices.length; i++) {
			gl.glBindTexture(GL10.GL_TEXTURE_2D,texture[i]);
			gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 24, GL10.GL_UNSIGNED_BYTE, indices[i]);
		}
//		for (int i = 0; i < 21; i++) {
//			Log.i("zhy", ""+i);
//			//从第0个点开始绘制绘制4个
//			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, i, 4);
//			i += 3;
//		}
//			//绘制第二个立方体面
//			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);
//			//绘制第三个立方体面
//			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8, 4);
//			//绘制第四个立方体面
//			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);
//			//绘制第五个立方体面
//			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16, 4); 
//			//绘制第六个立方体面
//			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20, 4);
		
		gl.glPopMatrix();
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//关闭纹理
		//旋转
		rotx += 1f;
		roty += 1.2f;
		rotz += 0.6f;
		
		//光源6
		if (!light) {
			gl.glDisable(GL10.GL_LIGHT1);
		}else{
			gl.glEnable(GL10.GL_LIGHT1);
		}
		playNum++;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		//设置场景大小
		gl.glViewport(0, 0, width, height);//前两个为位置,后两个为大小
		//设置投影矩阵
		gl.glMatrixMode(GL10.GL_PROJECTION);
		//重置模型矩阵q
		gl.glLoadIdentity();
		//设置窗口比例和透视图
		GLU.gluPerspective(gl, 45.0f, (float)width/height, 0.1f, 100.0f);//不一样207行
		//设置模型观察矩阵
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		//重置模型矩阵
		gl.glLoadIdentity();
		//设置环境光3
		gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, lightAmbienet);
		//设置漫射光
		gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, lightDiffuse);
		//设置光源位置4
		gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, lightPosition);
		//打开一号光源5
		gl.glEnable(GL10.GL_LIGHT1);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig arg1) {
		//转换为native类型的浮点缓存
		mCubeVertexBuffer = Utility.createFloatBuffer(box);
		//转换为native类型的浮点缓存
		mcubeTexBuffer = Utility.createFloatBuffer(texCoords);
		//设置背景颜色
		gl.glClearColorx(0,0 ,8, 1);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glShadeModel(GL10.GL_SMOOTH);
		//开启深度测试
		gl.glEnable(GL10.GL_DEPTH_TEST);
		//设置深度缓存
		gl.glClearDepthf(1.0f);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		//对透视修正
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		//开启纹理功能
		gl.glEnable(GL10.GL_TEXTURE_2D);
		//加载和处理纹理图像
		loadBitmapTex(gl,res);
	}
	
	private void loadBitmapTex(GL10 gl,int[] res){
		IntBuffer intBuffer = IntBuffer.allocate(6);
		gl.glGenTextures(6, intBuffer);
		texture = intBuffer.array();
		Bitmap[] bm = new Bitmap[6];
			for (int i = 0; i < res.length; i++) {
				
				bm[i] = Utility.getTexTureFromBitmapResource(context, res[i]);
			}
			
//		int[] mTextureID = new int[1];
//		这里赋值
			//1
//			gl.glGenTextures(1, texture,0);//设置偏移量
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bm[0], 0);
			//设置纹理过滤器
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
			//2
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[1]);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bm[1], 0);
			//设置纹理过滤器
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
			//3
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[2]);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bm[2], 0);
			//设置纹理过滤器
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
			//4
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[3]);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bm[3], 0);
			//设置纹理过滤器
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
			//5
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[4]);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bm[4], 0);
			//设置纹理过滤器
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
			//6
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[5]);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bm[5], 0);
			//设置纹理过滤器
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
			//回收资源
			for (int i = 0; i < bm.length; i++) {
				bm[i].recycle();
			}
		}
}
