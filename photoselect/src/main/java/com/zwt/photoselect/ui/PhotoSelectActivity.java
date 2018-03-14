package com.zwt.photoselect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zwt.photoselect.R;
import com.zwt.photoselect.adapter.PhotoFolderAdapter;
import com.zwt.photoselect.adapter.PhotoImageAdapter;
import com.zwt.photoselect.base.PhotoBaseActivity;
import com.zwt.photoselect.base.PhotoCameraManager;
import com.zwt.photoselect.base.PhotoFolderWindow;
import com.zwt.photoselect.base.PhotoLocalManager;
import com.zwt.photoselect.constants.PhotoAction;
import com.zwt.photoselect.constants.PhotoDefault;
import com.zwt.photoselect.intent.PhotoPreviewIntent;
import com.zwt.photoselect.model.FolderModel;
import com.zwt.photoselect.model.ImageModel;
import com.zwt.photoselect.model.ResourceModel;
import com.zwt.photoselect.utils.ScreenUtils;
import com.zwt.photoselect.utils.UIUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangweitao on 2018/3/8.
 * 选择页面
 */

public class PhotoSelectActivity extends PhotoBaseActivity {
    private RecyclerView recyclerView;
    private TextView allText;
    private TextView previewText;
    private PhotoImageAdapter mPhotoImageAdapter;
    private PhotoLocalManager mPhotoLocalManager;
    private PhotoFolderWindow mPhotoFolderWindow;
    private PhotoCameraManager mPhotoCameraManager;

    private boolean mIsShowCamera;
    private boolean mIsShowMulti;
    private int mMaxNumber;
    private ArrayList<String> mSelectedPaths;
    private ResourceModel mResourceModel;

    @Override
    protected int provideContentViewId() {
        return R.layout.photo_activity_select;
    }

    @Override
    public void init() {
        super.init();
        mIsShowCamera = getIntent().getBooleanExtra(PhotoAction.ACTION_IS_SHOW_CAMERA, PhotoDefault.DEFAULT_SHOW_CAMERA);
        mIsShowMulti = getIntent().getBooleanExtra(PhotoAction.ACTION_IS_SHOW_MULTI, PhotoDefault.DEFAULT_SHOW_MULTI);
        mMaxNumber = getIntent().getIntExtra(PhotoAction.ACTION_MAX_SELECT_NUMBER, PhotoDefault.DEFAULT_MAX_NUMBER);
        mSelectedPaths = getIntent().getStringArrayListExtra(PhotoAction.ACTION_DEFAULT_SELECTED_PATHS);
        ResourceModel newResource = (ResourceModel) getIntent().getSerializableExtra(PhotoAction.ACTION_RESOURCE_DATA);
        mResourceModel = newResource == null ? new ResourceModel() : newResource;
    }

    @Override
    public void initView() {
        super.initView();
        recyclerView = findViewById(R.id.photo_select_recycleView);
        allText = findViewById(R.id.photo_select_tv_all);
        previewText = findViewById(R.id.photo_select_tv_preview);
    }

