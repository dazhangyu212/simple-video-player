package zy.pb.demo.adapter;

import java.util.ArrayList;

import zy.pb.demo.R;
import zy.pb.demo.model.VideoInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<VideoInfo> videos;
	private LayoutInflater inflater;
	/**
	 * 构造器
	 * @param context
	 * @param videos
	 */
	public VideoAdapter(Context context, ArrayList<VideoInfo> videos) {
		super();
		this.context = context;
		this.videos = videos;
		inflater = LayoutInflater.from(this.context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return videos.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return videos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			VideoInfo video = videos.get(arg0);
			//行布局模板
			arg1 = inflater.inflate(R.layout.layout_item_videoinfo, null);
			//找到控件
			ImageView thumbnail = (ImageView) arg1.findViewById(R.id.iv_video_thumbnail);
			TextView name = (TextView) arg1.findViewById(R.id.tv_video_name);
			TextView size = (TextView) arg1.findViewById(R.id.tv_video_size);
			//给控件赋值
			thumbnail.setImageBitmap(video.thumbnail);
			name.setText(video.name);
			size.setText(video.size);
		}else {
			return arg1;
		}
		
		return arg1;
	}

}
