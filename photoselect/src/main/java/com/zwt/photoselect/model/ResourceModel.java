package com.zwt.photoselect.model;

import android.support.annotation.IdRes;

import com.zwt.photoselect.constants.PhotoDefault;

import java.io.Serializable;

/**
 * Created by zhangweitao on 2018/3/8.
 * 设置资源文件
 */

public class ResourceModel implements Serializable {

    private String dirName = PhotoDefault.DEFAULT_PICTURE_SAVE_DIR;
    private @IdRes
    int errPicture = PhotoDefault.DEFAULT_PICTURE_ERROR;

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public int getErrPicture() {
        return errPicture;
    }

    public void setErrPicture(int errPicture) {
        this.errPicture = errPicture;
    }

}
