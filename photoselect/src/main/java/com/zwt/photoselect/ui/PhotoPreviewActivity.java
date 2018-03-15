package com.zwt.photoselect.ui;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.zwt.photoselect.R;
import com.zwt.photoselect.adapter.PhotoPagerAdapter;
import com.zwt.photoselect.base.PhotoBaseActivity;
import com.zwt.photoselect.constants.PhotoAction;
import com.zwt.photoselect.model.ResourceModel;
import com.zwt.photoselect.view.ViewPagerFixed;

import java.util.ArrayList;

/**
 * Created by zhangweitao on 2018/3/8.
 * 预览页面
 */

public class PhotoPreviewActivity extends PhotoBaseActivity {

    private ViewPagerFixed mViewPagerFixed;
    private PhotoPagerAdapter mPhotoPagerAdapter;
    private ArrayList<String> mPaths;
    private String mCurrentPath = "";
    private ResourceModel mResourceModel;

    @Override
    protected int provideContentViewId() {
        return R.layout.photo_activity_preview;
    }

    @Override
    public void init() {
        super.init();
        mPaths = getIntent().getStringArrayListExtra(PhotoAction.ACTION_PREVIEW_SELECTED_PATHS);
        mCurrentPath = getIntent().getStringExtra(PhotoAction.ACTION_PREVIEW_CURRENT_PATH);
        ResourceModel newResource = (ResourceModel) getIntent().getSerializableExtra(PhotoAction.ACTION_RESOURCE_DATA);
        mResourceModel = newResource == null ? new ResourceModel() : newResource;
    }

    @Override
    public void initView() {
        super.initView();
        mViewPagerFixed = findViewById(R.id.photo_preview_viewpager);
    }

    @Override
    public void initData() {
        super.initData();
        mPhotoPagerAdapter = new PhotoPagerAdapter(this, mResourceModel.getErrPicture());
        mPhotoPagerAdapter.setPreviewData(mPaths);
        mViewPagerFixed.setAdapter(mPhotoPagerAdapter);
        mViewPagerFixed.setCurrentItem(mPaths.indexOf(mCurrentPath));
        mViewPagerFixed.setOffscreenPageLimit(5);
        updateToolBarTitle();
    }

    @Override
    public void initListener() {
        super.initListener();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mViewPagerFixed.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateToolBarTitle();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initMenuItem() {
        super.initMenuItem();
        mDoneMenu.setVisible(false);
    }

    private void updateToolBarTitle() {
        getSupportActionBar().setTitle(getString(R.string.photo_preview_image_index,
                mViewPagerFixed.getCurrentItem() + 1, mPaths.size()));
    }
}
