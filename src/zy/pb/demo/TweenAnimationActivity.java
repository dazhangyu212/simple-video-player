package zy.pb.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class TweenAnimationActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_tween);
		ImageView ivbackground = (ImageView) findViewById(R.id.iv_background);
//		setAlphaAnimation(ivbackground);
//		setTranslateAnimation(ivbackground);
//		setScaleAnimation(ivbackground);
		setRotateAnimation(ivbackground);
	}
	//设置渐变透明度
	@SuppressWarnings("unused")
	private void setAlphaAnimation(View v) {
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(3000);
		v.setAnimation(aa);
		v.startAnimation(aa);
		
	}
	//画面转移位置移动动画
	@SuppressWarnings("unused")
	private void setTranslateAnimation(View v) {
		// TODO Auto-generated method stub
		TranslateAnimation ta = new TranslateAnimation(0, 320, 0, 0);
		ta.setDuration(3000);
		v.setAnimation(ta);
		v.startAnimation(ta);
	}
	//渐变尺寸伸缩动画
	void setScaleAnimation(View v){
		//代表图像从中间出现逐渐放大,从无到有从小到大
		ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		sa.setDuration(3000);
		v.setAnimation(sa);
		v.startAnimation(sa);
	}
	//旋转画面
	void setRotateAnimation(View v){
		RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		ra.setDuration(3000);
		v.setAnimation(ra);
		v.startAnimation(ra);
	}
	
}
