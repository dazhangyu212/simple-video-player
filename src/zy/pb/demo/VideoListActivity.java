package zy.pb.demo;

import java.io.File;
import java.util.ArrayList;

import zy.pb.demo.adapter.VideoAdapter;
import zy.pb.demo.model.VideoInfo;
import zy.pb.demo.utils.SDCardFileUtil;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class VideoListActivity extends ListActivity {
	//记录壁纸的文件
	public static final String WALLPAPER_FILE="wallpaper_file";
	//缓存天气的文件
//	public static final String STORE_WEATHER="store_weather";
	private LinearLayout rootLayout;
	//定义菜单加载器
	private MenuInflater mi;
	private ArrayList<VideoInfo> videos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.layout_videolist);
		//设置标题,一下屏幕设置要放在setContentView方法
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE	, R.layout.inc_activitys_title);
        //实例化菜单加载器
        mi = new MenuInflater(this);
        rootLayout =   (LinearLayout) findViewById(R.id.rootLayout);
        FirstRuning();
        updateList();
	}
	
	private int mBackKeyPressedTimes = 0;
	/**
	 * 点击两次回退键,退出
	 */
	@Override
	public void onBackPressed() {
		if (mBackKeyPressedTimes == 0) {
			Toast.makeText(this, getResources().getString(R.string.exit_app), Toast.LENGTH_SHORT).show();
			mBackKeyPressedTimes =1;
			new Thread(){
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (Exception e) {
						// TODO: handle exception
					}finally{
						mBackKeyPressedTimes = 0;
					}
				};
			}.start();
			return ;
		}else {
			finish();
			System.exit(0);
		}
		super.onBackPressed();
	}
	
	/**
	 *  用SimpleAdapter绑定数据到ListView
	 */
