package com.zwt.photoselect.base;

import android.content.Context;
import android.support.annotation.IdRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by zhangweitao on 2018/3/8.
 * 图片加载管理
 */

public class PhotoLoadPicture {

    private Context mContext;
    private @IdRes
    int errorResId;

    public PhotoLoadPicture(Context context, int resId) {
        this.mContext = context;
        this.errorResId = resId;
    }

    public void loadPictureCenterCrop(String path, ImageView imageView) {
        Glide.with(mContext)
                .load(path)
                .placeholder(errorResId)
                .error(errorResId)
                .centerCrop()
                .into(imageView);
    }

    public void loadPictureCrossFade(String path, ImageView imageView) {
        Glide.with(mContext)
                .load(path)
                .placeholder(errorResId)
                .error(errorResId)
                .crossFade()
                .into(imageView);
    }
}
