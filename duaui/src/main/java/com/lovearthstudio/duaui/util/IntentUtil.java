package com.lovearthstudio.duaui.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Author：Mingyu Yi on 2016/5/12 22:10
 * Email：461072496@qq.com
 */
public class IntentUtil {

    public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public static final int PHOTO_REQUEST_CUT = 3;// 结果

    public static void startPhotoShot(Activity activity, Uri uri){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
    }

    public static void startPhotoGallery(Activity activity){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    public static void startPhotoZoom(Activity activity, Uri input,Uri output, int size) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(input, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", false);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        activity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }





    public static Intent makeCrossAppIntent(String fullActivityName){
        int index=fullActivityName.lastIndexOf(".");
        String packageName=fullActivityName.substring(0,index);
        String activityName = fullActivityName.substring(index + 1);
        return makeCrossAppIntent(packageName,activityName);
    }
    public static Intent makeCrossAppIntent(String packageName, String activityName){
        Intent intent=null;
        if(packageName!=null&&activityName!=null){
            try {
                intent=new Intent().setComponent(new ComponentName(packageName, packageName+"."+activityName));
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return intent;
    }
    public static Intent makeCallbackIntent(Context context,String packageName,String activityName){
        return makeCallbackIntent(context,packageName+"."+activityName);
    }
    public static Intent makeCallbackIntent(Context context,String fullActivityName){
        Intent intent=null;
        Class<?> clazz = null;
        if(fullActivityName!=null) {
            try {
                clazz = Class.forName(fullActivityName );
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if(context!=null&&clazz!=null){
            try {
                intent=new Intent(context, clazz);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return intent;
    }
}
