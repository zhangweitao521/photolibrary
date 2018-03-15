package com.zwt.photoselect.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zwt.photoselect.R;
import com.zwt.photoselect.base.PhotoLoadPicture;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by zhangweitao on 2018/3/8.
 * 预览适配器
 */

public class PhotoPagerAdapter extends PagerAdapter {

    private LayoutInflater mLayoutInflater;
    private ArrayList<String> mPaths;
    private PhotoLoadPicture mPhotoLoadPicture;

    public PhotoPagerAdapter(Context context, int errorResId) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mPaths = new ArrayList<>();
        this.mPhotoLoadPicture = new PhotoLoadPicture(context, errorResId);
    }

    public void setPreviewData(ArrayList<String> paths) {
        if (paths == null) return;
        this.mPaths = paths;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.photo_viewpager_item_preview, container, false);
        PhotoView imageView = itemView.findViewById(R.id.photo_item_photoview);
        mPhotoLoadPicture.loadPictureCrossFade(mPaths.get(position), imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public int getCount() {
        return mPaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
