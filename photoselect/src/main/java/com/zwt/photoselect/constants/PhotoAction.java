package com.zwt.photoselect.constants;

/**
 * Created by zhangweitao on 2018/3/8.
 * 动作标识
 */

public class PhotoAction {
    /**
     * 是否展示照相机
     */
    public final static String ACTION_IS_SHOW_CAMERA = "action_is_show_camera";
    /**
     * 是否是多选
     */
    public final static String ACTION_IS_SHOW_MULTI = "action_is_show_multi";
    /**
     * 最大选择照片数量
     */
    public final static String ACTION_MAX_SELECT_NUMBER = "action_max_select_number";
    /**
     * 默认选中照片
     */
    public final static String ACTION_DEFAULT_SELECTED_PATHS = "action_default_selected_paths";
    /**
     * 预览照片集合
     */
    public final static String ACTION_PREVIEW_SELECTED_PATHS = "action_preview_selected_paths";
    /**
     * 预览默认展示项
     */
    public final static String ACTION_PREVIEW_CURRENT_PATH = "action_preview_current_path";
    /**
     * 选择好的照片路径集合
     */
    public final static String ACTION_RESULT_LIST = "action_result_list";
    /**
     * 资源文件设置
     */
    public final static String ACTION_RESOURCE_DATA = "action_resource_data";
    /**
     * 预览请求状态码
     */
    public final static int ACTION_REQUEST_PREVIEW = 1;
    /**
     * 拍照请求状态码
     */
    public final static int ACTION_REQUEST_CAMERA = 2;
}
