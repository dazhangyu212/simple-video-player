package zy.pb.demo.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore.Video.Thumbnails;
import zy.pb.demo.model.VideoInfo;

public class SDCardFileUtil {
	
	private static ArrayList<VideoInfo> videoList = new ArrayList<VideoInfo>();
	/**
	 * 创建文件过滤器
	 * @return
	 */
	public static FileFilter getFilter(){
		FileFilter filter = new FileFilter() {
			
			@Override
			public boolean accept(File f) {
				//
				return f.isDirectory() || f.getName().matches("^.*?\\.(3gp|mp4|mkv)$");
			}
		};
		
		return filter;
		
	}
	
	//获取SDCard的根目录
	public static File rootDir = Environment.getExternalStorageDirectory();
	public static ArrayList<VideoInfo> videos;
	/**
	 * 加载视频信息
	 */
	public static ArrayList<VideoInfo> loadingVideos() {
		FileFilter filter = getFilter();
		//获取集合并清空
		videos = SDCardFileUtil.getVideos(rootDir, filter);
		if (videos!=null || videos.size()>0) {
			videos.clear();
		}
		videos = SDCardFileUtil.getVideos(rootDir, filter);
		return videos;
	}
	
	/**
	 * 递归遍历SDCard获取所有视频信息
	 * @return
	 */
	public static ArrayList<VideoInfo> getVideos(File file,FileFilter filter){
		File[] files = file.listFiles(filter);
		for (File f: files) {
			if (f.isDirectory() && f.canRead()) {
				getVideos(f, filter);
			}else if (f.isFile()) {
				VideoInfo video = new VideoInfo();
				video.name = f.getName();//名称
				video.path = f.getAbsolutePath();//路径
				video.size = dealSize(f.length());//大小
				video.thumbnail = ThumbnailUtils.createVideoThumbnail(f.getAbsolutePath()
						, Thumbnails.MINI_KIND);//缩略图
				videoList.add(video);
			}
		}
		return videoList;
	}
	private static String dealSize(float length) {
		long kb = 1024;
		long mb = 1024*kb;
		long gb = 1024*mb;
		if (length<kb) {
			return String.format("%d B", (int)length);
		}else if (length<mb ) {
			return String.format("%.2f KB", length/kb);
		}else if (length<gb) {
			return String.format("%.2f MB", length/mb);
		}else {
			return String.format("%.2f GB", length/gb);
		}
		
	}
	/** 进度对话框*/
	public static ProgressDialog pd;
	/** 显示加载进度条*/
	public static void showLoading(Context context){
		pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMessage("列表更新中...");
		pd.setCancelable(false);// 禁止用户取消
		pd.show();
	}
	
	public static void hideLoading(){
		if (pd.isShowing()) {
			pd.cancel();// 消失
		}
	}
	public static void HttpGet(String urlStr){
		String result = "";
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStream in = conn.getInputStream();
			result = readData(in);
			conn.disconnect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**读取输入流*/
	public static String readData(InputStream inStream) throws IOException{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while((len = inStream.read(buffer)) != -1){
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		inStream.close();
		outStream.close();
		return new String(data,"UTF-8");
	}
}
