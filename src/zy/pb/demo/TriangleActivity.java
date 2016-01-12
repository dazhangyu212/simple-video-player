package zy.pb.demo;

import zy.pb.demo.renderer.TriangleRenderer;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class TriangleActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GLSurfaceView glSurfaceView = new GLSurfaceView(this);
		TriangleRenderer renderer = new TriangleRenderer();
		glSurfaceView.setRenderer(renderer);
		setContentView(glSurfaceView);
		
	}
}
