package zy.pb.demo;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
/**
 * 调用系统播放器
 * @author Administrator
 *
 */
public class SystemMediaPlayerActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//获取启动 该Activity的意图,并从中取出路径
		String path = getIntent().getStringExtra("path");
		Intent intent = new Intent();
		//将Activity设置为栈顶运动的Activity
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//设置要执行的动作时给用户显示数据
		intent.setAction(android.content.Intent.ACTION_VIEW);
		//设置要显示的数据及打开类型
		intent.setDataAndType(Uri.fromFile(new File(path)), "video/*");
		startActivity(intent);
	}
}
