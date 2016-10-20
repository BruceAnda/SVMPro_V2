package com.lovearthstudio.duaui.util;

import java.io.File;
import java.io.FileOutputStream;


import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class FileUtil {
    public String local_image_path;
    public FileUtil(String path){
        this.local_image_path=path;
        makeDir(local_image_path);
    }
    public boolean saveBitmap(String filename, Bitmap bitmap){
        return saveBitmap(local_image_path,filename,bitmap);
    }
    public boolean isFileExists(String filename) {
        return isFileExists(filename,local_image_path);
    }
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    public static boolean saveBitmap(String path,String filename, Bitmap bitmap) {
        if (!isExternalStorageWritable()) {
            Log.e("DuaFileUtil","SD卡不可用，保存失败");
            return false;
        }
        if (bitmap == null) {
            return false;
        }
        try {
            
            File file = newFile(path,filename);
            FileOutputStream outputstream = new FileOutputStream(file);
            if((filename.indexOf("png") != -1)||(filename.indexOf("PNG") != -1))  
            {  
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputstream);
            }  else{
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputstream);
            }
            outputstream.flush();
            outputstream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } 
    }
    public static boolean isFileExists(String filename,String path) {
        return newFile(path,filename).exists();
    }
    public static File newFile(String path,String filename) {
        return new File(makeDir(path), filename);
    }
    public static File makeDir(String path){
        File dir =new File(path);
        if(!dir.exists()){
            dir.mkdirs();
        }
        return dir;
    }
}