    @Override
    public void initData() {
        super.initData();
        getSupportActionBar().setTitle(UIUtils.getStringText(this, R.string.photo_title_select));
        mPhotoLocalManager = new PhotoLocalManager(this, PhotoLocalManager.TYPE_IMAGE);
        mPhotoImageAdapter = new PhotoImageAdapter(this, mResourceModel.getErrPicture());
        mPhotoImageAdapter.setData(mIsShowCamera, mIsShowMulti, mMaxNumber, mSelectedPaths);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, ScreenUtils.getNumColumns(this)));
        recyclerView.setAdapter(mPhotoImageAdapter);
        mPhotoFolderWindow = new PhotoFolderWindow(this, mResourceModel.getErrPicture());
        mPhotoCameraManager = new PhotoCameraManager(this, mResourceModel.getDirName());
    }

    @Override
    public void initListener() {
        super.initListener();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectResult(mSelectedPaths == null ? new ArrayList<String>() : mSelectedPaths);
            }
        });
        allText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPhotoFolderWindow.isShowing()) {
                    mPhotoFolderWindow.dismiss();
                } else {
                    mPhotoFolderWindow.showAsDropDown(mToolbar, mToolbar.getMeasuredHeight() * 2);
                }
            }
        });
        previewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPhotoFolderWindow.isShowing()) {
                    mPhotoFolderWindow.dismiss();
                } else {
                    ArrayList<String> list = mPhotoImageAdapter.getSelectImage();
                    if (list == null || list.size() < 1) return;
                    PhotoPreviewIntent intent = new PhotoPreviewIntent(PhotoSelectActivity.this);
                    intent.setPhotoPaths(list).setCurrentPath(list.get(0)).setResourceData(mResourceModel);
                    startActivityForResult(intent, PhotoAction.ACTION_REQUEST_PREVIEW);
                }
            }
        });
        mPhotoImageAdapter.setOnPhotoClickListener(new PhotoImageAdapter.OnPhotoClickListener() {
            @Override
            public void onClickCamera() {
                openCamera();
            }

            @Override
            public void onSinglePicture(String path) {
                ArrayList<String> paths = new ArrayList<>();
                paths.add(path);
                selectResult(paths);
            }

            @Override
            public void onSelectNumber(int number) {
                initMenuData(number);
            }
        });
        mPhotoLocalManager.setOnLocalMediaListener(new PhotoLocalManager.OnLocalMediaListener() {
            @Override
            public void onLoadComplete(List<FolderModel> folders) {
                mPhotoFolderWindow.bindFolder(folders);
                mPhotoImageAdapter.setAllImageData(folders.get(0).getChildImages());
            }
        });
        mPhotoLocalManager.loadAllImage();
        mPhotoFolderWindow.setOnItemClickListener(new PhotoFolderAdapter.OnFolderClickListener() {
            @Override
            public void onClickFolder(String folderName, ArrayList<ImageModel> images) {
                mPhotoFolderWindow.dismiss();
                mPhotoImageAdapter.setAllImageData(images);
                allText.setText(folderName);
            }
        });
    }

    @Override
    public void initMenuItem() {
        super.initMenuItem();
        mDoneMenu.setVisible(mIsShowMulti ? true : false);
        initMenuData(mSelectedPaths == null ? 0 : mSelectedPaths.size());
    }

    @Override
    public void onClickComplete() {
        super.onClickComplete();
        selectResult(mPhotoImageAdapter.getSelectImage());
    }

    private void initMenuData(int num) {
        if (num == 0) {
            mDoneMenu.setTitle(getString(R.string.photo_done));
            previewText.setVisibility(View.INVISIBLE);
        } else {
            mDoneMenu.setTitle(getString(R.string.photo_done_with_count, num, mMaxNumber));
            previewText.setVisibility(View.VISIBLE);
        }
    }

    private void selectResult(ArrayList<String> paths) {
        Intent data = new Intent();
        data.putStringArrayListExtra(PhotoAction.ACTION_RESULT_LIST, paths);
        setResult(RESULT_OK, data);
        finish();
    }

    private void openCamera() {
        try {
            Intent intent = mPhotoCameraManager.dispatchTakePictureIntent();
            startActivityForResult(intent, PhotoAction.ACTION_REQUEST_CAMERA);
        } catch (IOException e) {
            UIUtils.showToast(this, R.string.photo_msg_no_camera);
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PhotoAction.ACTION_REQUEST_CAMERA:// 相机拍照完成后，返回图片路径
                    ArrayList<String> selectPaths = mPhotoImageAdapter.getSelectImage();
                    if (mPhotoCameraManager.getCurrentPhotoPath() != null) {
                        mPhotoCameraManager.galleryAddPic();
                        selectPaths.add(mPhotoCameraManager.getCurrentPhotoPath());
                    }
                    selectResult(selectPaths);
                    break;
                case PhotoAction.ACTION_REQUEST_PREVIEW:// 预览照片
                    break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mPhotoCameraManager.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mPhotoCameraManager.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }
}
