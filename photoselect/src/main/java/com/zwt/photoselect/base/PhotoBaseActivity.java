package com.zwt.photoselect.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zwt.photoselect.R;

/**
 * Created by zhangweitao on 2018/3/8.
 * 照片选择基类页面
 */

public abstract class PhotoBaseActivity extends AppCompatActivity {

    private View mView;
    private FrameLayout mContainerLayout;
    public Toolbar mToolbar;
    public MenuItem mDoneMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(provideContentViewId());
        initView();
        initData();
        initListener();
    }

    protected abstract @LayoutRes
    int provideContentViewId();

    public void init() {
    }

    public void initView() {
    }

    public void initData() {
    }

    public void initListener() {
    }

    public void initMenuItem() {
    }

    public void onClickComplete() {
    }

    private void ensureContent() {
        if (mView == null) {
            mView = getLayoutInflater().inflate(R.layout.photo_base_include, null);
            mContainerLayout = mView.findViewById(R.id.photo_base_fl_container);
            mToolbar = mView.findViewById(R.id.photo_base_tool_bar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        ensureContent();
        View view = getLayoutInflater().inflate(layoutResID, mContainerLayout, false);
        mContainerLayout.addView(view);
        super.setContentView(mView);
    }

    @Override
    public void setContentView(View view) {
        ensureContent();
        mContainerLayout.addView(view);
        super.setContentView(mView);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        ensureContent();
        mContainerLayout.addView(view, params);
        super.setContentView(mView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        if (mContainerLayout != null) {
            mContainerLayout.addView(view);
            onContentChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_menu_select, menu);
        mDoneMenu = menu.findItem(R.id.photo_action_picker_done);
        initMenuItem();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.photo_action_picker_done) {
            onClickComplete();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
