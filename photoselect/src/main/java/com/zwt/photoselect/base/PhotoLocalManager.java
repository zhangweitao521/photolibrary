package com.zwt.photoselect.base;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.zwt.photoselect.R;
import com.zwt.photoselect.model.FolderModel;
import com.zwt.photoselect.model.ImageModel;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * Created by zhangweitao on 2018/3/8.
 * 获取本地图片和文件夹
 */

public class PhotoLocalManager {

    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;

    private final static String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media._ID};

    private final static String[] VIDEO_PROJECTION = {
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DURATION};

    private int mType = TYPE_IMAGE;
    private FragmentActivity mFragmentActivity;
    private OnLocalMediaListener mOnLocalMediaListener;
    private HashSet<String> mDirPaths = new HashSet<>();

    public PhotoLocalManager(FragmentActivity activity, int type) {
        this.mFragmentActivity = activity;
        this.mType = type;
    }

    public interface OnLocalMediaListener {
        /**
         * 加载完成所有图片和文件夹
         *
         * @param folders
         */
        void onLoadComplete(List<FolderModel> folders);
    }

    public void setOnLocalMediaListener(OnLocalMediaListener listener) {
        this.mOnLocalMediaListener = listener;
    }

    public void loadAllImage() {
        mFragmentActivity.getSupportLoaderManager().initLoader(mType, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                        CursorLoader cursorLoader = null;
                        if (id == TYPE_IMAGE) {
                            cursorLoader = new CursorLoader(mFragmentActivity,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    IMAGE_PROJECTION,
                                    MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                                    new String[]{"image/jpeg", "image/png"},
                                    IMAGE_PROJECTION[2] + " DESC");
                        } else if (id == TYPE_VIDEO) {
                            cursorLoader = new CursorLoader(mFragmentActivity,
                                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                    VIDEO_PROJECTION,
                                    null,
                                    null,
                                    VIDEO_PROJECTION[2] + " DESC");
                        }
                        return cursorLoader;
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                        ArrayList<FolderModel> imageFolders = new ArrayList<>();
                        FolderModel allImageFolder = new FolderModel();
                        ArrayList<ImageModel> allImages = new ArrayList<>();

                        while (data != null && data.moveToNext()) {
                            // 获取图片的路径
                            String path = data.getString(data
                                    .getColumnIndex(MediaStore.Images.Media.DATA));
                            File file = new File(path);
                            if (!file.exists())
                                continue;
                            // 获取该图片的目录路径名
                            File parentFile = file.getParentFile();
                            if (parentFile == null || !parentFile.exists())
                                continue;

                            String dirPath = parentFile.getAbsolutePath();
                            // 利用一个HashSet防止多次扫描同一个文件夹
                            if (mDirPaths.contains(dirPath)) {
                                continue;
                            } else {
                                mDirPaths.add(dirPath);
                            }

                            if (parentFile.list() == null)
                                continue;
                            FolderModel localMediaFolder = getImageFolder(path, imageFolders);

                            File[] files = parentFile.listFiles(new FilenameFilter() {
                                @Override
                                public boolean accept(File dir, String filename) {
                                    if (filename.endsWith(".jpg")
                                            || filename.endsWith(".png")
                                            || filename.endsWith(".jpeg"))
                                        return true;
                                    return false;
                                }
                            });
                            ArrayList<ImageModel> images = new ArrayList<>();
                            for (int i = 0; i < files.length; i++) {
                                File f = files[i];
                                ImageModel localMedia = new ImageModel(f.getAbsolutePath());
                                allImages.add(localMedia);
                                images.add(localMedia);
                            }
                            if (images.size() > 0) {
                                localMediaFolder.setChildImages(images);
                                localMediaFolder.setImageNum(localMediaFolder.getChildImages().size());
                                imageFolders.add(localMediaFolder);
                            }
                        }

                        allImageFolder.setChildImages(allImages);
                        allImageFolder.setImageNum(allImageFolder.getChildImages().size());
                        allImageFolder.setFirstImagePath(allImages.get(0).getPath());
                        allImageFolder.setName(mFragmentActivity.getString(R.string.photo_all_image));
                        imageFolders.add(allImageFolder);
                        sortFolder(imageFolders);
                        mOnLocalMediaListener.onLoadComplete(imageFolders);
                        if (data != null) data.close();
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> loader) {

                    }
                });
    }

    private void sortFolder(List<FolderModel> imageFolders) {
        // 文件夹按图片数量排序
        Collections.sort(imageFolders, new Comparator<FolderModel>() {
            @Override
            public int compare(FolderModel lhs, FolderModel rhs) {
                if (lhs.getChildImages() == null || rhs.getChildImages() == null) {
                    return 0;
                }
                int lsize = lhs.getImageNum();
                int rsize = rhs.getImageNum();
                return lsize == rsize ? 0 : (lsize < rsize ? 1 : -1);
            }
        });
    }

    private FolderModel getImageFolder(String path, List<FolderModel> imageFolders) {
        File imageFile = new File(path);
        File folderFile = imageFile.getParentFile();

        for (FolderModel folder : imageFolders) {
            if (folder.getName().equals(folderFile.getName())) {
                return folder;
            }
        }
        FolderModel newFolder = new FolderModel();
        newFolder.setName(folderFile.getName());
        newFolder.setPath(folderFile.getAbsolutePath());
        newFolder.setFirstImagePath(path);
        return newFolder;
    }

}
