package zy.pb.demo;

import zy.pb.demo.renderer.OpenGL3DRenderer;
import zy.pb.demo.utils.SDCardFileUtil;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

public class OpenGLActivity extends Activity {
	private OpenGL3DRenderer render;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		GLSurfaceView cubeGL = new GLSurfaceView(this);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		render = new OpenGL3DRenderer(this);
		cubeGL.setZOrderOnTop(true);
		cubeGL.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		cubeGL.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		cubeGL.setBackgroundDrawable(getResources().getDrawable(R.drawable.snowflower));
		cubeGL.setRenderer(render);
		setContentView(cubeGL);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE	, R.layout.inc_app_title);
		Toast.makeText(OpenGLActivity.this, "zhe", Toast.LENGTH_LONG).show();
		LoadingAllVideos();
	}
	
	@SuppressWarnings("static-access")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//3D鍥惧儚鐨勫紑鍏崇伅鏁堟灉,鏆傛椂鍙栨秷
		if (event.getAction() == event.ACTION_DOWN) {
			render.light = !render.light;
		}
		return super.onTouchEvent(event);
	}
	//鍔犺浇瑙嗛淇℃伅鐨勮繃绋�,鐢ㄧ嚎绋�
	private void LoadingAllVideos() {
		new Thread(){
			@Override
			public void run() {
				try {
					SDCardFileUtil.loadingVideos();
					Thread.sleep(3000);//灏嗘潵瑕佸幓鎺�
					Message msg = new Message();
					msg.what = 2;// 姝ｅ父
					handler.sendMessage(msg);// 鍙戦�佹秷鎭�
				} catch (Exception e) {
					
				}
			}
		}.start();
			
	}
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == 2){// 璺宠浆鐣岄潰
				Toast.makeText(OpenGLActivity.this, "鍔犺浇瀹屾垚", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(OpenGLActivity.this, VideoListActivity.class);
				OpenGLActivity.this.startActivity(intent);
				OpenGLActivity.this.finish();
			}
		};
	};
}
