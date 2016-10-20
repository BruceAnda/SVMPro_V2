package com.lovearthstudio.duaui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovearthstudio.duasdk.Dua;
import com.lovearthstudio.duasdk.DuaCallback;
import com.lovearthstudio.duasdk.util.DuaPermissionUtil;
import com.lovearthstudio.duasdk.util.LogUtil;
import com.lovearthstudio.duasdk.util.TimeUtil;
import com.lovearthstudio.duaui.base.BackHandledFragment;
import com.lovearthstudio.duaui.base.BackHandledInterface;
import com.lovearthstudio.duaui.util.AlertUtil;
import com.lovearthstudio.duaui.util.FileUtil;
import com.lovearthstudio.duaui.util.IntentUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;

public class DuaActivityLogin extends FragmentActivity implements View.OnClickListener,BackHandledInterface {
    public static final String LOGIN_SUCCESS ="LoginSuccess" ;
    public ImageView leftIcon;
    public TextView leftTitle;
    public TextView centerTitle;
    public TextView rightTitle;

    public String ustr;
    public String vf_code;
    public String pwd;
    public String repwd;
    public String name="匿名";
    public String sex="U";
    public String birthday="未设置";
    public String avatar_url="";

    private Intent okIntent;
    private Intent cancelIntent;
    private BackHandledFragment mBackHandedFragment;
    public FragmentManager fragmentManager;
    public boolean isRootFragment;
    private DuaFragmentProfileAvata fragmentProfileAvata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dua.init(getApplication());
        LOCAL_IMG_PATH=getExternalCacheDir().getPath()+ File.separator;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.dua_activity_login);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.dua_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
        leftIcon=(ImageView)findViewById(R.id.dua_bar_left_icon);
        leftIcon.setOnClickListener(this);
        leftTitle=(TextView)findViewById(R.id.dua_bar_left_text);
        leftTitle.setText("返回");
        leftTitle.setOnClickListener(this);
        rightTitle=(TextView)findViewById(R.id.dua_bar_right_text);
        rightTitle.setText("取消");
        rightTitle.setOnClickListener(this);
        centerTitle=(TextView)findViewById(R.id.dua_bar_center_title);


        fragmentManager=getSupportFragmentManager();
        setCurrentFragment(new DuaFragmentLogin(),null);


        try {
            cancelIntent =IntentUtil.makeCallbackIntent(this,getIntent().getStringExtra("CancelActivity"));
            okIntent =IntentUtil.makeCallbackIntent(this,getIntent().getStringExtra("OkActivity"));
        }catch (Exception e){
        }

        DuaPermissionUtil.requestDuaPermissions(this);
    }
    public void showLoginDialog(){
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("手机号已经注册，是否直接登陆？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        onDismissPressed();
//                        Dua.getInstance().vfcodeLogin(ustr, vf_code, new DuaCallback() {
//                            @Override
//                            public void onSuccess(Object result) {
//                                finish();
//                            }
//
//                            @Override
//                            public void onError(int status, String reason) {
//                                AlertUtil.showToast(DuaActivityLogin.this,status+" "+reason);
//                            }
//                        },pwd);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private String LOCAL_IMG_PATH;
    private Uri  uri;
    private String imageName;
    private ImageView iv_avatar;
    private int RESOURCE_ID_START=View.generateViewId();
    public void showPhotoDialog(ImageView userAvatar) {
        iv_avatar=userAvatar;
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        final int startId=RESOURCE_ID_START+1;
        String[] content={"拍照","相册"};
        AlertUtil.showDialog(dlg, "",startId,content,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=v.getId();
                if(id==startId){
                }else if (id==startId+1){
                    imageName =  TimeUtil.getCurrentTimeString("yyyyMMddHHmmssSS")+".png";
                    uri= Uri.fromFile(FileUtil.newFile(LOCAL_IMG_PATH,imageName));
                    IntentUtil.startPhotoShot(DuaActivityLogin.this,uri);
                }else if (id==startId+2) {
                    imageName = TimeUtil.getCurrentTimeString("yyyyMMddHHmmssSS") + ".png";
                    uri=Uri.fromFile(FileUtil.newFile(LOCAL_IMG_PATH,imageName));
                    IntentUtil.startPhotoGallery(DuaActivityLogin.this);
                }
                dlg.cancel();
            }
        });
    }


    public void setCurrentFragment(Fragment fragment,String tag){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.dua_fragment_root, fragment,tag);
        ft.addToBackStack(tag);
        ft.commit();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IntentUtil.PHOTO_REQUEST_TAKEPHOTO:
                    break;

                case IntentUtil.PHOTO_REQUEST_GALLERY:
                    if (data != null) {
                        IntentUtil.startPhotoZoom(this, data.getData(),uri,480);
                    }
                    break;

                case IntentUtil.PHOTO_REQUEST_CUT:
                    // BitmapFactory.Options options = new BitmapFactory.Options();
                    //
                    // /**
                    // * 最关键在此，把options.inJustDecodeBounds = true;
                    // * 这里再decodeFile()，返回的bitmap为空
                    // * ，但此时调用options.outHeight时，已经包含了图片的高了
                    // */
                    // options.inJustDecodeBounds = true;
                    Bitmap bitmap = BitmapFactory.decodeFile(LOCAL_IMG_PATH + imageName);
                    iv_avatar.setImageBitmap(bitmap);
                    fragmentProfileAvata =((DuaFragmentProfileAvata) fragmentManager.findFragmentByTag("Avatar"));
                    fragmentProfileAvata.setNextStepEnable(false);
                    DuaCallback callback=new DuaCallback() {
                        @Override
                        public void onSuccess(Object result) {
                            try {
                                JSONObject jo = (JSONObject) result;
                                final String url = jo.getString("url");
                                avatar_url=url;
                                fragmentProfileAvata.setNextStepEnable(true);
                            }catch (Exception e){
                                e.printStackTrace();
                                AlertUtil.showToast(DuaActivityLogin.this,"发生未知错误，请更改图片或忽略");
                                fragmentProfileAvata.setNextStepEnable(true);
                            }

                        }

                        @Override
                        public void onError(int status, String reason) {
                            LogUtil.e(status+" "+reason);
                            AlertUtil.showToast(DuaActivityLogin.this,"图片上传失败，请更改图片或忽略");
                            fragmentProfileAvata.setNextStepEnable(true);
                        }
                    };
                    Dua.getInstance().uploadAvatar(LOCAL_IMG_PATH+imageName,callback);
                    break;

            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        int i=v.getId();
        if(i==R.id.dua_bar_left_icon||i==R.id.dua_bar_right_text){
            onDismissPressed();
        }else if(i==R.id.dua_bar_left_text){
            onBackPressed();
        }
    }

    public void onDismissPressed(){
        if(isRootFragment){
            onLoginCancel();
        }else {
            fragmentManager.popBackStackImmediate(1, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }
    @Override
    public void onBackPressed() {
        if(mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()){
            if(isRootFragment){
                onLoginCancel();
            }else{
                fragmentManager.popBackStack();
            }
        }
    }

    public void onLoginCancel(){
        setResult(Activity.RESULT_CANCELED);
        if(cancelIntent!=null) startActivity(cancelIntent);
        finish();
    }
    public void onLoginOk(){
        setResult(RESULT_OK);
        if(okIntent !=null) startActivity(okIntent);
        EventBus.getDefault().post(LOGIN_SUCCESS);
        finish();
    }
}
