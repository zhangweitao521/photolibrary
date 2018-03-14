package com.zwt.photoselect.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;


import com.zwt.photoselect.R;
import com.zwt.photoselect.base.PhotoLoadPicture;
import com.zwt.photoselect.model.ImageModel;
import com.zwt.photoselect.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangweitao on 2018/3/8.
 * 图片适配器
 */

public class PhotoImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_PICTURE = 1;

    private ArrayList<ImageModel> mAllImage;
    private Activity mContext;
    private Resources mResources;
    private LayoutInflater mLayoutInflater;
    private OnPhotoClickListener mOnPhotoClickListener;

    private boolean mIsShowCamera;
    private boolean mIsShowMulti;
    private int mMaxNumber;
    private ArrayList<String> mSelectPaths;
    private PhotoLoadPicture mPhotoLoadPicture;

    public PhotoImageAdapter(Activity context, int errorResId) {
        this.mAllImage = new ArrayList<>();
        this.mSelectPaths = new ArrayList<>();
        this.mContext = context;
        this.mResources = context.getResources();
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mPhotoLoadPicture = new PhotoLoadPicture(context, errorResId);
    }

    public interface OnPhotoClickListener {
        /**
         * 点击照相机
         */
        void onClickCamera();

        /**
         * 单选照片
         *
         * @param path
         */
        void onSinglePicture(String path);

        /**
         * 选中照片的数量
         *
         * @param number
         */
        void onSelectNumber(int number);

    }

    public void setData(boolean isShowCamera, boolean isShowMulti, int number, List<String> paths) {
        this.mIsShowCamera = isShowCamera;
        this.mIsShowMulti = isShowMulti;
        this.mMaxNumber = number;
        this.mSelectPaths.clear();
        if (paths != null) {
            this.mSelectPaths.addAll(paths);
        }
    }

    public void setAllImageData(ArrayList<ImageModel> allImage) {
        this.mAllImage.clear();
        if (allImage == null) return;
        this.mAllImage.addAll(allImage);
        setDefaultSelected();
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelectImage() {
        return this.mSelectPaths;
    }

    public void setOnPhotoClickListener(OnPhotoClickListener listener) {
        this.mOnPhotoClickListener = listener;
    }

    private void setDefaultSelected() {
        for (String path : mSelectPaths) {
            getImageByPath(path);
        }
    }

    private void getImageByPath(String path) {
        if (mAllImage != null && mAllImage.size() > 0) {
            for (ImageModel image : mAllImage) {
                if (image.getPath().equalsIgnoreCase(path)) {
                    int index = mAllImage.indexOf(image);
                    image.setChecked(true);
                    mAllImage.set(index, image);
                    return;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mIsShowCamera ? mAllImage.size() + 1 : mAllImage.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mIsShowCamera && position == 0) return TYPE_CAMERA;
        return TYPE_PICTURE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CAMERA) {
            View view = mLayoutInflater.inflate(R.layout.photo_recycle_item_camera, parent, false);
            return new CameraViewHolder(view);
        } else {
            View view = mLayoutInflater.inflate(R.layout.photo_recycle_item_image, parent, false);
            return new ImageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_PICTURE) {
            position = mIsShowCamera ? position - 1 : position;
            ((ImageViewHolder) holder).bindData(mAllImage.get(position));
        }
    }

    class CameraViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CameraViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mSelectPaths.size() < mMaxNumber) {
                if (mOnPhotoClickListener != null) {
                    mOnPhotoClickListener.onClickCamera();
                }
            } else {
                UIUtils.showToast(mContext, R.string.photo_amount_limit);
            }
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView picture;
        private CheckBox check;
        private View mView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            this.mView = itemView;
            picture = itemView.findViewById(R.id.photo_item_iv);
            check = itemView.findViewById(R.id.photo_item_cb);
        }

        public void bindData(final ImageModel model) {
            mPhotoLoadPicture.loadPictureCenterCrop(model.getPath(), picture);
            check.setVisibility(mIsShowMulti ? View.VISIBLE : View.INVISIBLE);
            setImageCheck(model.isChecked());
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mIsShowMulti) {
                        boolean isCheck = model.isChecked();
                        if (isCheck || mSelectPaths.size() < mMaxNumber) {
                            model.setChecked(!isCheck);
                            setImageCheck(model.isChecked());
                            setImageCheckNumber(model);
                            if (mOnPhotoClickListener != null) {
                                mOnPhotoClickListener.onSelectNumber(mSelectPaths.size());
                            }
                        } else {
                            UIUtils.showToast(mContext, R.string.photo_amount_limit);
                        }
                    } else {
                        if (mOnPhotoClickListener != null) {
                            mOnPhotoClickListener.onSinglePicture(model.getPath());
                        }
                    }
                }
            });
        }

        private void setImageCheck(boolean isChecked) {
            picture.setColorFilter(mResources.getColor(
                    isChecked ? R.color.photo_image_pressed_overlay : R.color.photo_image_normal_overlay), PorterDuff.Mode.SRC_ATOP);
            check.setChecked(isChecked);
        }

        private void setImageCheckNumber(ImageModel model) {
            if (model.isChecked()) {
                mSelectPaths.add(model.getPath());
            } else {
                for (int i = 0; i < mSelectPaths.size(); i++) {
                    if (mSelectPaths.contains(model.getPath())) {
                        mSelectPaths.remove(model.getPath());
                    }
                }
            }
        }
    }
}
