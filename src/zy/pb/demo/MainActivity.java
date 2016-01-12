package zy.pb.demo;


import zy.pb.demo.utils.Utility;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * 自定义播放器
 * 
 * @author Administrator
 *
 */
public class MainActivity extends Activity implements SurfaceHolder.Callback {
	private static final String TAG = "MainActivity";
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private MediaPlayer mediaPlayer;
	private String path = "";
	private ImageButton playbtn, stopbtn, listbtn;
	private static boolean isplaying = true;
	private int position = 0;
	private LinearLayout bottombar;
	private SeekBar seekBar;
	// 播放按钮点击事件
	private OnClickListener playbtnlistener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (mediaPlayer.isPlaying()) {
				position = mediaPlayer.getCurrentPosition();
				mediaPlayer.pause();
				playbtn.setImageDrawable(getResources().getDrawable(
						R.raw.btn_mp_play));
			} else {
				playVideo();
				playbtn.setImageDrawable(getResources().getDrawable(
						R.raw.btn_mp_pause));
			}
		}
	};
	// 停止按钮事件
	private OnClickListener stopbtnlistener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (mediaPlayer.isPlaying()) {
//				isplaying = false;
				mediaPlayer.stop();
				
				position = 0;
				playbtn.setImageDrawable(getResources().getDrawable(
						R.raw.btn_mp_play));
				// Intent intent = new Intent(MainActivity.this,
				// VideoListActivity.class);
				// startActivity(intent);
				// finish();
			} else {
				return;
			}
		}
	};
	// 播放列表点击事件
	private OnClickListener listbtnlistener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			isplaying = false;
			Intent intent = new Intent(MainActivity.this,
					VideoListActivity.class);
			startActivity(intent);
			
			finish();
		}
	};

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 不显示Title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// 设置横屏(或竖屏|SCREEN_ORIENTATION_PORTRAIT)启动
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.layout_main);
		mediaPlayer = new MediaPlayer();
//		mediaPlayer.setVolume(arg0, arg1);
		surfaceView = (android.view.SurfaceView) this
				.findViewById(R.id.sv_main_screen);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.setKeepScreenOn(true);
		surfaceHolder.addCallback(this);
		// 非原始数据,不适用缓冲区取数据
		surfaceHolder.setType(surfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		path = getIntent().getStringExtra("path");

		// 获取按钮并添加点击事件
		playbtn = (ImageButton) this.findViewById(R.id.ib_main_play);
		stopbtn = (ImageButton) findViewById(R.id.ib_main_stop);
		listbtn = (ImageButton) findViewById(R.id.ib_main_openlist);
		bottombar = (LinearLayout) findViewById(R.id.bottombar);
		seekBar = (SeekBar) findViewById(R.id.seekBar);

		playbtn.setOnClickListener(playbtnlistener);
		stopbtn.setOnClickListener(stopbtnlistener);
		listbtn.setOnClickListener(listbtnlistener);
		surfaceView.setOnTouchListener(onTouchListener);
		thread.start();
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int process = seekBar.getProgress();
				if (mediaPlayer != null && mediaPlayer.isPlaying()) {
					mediaPlayer.seekTo(process);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub

			}
		});

	}

	// 设置触碰事件
	private OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// 显示顶部和底部的工具栏
			bottombar.setVisibility(View.VISIBLE);

			Utility.tick = 0;// 计时器归零
			Utility.target = 5; // 定时5秒
			startTimer();// 启动计时器

			return false;
		}

		/**
		 * 启动计时器以隐藏菜单栏
		 */
		private void startTimer() {
			new Thread() {
				public void run() {
					while (Utility.tick < Utility.target) {
						try {
							Thread.sleep(1000);// 间歇1秒
						} catch (InterruptedException e) {
							//
							e.printStackTrace();
						}
						Utility.tick++;
						Log.i("tic", "tick=" + Utility.tick + ",target="
								+ Utility.target);
					}
					handler.sendEmptyMessage(1);

				};
			}.start();
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				bottombar.setVisibility(View.GONE);
			}else if (msg.what == 2) {
				position = mediaPlayer.getCurrentPosition();//需要修改,11-25下午修改测试
				seekBar.setProgress(position);
			}else if (msg.what == 3) {
				Log.i("TAG", "Sleeping");
			}
		};
	};

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// 横竖屏切换_1
		if (mediaPlayer != null) {
			outState.putInt("position", mediaPlayer.getCurrentPosition());
		}
		super.onSaveInstanceState(outState);
	}

	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// 横竖屏切换_2
		position = savedInstanceState.getInt("position");
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onPause() {
		playbtn.setImageDrawable(getResources().getDrawable(R.raw.btn_mp_play));
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			position = mediaPlayer.getCurrentPosition();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
		}
		super.onDestroy();

	}

	/**
	 * 播放视频
	 */
	private void playVideo() {
		try {

			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			mediaPlayer.setDisplay(surfaceHolder);
			mediaPlayer.prepareAsync();
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer arg0) {
					Log.i("TAG", "##9999999999999999999999#");
					mediaPlayer.seekTo(position);
					mediaPlayer.start();
					seekBar.setProgress(position);
					int max = mediaPlayer.getDuration();
					seekBar.setMax(max);// 设置最大进度
					
				}
			});
			mediaPlayer.setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
					playbtn.setImageDrawable(getResources().getDrawable(
							R.raw.btn_mp_play));
					position = 0;
					return false;
				}
			});
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}

	}

	// 更新进度计时器
	Thread thread = new Thread() {

		public void run() {
			while (isplaying) {
				try {
					if (mediaPlayer!=null && mediaPlayer.isPlaying()) {
						handler.sendEmptyMessage(2);
						Log.i("TAG", "视频播放中....");
					}else{
						handler.sendEmptyMessage(3);
						Log.i("TAG", "视频暂停....");

					}
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
	};

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// position = 0;
		playVideo();
		bottombar.setVisibility(View.GONE);
		playbtn.setImageDrawable(getResources().getDrawable(R.raw.btn_mp_pause));

	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * 按键处理
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//音频管理器
		AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(MainActivity.this)
					.setTitle("小酷提醒您")
					.setMessage("您确定要退出吗")
					.setPositiveButton("退出",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									isplaying = false;
//									mediaPlayer.stop();
									// thread.interrupt();//需要修改
									position = 0;// 播放进度重置
									finish();
								}
							})
					.setNegativeButton("返回",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									arg0.dismiss();
								}
							}).show();
		}else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			//音量加大
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume-1, 1);
			
		}else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			//音量减小
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume+1, 1);
		}
		return true;
	}
}
