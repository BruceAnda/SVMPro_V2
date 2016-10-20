package com.lovearthstudio.duaui.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: wulong
 * Date: 13-10-10
 * Time: 上午11:37
 * To change this template use File | Settings | File Templates.
 */
public class ScreenShot {

    private static final String TAG = ScreenShot.class.getName();


    private static final int SAVE_AUTHORITY = Context.MODE_PRIVATE;

    // 获取指定Activity的截屏，保存到png文件
    public static Bitmap takeScreenShot(Activity activity) {

//View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();


//获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        System.out.println(statusBarHeight);

//获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();


//去掉标题栏
//Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }


    //保存到sdcard
    public static void savePic(Bitmap b, String strFileName) {
        FileOutputStream fos = null;
        try {
//            fos = act.openFileOutput(strFileName, SAVE_AUTHORITY);
            fos=new FileOutputStream(strFileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "savePic e = ", e);
        }
    }

    private static void shareAct(Activity act, String fileName, String text) {

        Uri uri = null;

        try{
            FileInputStream input = act.openFileInput(fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            uri = Uri.parse(MediaStore.Images.Media.insertImage(act.getContentResolver(), bitmap, null, null));
            input.close();
        } catch(Exception e){
            Log.v(TAG,"shareAct e = ",e);
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "好友推荐");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        act.startActivity(Intent.createChooser(shareIntent, act.getTitle()));
    }

    public static void share(Activity act, String text) {
        String saveFileName = "share_pic.jpg";
        savePic(takeScreenShot(act), saveFileName);
        shareAct(act,saveFileName,text);
    }
}