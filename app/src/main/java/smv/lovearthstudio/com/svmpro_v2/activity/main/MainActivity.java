package smv.lovearthstudio.com.svmpro_v2.activity.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.RadioGroup;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import smv.lovearthstudio.com.svmpro_v2.R;
import smv.lovearthstudio.com.svmpro_v2.fragment.FFTFragment;
import smv.lovearthstudio.com.svmpro_v2.fragment.PredictFragment;
import smv.lovearthstudio.com.svmpro_v2.fragment.SettingFragment;
import smv.lovearthstudio.com.svmpro_v2.fragment.SimpingFragment;
import smv.lovearthstudio.com.svmpro_v2.fragment.TrainFragment;

import static smv.lovearthstudio.com.svmpro_v2.util.Util.FILE_SHAREDPERFERENCES;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.MODEL_FILE_DIR;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.PREDICT_FILE_DIR;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.RANGE_FILE_DIR;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.SCALE_FILE_DIR;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.TRAIN_FILE_DIR;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.selectFeatures;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.sensit;

/**
 * 整个程序的主界面逻辑
 */
public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    @BindView(R.id.bottom_tabs)
    RadioGroup mBottomTabs;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // 检查权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                init();
            }
        } else {
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    init();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    new AlertDialog.Builder(MainActivity.this).setTitle("提示!").setMessage("请赋予权限!").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    }).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * 初始化操作
     */
    private void init() {
        mBottomTabs.setOnCheckedChangeListener(this);

        mBottomTabs.check(R.id.tab_samping);

        createDirs();

        initSensit();
    }

    /**
     * 初始化传感器采样频率
     */
    private void initSensit() {
        mSharedPreferences = getSharedPreferences(FILE_SHAREDPERFERENCES, MODE_PRIVATE);
        sensit = mSharedPreferences.getString("sensorhz", "32HZ");
        readSelectFeatures();
    }

    /**
     * 读取特征值
     */
    private void readSelectFeatures() {
        String featuresSelected = mSharedPreferences.getString("featuresSelected", "");
        if (TextUtils.isEmpty(featuresSelected)) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 19; i++) {
                stringBuilder.append(i + ":true,");
            }
            featuresSelected = stringBuilder.substring(0, stringBuilder.lastIndexOf(","));
            mSharedPreferences.edit().putString("featuresSelected", featuresSelected).commit();
        }
        String[] split = featuresSelected.split(",");
        for (int i = 0; i < split.length; i++) {
            String[] feature = split[i].split(":");
            selectFeatures.put(Integer.parseInt(feature[0]), Boolean.parseBoolean(feature[1]));
        }
    }

    /**
     * 创建文件目录
     */
    private void createDirs() {
        File trainDir = new File(TRAIN_FILE_DIR);
        if (!trainDir.exists()) {
            trainDir.mkdirs();
        }
        File rangeDir = new File(RANGE_FILE_DIR);
        if (!rangeDir.exists()) {
            rangeDir.mkdirs();
        }
        File scaleDir = new File(SCALE_FILE_DIR);
        if (!scaleDir.exists()) {
            scaleDir.mkdirs();
        }
        File modelDir = new File(MODEL_FILE_DIR);
        if (!modelDir.exists()) {
            modelDir.mkdirs();
        }
        File predictDir = new File(PREDICT_FILE_DIR);
        if (!predictDir.exists()) {
            predictDir.mkdirs();
        }
    }

    /**
     * 当底部的tab选择改变后会调用此方法
     *
     * @param group
     * @param checkedId
     */
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.tab_samping:  // tab_simping_normal
                setTitle(getString(R.string.simping));
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_content, new SimpingFragment())
                        .commit();
                break;
            case R.id.tab_train:    // tab_train_normal
                setTitle(getString(R.string.train));
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_content, new TrainFragment())
                        .commit();
                break;
            case R.id.tab_forecast:     // tab_predict_normal
                setTitle(getString(R.string.predict));
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_content, new PredictFragment())
                        .commit();
                break;
            case R.id.tab_fft:
                setTitle(getString(R.string.fft));
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_content, new FFTFragment())
                        .commit();
                break;
            case R.id.tab_setting:      // tab_setting_normal
                setTitle(getString(R.string.setting));
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_content, new SettingFragment())
                        .commit();
                break;
        }
    }
}
