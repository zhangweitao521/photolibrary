package com.zwt.photoselect.intent;

import android.content.Context;
import android.content.Intent;

import com.zwt.photoselect.constants.PhotoAction;
import com.zwt.photoselect.model.ResourceModel;
import com.zwt.photoselect.ui.PhotoSelectActivity;

import java.util.ArrayList;

/**
 * Created by zhangweitao on 2018/3/8.
 * 照片选择
 */

public class PhotoSelectIntent extends Intent {

    public PhotoSelectIntent(Context packageContext) {
        super(packageContext, PhotoSelectActivity.class);
    }

    public PhotoSelectIntent setShowCamera(boolean bool) {
        this.putExtra(PhotoAction.ACTION_IS_SHOW_CAMERA, bool);
        return this;
    }

    public PhotoSelectIntent setShowMulti(boolean bool) {
        this.putExtra(PhotoAction.ACTION_IS_SHOW_MULTI, bool);
        return this;
    }

    public PhotoSelectIntent setMaxNumber(int total) {
        this.putExtra(PhotoAction.ACTION_MAX_SELECT_NUMBER, total);
        return this;
    }

    public PhotoSelectIntent setSelectedPaths(ArrayList<String> imagePaths) {
        this.putStringArrayListExtra(PhotoAction.ACTION_DEFAULT_SELECTED_PATHS, imagePaths);
        return this;
    }

    public PhotoSelectIntent setResourceData(ResourceModel model) {
        this.putExtra(PhotoAction.ACTION_RESOURCE_DATA, model);
        return this;
    }
}
