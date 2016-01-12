package zy.pb.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class WelcomeActivity extends Activity implements AnimationListener {
    /** Called when the activity is first created. */
	private ImageView img ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Debug.startMethodTracing("onCreateMethod");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_welcome);
        img = (ImageView) findViewById(R.id.img);
        Animation animfadein = AnimationUtils.loadAnimation(this, R.anim.anim_fadein);
        animfadein.setAnimationListener(this);
        img.setAnimation(animfadein);
        
    }
	@Override
	public void onAnimationEnd(Animation arg0) {
		// TODO Auto-generated method stub
		 Animation animfadeout = AnimationUtils.loadAnimation(this, R.anim.anim_fadeout);
		animfadeout.setFillAfter(true);
		 img.setAnimation(animfadeout);
		 Intent intent = new Intent(WelcomeActivity.this, OpenGLActivity.class);
		 WelcomeActivity.this.startActivity(intent);
		 finish();
	}
	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub
		
	}
}