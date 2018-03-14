package com.zwt.photoselect.intent;

import android.content.Context;
import android.content.Intent;

import com.zwt.photoselect.constants.PhotoAction;
import com.zwt.photoselect.model.ResourceModel;
import com.zwt.photoselect.ui.PhotoPreviewActivity;

import java.util.ArrayList;

/**
 * Created by zhangweitao on 2018/3/8.
 * 照片预览
 */

public class PhotoPreviewIntent extends Intent {

    public PhotoPreviewIntent(Context packageContext) {
        super(packageContext, PhotoPreviewActivity.class);
    }

    public PhotoPreviewIntent setPhotoPaths(ArrayList<String> paths) {
        this.putStringArrayListExtra(PhotoAction.ACTION_PREVIEW_SELECTED_PATHS, paths);
        return this;
    }

    public PhotoPreviewIntent setCurrentPath(String path) {
        this.putExtra(PhotoAction.ACTION_PREVIEW_CURRENT_PATH, path);
        return this;
    }

    public PhotoPreviewIntent setResourceData(ResourceModel model) {
        this.putExtra(PhotoAction.ACTION_RESOURCE_DATA, model);
        return this;
    }
}
