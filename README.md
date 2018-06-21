## photolibrary

- 支持多张图片选择；
- 支持拍照；

## 图片 example
![](https://github.com/zhangweitao521/photolibrary/raw/master/screen/TIM图片20180315120703.jpg)

![](http://github.com/zhangweitao521/photolibrary/raw/master/screen/TIM图片20180315120657.jpg)

![](http://github.com/zhangweitao521/photolibrary/raw/master/screen/TIM图片20180315120708.jpg)

## useage
```html
dependencies {
   compile 'com.zwt.photolibrary:photoselect:1.0.2'
}
```
## maven
```html
<dependency>
  <groupId>com.zwt.photolibrary</groupId>
  <artifactId>photoselect</artifactId>
  <version>1.0.2</version>
  <type>pom</type>
</dependency>
```
## 选择照片
```java
PhotoSelectIntent intent = new PhotoSelectIntent(MainActivity.this);  
intent.setShowCamera(true).setShowMulti(true).setMaxNumber(11);  
startActivityForResult(intent, 1);
```
## 预览照片
```java
PhotoPreviewIntent intent = new PhotoPreviewIntent(MainActivity.this);  
intent.setCurrentPath(path).setPhotoPaths(list);  
startActivity(intent);
```
## 获取选择的照片
```java
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        if (resultCode == RESULT_OK) {  
            switch (requestCode) {  
                case 1:  
                    list = data.getStringArrayListExtra(PhotoAction.ACTION_RESULT_LIST);  
                    break;  
            }  
        }  
    }
```
## manifest //设置权限以及注册Activity
```html
<manifest xmlns:android="http://schemas.android.com/apk/res/android">  
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>  
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>  
    <uses-permission android:name="android.permission.CAMERA" />  
  <application ...>  
       <!--@style/PhotoAppTheme是图片选择界面样式-->  
       <activity  
            android:name="com.zwt.photoselect.ui.PhotoSelectActivity"  
            android:theme="@style/PhotoAppTheme" />  

        <activity  
            android:name="com.zwt.photoselect.ui.PhotoPreviewActivity"  
            android:theme="@style/PhotoAppTheme" />  
    
  </application>  
</manifest> 
```
## android7.0拍照权限问题
```html
1.<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    >
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.CAMERA" />
  <application
    ...
    >
    ...
        <!-- 解决Android7.0相机相册权限配置 -->
        <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="com.zwt.testapplication.provider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths" />
        </provider>
    
  </application>
</manifest>
2.在res文件夹下新建xml包，在xml包下新建file_paths.xml文件；
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <paths>
        <external-path
            name="camera_photos"
            path="." />
    </paths>
</resources>
```
## 更新日志
Version: 1.0.2
- 修复权限问题；
