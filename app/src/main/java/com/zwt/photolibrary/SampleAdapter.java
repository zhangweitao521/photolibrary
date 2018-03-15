package com.zwt.photolibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangweitao on 2018/3/15.
 */

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ImageViewHolder> {

    private LayoutInflater mLayoutInflater;
    private OnPhotoClickListener mOnPhotoClickListener;
    private ArrayList<String> mSelectPaths;
    private Context mContext;

    public SampleAdapter(Context context) {
        this.mContext = context;
        this.mSelectPaths = new ArrayList<>();
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public interface OnPhotoClickListener {

        void onItemClick(String path);

    }

    public void setData(List<String> paths) {
        this.mSelectPaths.clear();
        if (paths != null) {
            this.mSelectPaths.addAll(paths);
        }
        notifyDataSetChanged();
    }

    public void setOnPhotoClickListener(OnPhotoClickListener listener) {
        this.mOnPhotoClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mSelectPaths.size();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.sample_recycle_item_image, parent, false);
        return new SampleAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.bindData(mSelectPaths.get(position));
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView picture;
        private View mView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            this.mView = itemView;
            picture = itemView.findViewById(R.id.sample_item_iv);
        }

        public void bindData(final String path) {
            Glide.with(mContext)
                    .load(path)
                    .centerCrop()
                    .into(picture);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnPhotoClickListener != null) {
                        mOnPhotoClickListener.onItemClick(path);
                    }
                }
            });
        }
    }
}
