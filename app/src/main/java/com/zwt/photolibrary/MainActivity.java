package com.zwt.photolibrary;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zwt.photoselect.constants.PhotoAction;
import com.zwt.photoselect.intent.PhotoPreviewIntent;
import com.zwt.photoselect.intent.PhotoSelectIntent;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private SampleAdapter mSampleAdapter;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermission();
            }
        });
        mSampleAdapter = new SampleAdapter(this);
        mRecyclerView = findViewById(R.id.select_recycleview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.setAdapter(mSampleAdapter);
        mSampleAdapter.setOnPhotoClickListener(new SampleAdapter.OnPhotoClickListener() {
            @Override
            public void onItemClick(String path) {
                if (list != null && list.size() > 0) {
                    PhotoPreviewIntent intent = new PhotoPreviewIntent(MainActivity.this);
                    intent.setCurrentPath(path).setPhotoPaths(list);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    list = data.getStringArrayListExtra(PhotoAction.ACTION_RESULT_LIST);
                    mSampleAdapter.setData(list);
                    break;
            }
        }
    }

    private void checkCameraPermission() {
        MPermissionUtils.requestPermissionsResult(this, 2,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}
                , new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        PhotoSelectIntent intent = new PhotoSelectIntent(MainActivity.this);
                        intent.setShowCamera(true)
                                .setShowMulti(true)
                                .setMaxNumber(11);
                        startActivityForResult(intent, 1);
                    }

                    @Override
                    public void onPermissionDenied() {
                        MPermissionUtils.showTipsDialog(MainActivity.this);
                    }
                });
    }
}
