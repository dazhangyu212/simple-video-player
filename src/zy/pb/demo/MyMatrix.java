package zy.pb.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

public class MyMatrix extends View {
	private Bitmap myBitmap;
	private Matrix myMatrix;
	public MyMatrix(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		myBitmap = ((BitmapDrawable)getResources().getDrawable(R.raw.welcome)).getBitmap();
		myMatrix = new Matrix();
		myMatrix.setScale(200f/ myBitmap.getWidth(),200f/myBitmap.getHeight());//设置图片的大小,宽和长
		myMatrix.postTranslate(100f, 100f);//图片平移
		myMatrix.preSkew(0.2f, 0.2f, 100f, 100f);//图片倾斜,以(100f,100f)为原点
		canvas.drawBitmap(myBitmap, myMatrix, null);
		
		super.onDraw(canvas);
	}

}
