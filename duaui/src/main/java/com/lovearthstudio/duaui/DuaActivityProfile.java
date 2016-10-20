package com.lovearthstudio.duaui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.widgets.WheelDatePicker;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lovearthstudio.duasdk.Dua;
import com.lovearthstudio.duasdk.DuaCallback;
import com.lovearthstudio.duasdk.util.LogUtil;
import com.lovearthstudio.duasdk.util.SharedPreferenceUtil;
import com.lovearthstudio.duasdk.util.TimeUtil;
import com.lovearthstudio.duaui.base.PopupEdit;
import com.lovearthstudio.duaui.util.AlertUtil;
import com.lovearthstudio.duasdk.util.DuaPermissionUtil;
import com.lovearthstudio.duaui.util.FileUtil;
import com.lovearthstudio.duaui.util.IntentUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DuaActivityProfile extends AppCompatActivity implements View.OnClickListener,WheelDatePicker.OnDateSelectedListener{
    private String LOCAL_IMG_PATH;
    private String imageName;
    private Uri  uri;
    private RelativeLayout rl_avatar;
    private RelativeLayout rl_sex;
    private TextView tv_sex;
    private RelativeLayout rl_bday;
    private TextView tv_bday;
    private RelativeLayout rl_height;
    private TextView tv_height;
    private List heightData=newIncArray(120,80);
    private RelativeLayout rl_weight;
    private TextView tv_weight;
    private List weightData=newIncArray(50,50);
    private RelativeLayout rl_name;
    private TextView tv_name;
    private ImageView iv_avatar;
    private RelativeLayout rl_sign;
    private TextView tv_sign;

    private int RESOURCE_ID_START;



    private RelativeLayout rl_picker;
    private WheelPicker wheelPicker;
    private WheelDatePicker datePicker;
    private Button btn_picker_cancel;
    private Button btn_picker_ensure;
    private int curType;
    private String curDate;

    public static final String PREF_PROFILE="profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOCAL_IMG_PATH=getExternalCacheDir().getPath()+ File.separator;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.dua_activity_profile);

        RESOURCE_ID_START=View.generateViewId();
        rl_picker=(RelativeLayout)findViewById(R.id.dua_rl_profile_picker);
        rl_picker.setVisibility(View.GONE);
        wheelPicker=(WheelPicker)findViewById(R.id.dua_profile_picker);
        wheelPicker.setIndicator(true);
        wheelPicker.setVisibleItemCount(4);
        wheelPicker.setItemTextSize(50);
        btn_picker_cancel=(Button)findViewById(R.id.dua_profile_picker_cancel);
        btn_picker_cancel.setOnClickListener(this);
        btn_picker_ensure=(Button)findViewById(R.id.dua_profile_picker_ensure);
        btn_picker_ensure.setOnClickListener(this);

        datePicker =(WheelDatePicker)findViewById(R.id.dua_date_picker);
//        TextView yearTv=(TextView)findViewById(R.id.wheel_date_picker_year_tv);
//        yearTv.setTextColor(Color.parseColor("#55000000"));
//        yearTv.setTextSize(25);
//        TextView monthTv=(TextView)findViewById(R.id.wheel_date_picker_month_tv);
//        monthTv.setTextColor(Color.parseColor("#55000000"));
//        monthTv.setTextSize(25);
//        TextView dayTv=(TextView)findViewById(R.id.wheel_date_picker_day_tv);
//        dayTv.setTextColor(Color.parseColor("#55000000"));
//        dayTv.setTextSize(25);
        datePicker.setOnDateSelectedListener(this);
        int year= Calendar.getInstance().get(Calendar.YEAR);
        datePicker.setYearStart(year-100);
        datePicker.setYearEnd(year);
        datePicker.setIndicator(true);
        datePicker.setIndicatorColor(Color.parseColor("#55000000"));
        datePicker.setSelectedYear(year-18);
        datePicker.setCurved(true);
        datePicker.setCyclic(true);
        datePicker.setVisibleItemCount(5);

        rl_name=(RelativeLayout)findViewById(R.id.rl_name);
        rl_name.setOnClickListener(this);
        tv_name=(TextView)findViewById(R.id.tv_name);
        rl_bday=(RelativeLayout)findViewById(R.id.rl_bday);
        rl_bday.setOnClickListener(this);
        tv_bday=(TextView)findViewById(R.id.tv_bday);
        rl_sex=(RelativeLayout)findViewById(R.id.rl_sex);
        rl_sex.setOnClickListener(this);
        tv_sex=(TextView)findViewById(R.id.tv_sex);
        rl_bday=(RelativeLayout)findViewById(R.id.rl_bday);
        rl_bday.setOnClickListener(this);
        tv_bday=(TextView)findViewById(R.id.tv_bday);
        rl_height=(RelativeLayout)findViewById(R.id.rl_height);
        rl_height.setOnClickListener(this);
        tv_height=(TextView)findViewById(R.id.tv_height);
        rl_weight=(RelativeLayout)findViewById(R.id.rl_weight);
        rl_weight.setOnClickListener(this);
        tv_weight=(TextView)findViewById(R.id.tv_weight);
        rl_avatar=(RelativeLayout)findViewById(R.id.rl_avatar);
        rl_avatar.setOnClickListener(this);
        iv_avatar=(ImageView)findViewById(R.id.iv_avatar);
        rl_sign=(RelativeLayout)findViewById(R.id.rl_sign);
        rl_sign.setOnClickListener(this);
        tv_sign=(TextView) findViewById(R.id.tv_sign);

        tv_name.setText(initProfile("name",""));
        tv_bday.setText(initProfile("bday",""));
        tv_height.setText(initProfile("height","cm"));
        tv_weight.setText(initProfile("weight","kg"));
        tv_sign.setText(initProfile("saying",""));
        tv_sex.setText(SharedPreferenceUtil.prefGetKey(this,PREF_PROFILE,"sex","").equals("F") ? "女" : "男");
        String avatar=SharedPreferenceUtil.prefGetKey(this,PREF_PROFILE,"avatar","");
        LogUtil.e("头像地址是："+avatar);
        if(avatar!=null&&!avatar.equals("")){
            Glide.with(DuaActivityProfile.this)
                    .load(avatar)
//                    .placeholder(R.mipmap.head)
                    //.error(R.mipmap.ic_launcher)
//                .override((int) (Constant.screenwith - Constant.mainItemPadding - Constant.mainPadding), (int) (img_height * ratio))
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_avatar);
        }
    }
    private void showSexDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        final String title="性别";
        final int startId=RESOURCE_ID_START;
        final String[] content={"男","女"};
        AlertUtil.showDialog(dlg, title,startId,content,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=v.getId();
                if(id==startId){
                }else if (id==startId+1){
                    tv_sex.setText(content[0]);
                    updateProfile("sex","M");
                }else if (id==startId+2) {
                    tv_sex.setText(content[1]);
                    updateProfile("sex","F");
                }
                dlg.cancel();
            }
        });

    }

    private void showPhotoDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        final int startId=RESOURCE_ID_START+3;
        String[] content={"拍照","相册"};
        AlertUtil.showDialog(dlg, "",startId,content,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=v.getId();
                if(id==startId){
                }else if (id==startId+1){
                    imageName =  TimeUtil.getCurrentTimeString("yyyyMMddHHmmssSS")+".png";
                    uri=Uri.fromFile(FileUtil.newFile(LOCAL_IMG_PATH,imageName));
                    IntentUtil.startPhotoShot(DuaActivityProfile.this,uri);
                }else if (id==startId+2) {
                    imageName = TimeUtil.getCurrentTimeString("yyyyMMddHHmmssSS") + ".png";
                    uri=Uri.fromFile(FileUtil.newFile(LOCAL_IMG_PATH,imageName));
                    IntentUtil.startPhotoGallery(DuaActivityProfile.this);
                }
                dlg.cancel();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IntentUtil.PHOTO_REQUEST_TAKEPHOTO:
                    IntentUtil.startPhotoZoom(this,uri,uri,480);
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
                    final Bitmap bitmap = BitmapFactory.decodeFile(LOCAL_IMG_PATH + imageName);
                    iv_avatar.setImageBitmap(bitmap); //此处没有作用是因为onActivityResult发生在onResume前。
                    DuaCallback callback=new DuaCallback() {
                        @Override
                        public void onSuccess(Object result) {
                            iv_avatar.setImageBitmap(bitmap);
                            updateProfile("avatar",Dua.getInstance().getCurrentDuaUser().avatar);
                            AlertUtil.showToast(DuaActivityProfile.this,"头像更新成功");
                        }

                        @Override
                        public void onError(int status, String reason) {
                            iv_avatar.setImageBitmap(bitmap);
                            AlertUtil.showToast(DuaActivityProfile.this,"头像更新失败"+reason);
                        }
                    };
                    Dua.getInstance().updateAvatar(LOCAL_IMG_PATH+imageName,callback);
                    break;

            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.rl_avatar) {
            if(DuaPermissionUtil.invalidSdAndCamera(this)) {
                DuaPermissionUtil.requestSdAndCamera(this,10086);
            }else {
                showPhotoDialog();
            }
        }else if(i==R.id.rl_name){
            final PopupEdit pop=new PopupEdit(this);
            pop.setOnCommitListener(new PopupEdit.OnCommitListener() {
                @Override
                public void onClick(View view, String content) {
                    tv_name.setText(content);
                    updateProfile("name",content);
                    pop.dismiss();
                }
            });
            pop.showPopupWindow();
        }else if(i==R.id.rl_bday){
            if(rl_picker.getVisibility()==View.VISIBLE){
                rl_picker.setVisibility(View.GONE);
            }else {
                wheelPicker.setVisibility(View.GONE);
                datePicker.setVisibility(View.VISIBLE);
                rl_picker.setVisibility(View.VISIBLE);
                curType = 0;
            }
        }else if(i==R.id.rl_sex){
            showSexDialog();
        }else if(i==R.id.rl_height){
            if(rl_picker.getVisibility()==View.VISIBLE){
                rl_picker.setVisibility(View.GONE);
            }else {
                wheelPicker.setData(heightData);
                wheelPicker.setSelectedItemPosition(heightData.size()/2);
                wheelPicker.setItemTextSize(75);
                datePicker.setVisibility(View.GONE);
                wheelPicker.setVisibility(View.VISIBLE);
                rl_picker.setVisibility(View.VISIBLE);
                curType = 1;
            }
        }else if(i==R.id.rl_weight){
            if(rl_picker.getVisibility()==View.VISIBLE){
                rl_picker.setVisibility(View.GONE);
            }else {
                wheelPicker.setData(weightData);
                wheelPicker.setSelectedItemPosition(weightData.size()/2);
                wheelPicker.setItemTextSize(75);
                datePicker.setVisibility(View.GONE);
                wheelPicker.setVisibility(View.VISIBLE);
                rl_picker.setVisibility(View.VISIBLE);
                curType = 2;
            }
        }else if(i==R.id.dua_profile_picker_cancel){
            rl_picker.setVisibility(View.GONE);
        }else if(i==R.id.dua_profile_picker_ensure){
            if(curType==1) {
                Object data1=heightData.get(wheelPicker.getCurrentItemPosition());
                tv_height.setText(data1+"cm");
                updateProfile("height",data1);
            }else if(curType==2){
                Object data2=weightData.get(wheelPicker.getCurrentItemPosition());
                tv_weight.setText(data2+"kg");
                updateProfile("weight",data2);
            }else if(curType==0){
                tv_bday.setText(curDate);
                updateProfile("bday",curDate);
            }
            rl_picker.setVisibility(View.GONE);
        }else if (i==R.id.rl_sign){
            final PopupEdit pop=new PopupEdit(this);
            pop.setOnCommitListener(new PopupEdit.OnCommitListener() {
                @Override
                public void onClick(View view, String content) {
                    tv_sign.setText(content);
                    updateProfile("saying",content);
                    pop.dismiss();
                }
            });
            pop.showPopupWindow();
        }
    }

    public List newIncArray(int start,int count){
        List array=new ArrayList<>();
        for (int i = 0; i <count ; i++) {
            array.add(start+i);
        }
        return array;
    }

    public String initProfile(String key,String unit){
        String value=SharedPreferenceUtil.prefGetKey(this,PREF_PROFILE,key,"");
        if(!value.equals("")) value +=unit;
        return value;
    }

    public void updateProfile(final String key, final Object value){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put(key,value);
            Dua.getInstance().setUserProfile(jsonObject, new DuaCallback() {
                @Override
                public void onSuccess(Object result) {
                    SharedPreferenceUtil.prefSetKey(getApplicationContext(),PREF_PROFILE,key,String.valueOf(value));

                    JSONObject event = new JSONObject();
                    try {
                        event.put("type", "update_profile");
                        EventBus.getDefault().post(event);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(int status, String reason) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getProfile() {
        JSONArray array = Dua.paramsToJSONArray("bday", "sex", "name", "avatar", "height", "weight", "saying");
        Dua.getInstance().getUserProfile(array, new DuaCallback() {
            @Override
            public void onSuccess(Object res) {
                try {
                    JSONObject result=(JSONObject) res;
                    String bday = result.optString("bday");
                    String sex = result.optString("sex");
                    String name =result.optString("name");
                    String avatar = result.optString("avatar");
                    String height = result.optString("height");
                    String weight = result.optString("weight");
                    String saying = result.optString("saying");

                    getSharedPreferences("profile", Context.MODE_PRIVATE).edit().putString("bday", bday)
                            .putString("sex", sex)
                            .putString("name", name)
                            .putString("avatar", avatar)
                            .putString("height", height)
                            .putString("weight", weight)
                            .putString("saying", saying).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int status, String reason) {

            }
        });
    }
    @Override
    public void onDateSelected(WheelDatePicker picker, Date date) {
        curDate=TimeUtil.toTimeString(date.getTime(),"yyyyMMdd");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
