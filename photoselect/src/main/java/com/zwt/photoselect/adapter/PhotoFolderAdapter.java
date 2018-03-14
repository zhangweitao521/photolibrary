package com.zwt.photoselect.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zwt.photoselect.R;
import com.zwt.photoselect.base.PhotoLoadPicture;
import com.zwt.photoselect.model.FolderModel;
import com.zwt.photoselect.model.ImageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangweitao on 2018/3/8.
 * 文件夹适配器
 */

public class PhotoFolderAdapter extends RecyclerView.Adapter<PhotoFolderAdapter.FolderViewHolder> {

    private Context mContext;
    private OnFolderClickListener mOnFolderClickListener;
    private List<FolderModel> mFolders;
    private int checkedIndex = 0;
    private PhotoLoadPicture mPhotoLoadPicture;

    public interface OnFolderClickListener {
        /**
         * 点击文件夹
         *
         * @param folderName
         * @param images
         */
        void onClickFolder(String folderName, ArrayList<ImageModel> images);
    }

    public PhotoFolderAdapter(Context context, int errorResId) {
        this.mContext = context;
        this.mFolders = new ArrayList<>();
        this.mPhotoLoadPicture = new PhotoLoadPicture(context, errorResId);
    }

    public void setFolderData(List<FolderModel> folders) {
        this.mFolders = folders;
        notifyDataSetChanged();
    }

    public void setOnFolderClickListener(OnFolderClickListener listener) {
        this.mOnFolderClickListener = listener;
    }

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.photo_recycle_item_folder, parent, false);
        return new FolderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FolderViewHolder holder, int position) {
        holder.bindData(mFolders.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mFolders.size();
    }

    class FolderViewHolder extends RecyclerView.ViewHolder {

        private ImageView firstImage;
        private TextView titleText;
        private TextView numText;
        private ImageView radioImage;
        private View mView;

        public FolderViewHolder(View itemView) {
            super(itemView);
            this.mView = itemView;
            firstImage = itemView.findViewById(R.id.photo_item_folder_iv_first);
            titleText = itemView.findViewById(R.id.photo_item_folder_tv_title);
            numText = itemView.findViewById(R.id.photo_item_folder_tv_number);
            radioImage = itemView.findViewById(R.id.photo_item_folder_iv_radio);
        }

        @SuppressLint("StringFormatMatches")
        public void bindData(final FolderModel model, final int position) {
            mPhotoLoadPicture.loadPictureCenterCrop(model.getFirstImagePath(), firstImage);
            titleText.setText(model.getName());
            numText.setText(mContext.getString(R.string.photo_num_postfix, model.getImageNum()));
            radioImage.setVisibility(checkedIndex == position ? View.VISIBLE : View.GONE);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnFolderClickListener != null) {
                        checkedIndex = position;
                        notifyDataSetChanged();
                        mOnFolderClickListener.onClickFolder(model.getName(), model.getChildImages());
                    }
                }
            });
        }
    }
}
