package com.zwt.photoselect.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

import com.zwt.photoselect.R;
import com.zwt.photoselect.adapter.PhotoFolderAdapter;
import com.zwt.photoselect.model.FolderModel;
import com.zwt.photoselect.utils.ScreenUtils;
import com.zwt.photoselect.view.ListItemDivider;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by zhangweitao on 2018/3/8.
 * 文件夹弹窗
 */

public class PhotoFolderWindow extends PopupWindow {

    private Context mContext;
    private PhotoFolderAdapter mPhotoFolderAdapter;
    private View mView;
    private RecyclerView mRecyclerView;

    private boolean isDismiss = false;

    public PhotoFolderWindow(Context context,int errorResId) {
        this.mContext = context;
        this.mView = LayoutInflater.from(context).inflate(R.layout.photo_dialog_folder, null);
        this.setContentView(mView);
        this.setWidth(ScreenUtils.getScreenWidth(context));
        this.setAnimationStyle(R.style.PhotoWindowStyle);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        this.setBackgroundDrawable(new ColorDrawable(Color.argb(153, 0, 0, 0)));
        initView(errorResId);
        setPopupWindowTouchModal(this, false);
    }

    private void initView(int errorResId) {
        mPhotoFolderAdapter = new PhotoFolderAdapter(mContext,errorResId);
        mRecyclerView = mView.findViewById(R.id.photo_dialog_recycleview);
        mRecyclerView.addItemDecoration(new ListItemDivider(mContext));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mPhotoFolderAdapter);
    }

    public void bindFolder(List<FolderModel> folders) {
        mPhotoFolderAdapter.setFolderData(folders);
    }

    public void setOnItemClickListener(PhotoFolderAdapter.OnFolderClickListener listener) {
        mPhotoFolderAdapter.setOnFolderClickListener(listener);
    }

    public void showAsDropDown(View anchor, int height) {
        this.setHeight(ScreenUtils.getScreenHeight(mContext) - height);
        this.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.photo_up_in);
        mRecyclerView.startAnimation(animation);
    }

    @Override
    public void dismiss() {
        if (isDismiss) {
            return;
        }
        isDismiss = true;
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.photo_down_out);
        mRecyclerView.startAnimation(animation);
        dismiss();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isDismiss = false;
                PhotoFolderWindow.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private static void setPopupWindowTouchModal(PopupWindow popupWindow, boolean touchModal) {
        if (null == popupWindow) {
            return;
        }
        Method method;
        try {
            method = PopupWindow.class.getDeclaredMethod("setTouchModal", boolean.class);
            method.setAccessible(true);
            method.invoke(popupWindow, touchModal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