//	private void simpleAdapterListView() {
//		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
//		for (VideoInfo video : videos) {
//			Map<String, Object> item = new HashMap<String, Object>();
//			item.put("name", video.name);
//			item.put("size", video.size);
////				item.put("path", video.path);
//			item.put("thumbnail", video.thumbnail);
//			data.add(item);
//		}
//		
//		SimpleAdapter adapter = new SimpleAdapter(
//				VideoListActivity.this, 
//				data, 
//				R.layout.layout_item_videoinfo, 
//				new String[]{"name","size","thumbnail"}, 
//				new int[]{R.id.tv_video_name,R.id.tv_video_size,R.id.iv_video_thumbnail});
//		getListView().setAdapter(adapter);
//	}
    //单击菜单方法
    public boolean onOptionsItemSelected(MenuItem menuItem) {
    	//得到SharedPreferences操作对象更改壁纸
    	SharedPreferences.Editor editor = getSharedPreferences(WALLPAPER_FILE, MODE_PRIVATE).edit();
    	//判断单击菜单的ID
    	switch(menuItem.getItemId()) {
    	case R.id.menu_homepage:
    		String url = "";
    		SDCardFileUtil.HttpGet(url);
    		break;
    	case R.id.menu_update:
    		SDCardFileUtil.showLoading(this);
        	// 线程Thread(子线程)
        	new Thread(){// 内部类
        		public void run() {
        			
        			LoadingAllVideos();
					Message msg = new Message();
					msg.what = 1;// 正常
					handler.sendMessage(msg);// 发送消息
        		};
        	}.start();
        	
    		break;
    	//更换壁纸
    	case R.id.wallpaper01:
    		rootLayout.setBackgroundResource(R.raw.bg_0);
        	editor.putInt("wellpaper", R.raw.bg_0);
        	editor.commit();
        	menuItem.setChecked(true);
    		break;
    	case R.id.wallpaper02:
    		rootLayout.setBackgroundResource(R.raw.bg_1);
        	editor.putInt("wellpaper", R.raw.bg_1);
        	editor.commit();
        	menuItem.setChecked(true);
    		break;
    	case R.id.wallpaper03:
    		rootLayout.setBackgroundResource(R.raw.bg_2);
        	editor.putInt("wellpaper", R.raw.bg_2);
        	editor.commit();
        	menuItem.setChecked(true);
    		break;
    	case R.id.wallpaper04:
    		rootLayout.setBackgroundResource(R.raw.bg_3);
        	editor.putInt("wellpaper", R.raw.bg_3);
        	editor.commit();
        	menuItem.setChecked(true);
    		break;
		default:
			break;
    	}
    	
    	return true;
    }
    //添加菜单项
    public boolean onCreateOptionsMenu(Menu menu) {
    	//加载菜单资源，实例化菜单
    	mi.inflate(R.menu.configure_menu, menu);
    	//得到保存的壁纸
    	SharedPreferences sp= getSharedPreferences(WALLPAPER_FILE, MODE_PRIVATE);
    	SubMenu subMenu = menu.getItem(2).getSubMenu();
    	MenuItem item = null;
    	switch(sp.getInt("wellpaper", R.raw.bg_1)) {
    	case R.raw.bg_0:
    		item = subMenu.getItem(0);
    		item.setChecked(true);
    		break;
    	case R.raw.bg_1:
    		item = subMenu.getItem(1);
    		item.setChecked(true);
    		break;
    	case R.raw.bg_2:
    		item = subMenu.getItem(2);
    		item.setChecked(true);
    		break;
    	case R.raw.bg_3:
    		item = subMenu.getItem(3);
    		item.setChecked(true);
    		break;
    	}
    	return true;
    }
	//加载视频信息的过程,用线程
	private void LoadingAllVideos() {
		new Thread(){
			@Override
			public void run() {
				try {
					SDCardFileUtil.loadingVideos();
					Message msg = new Message();
					msg.what = 2;// 正常
					handler.sendMessage(msg);// 发送消息
				} catch (Exception e) {
					
				}
			}
		}.start();
			
	}
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == 2){// 跳转界面
				Toast.makeText(VideoListActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
				
			}else if(msg.what == 1){
				updateList();
				SDCardFileUtil.hideLoading();
			}
		};
	};
	private void updateList() {
		// TODO Auto-generated method stub
		videos = SDCardFileUtil.videos;
		if (videos!= null && videos.size()>0) {
			VideoAdapter adapter = new VideoAdapter(this, videos);
			getListView().setAdapter(adapter);
			//给listView 设置行点击事件
			getListView().setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					VideoInfo video = videos.get(arg2);
					if (video.path == null || !"".equals(video.path)) {
						
//					Intent intent = new Intent(VideoListActivity.this, SystemMediaPlayerActivity.class);
						Intent intent = new Intent(VideoListActivity.this, MainActivity.class);
						intent.putExtra("path", video.path);
						startActivity(intent);
//						finish();
					}else {
						Toast.makeText(VideoListActivity.this, "视频路径不存在", Toast.LENGTH_LONG).show();
					}
				}
			});
			//设置listview 的长按时间
//			getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
//
//				@Override
//				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//						int arg2, long arg3) {
//					Toast.makeText(VideoListActivity.this, "删除文件", Toast.LENGTH_LONG).show();
//					return false;
//				}
//			});
		}else{
			Toast.makeText(VideoListActivity.this, "SDCard中无视频文件", 1).show();
		}
	}
    private void FirstRuning() {
        //通过检查程序中的缓存文件判断程序是否是第一次运行
        String dirPath= "/data/data/zy.pb.demo/shared_prefs/";
        File file= new File(dirPath);
        boolean isFirstRun = false;
        //如果文件不存在说明是第一次动行
        if(!file.exists()) {
        	//设置默认的壁纸
        	SharedPreferences.Editor editor = getSharedPreferences(WALLPAPER_FILE, MODE_PRIVATE).edit();
        	editor.putInt("wellpaper", R.raw.listbg);
        	editor.commit();
        	
        	isFirstRun = true;
        	Toast.makeText(this, "首次运行", 0).show();
        	
        } else {
        	//设置壁纸为文件中保存的
        	SharedPreferences sp= getSharedPreferences(WALLPAPER_FILE, MODE_PRIVATE);
        	rootLayout.setBackgroundResource(sp.getInt("wellpaper", R.raw.listbg));	
        }
        
	}
}
