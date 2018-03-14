package com.zwt.photoselect.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by zhangweitao on 2018/3/8.
 * 显示工具类
 */

public class UIUtils {

    public static void showToast(Context context, @StringRes int resId) {
        Toast.makeText(context, context.getResources().getString(resId), Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static String getStringText(Context context, @StringRes int resId) {
        return context.getResources().getString(resId);
    }
}
