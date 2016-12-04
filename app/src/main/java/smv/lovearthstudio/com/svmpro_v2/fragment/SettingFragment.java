package smv.lovearthstudio.com.svmpro_v2.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import smv.lovearthstudio.com.svmpro_v2.R;
import smv.lovearthstudio.com.svmpro_v2.activity.other.setting.FeaturesListActivity;
import smv.lovearthstudio.com.svmpro_v2.activity.other.setting.SensitActivity;
import smv.lovearthstudio.com.svmpro_v2.widget.Item;

import static android.content.Context.MODE_PRIVATE;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_REQUST_FEATURES_LIST;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_REQUST_SENSORHZ_LIST;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_RESULT_OK;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.FILE_SHAREDPERFERENCES;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.sensit;

/**
 * 设置页面
 *
 * @author zhaoliang
 *         create at 2016/11/11 下午2:18
 */
public class SettingFragment extends Fragment {

    private SharedPreferences mSharedPreferences;

    @BindView(R.id.setting_item_sensorhz)
    Item mItemSensorHz;
    @BindView(R.id.setting_item_features)
    Item mItemFeatures;

    @OnClick({R.id.setting_item_sensorhz, R.id.setting_item_features})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.setting_item_sensorhz:
                startActivityForResult(new Intent(getActivity(), SensitActivity.class), CODE_REQUST_SENSORHZ_LIST);
                break;
            case R.id.setting_item_features:
                startActivityForResult(new Intent(getActivity(), FeaturesListActivity.class), CODE_REQUST_FEATURES_LIST);
                break;
        }
    }

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        initSharedPerferences();
        initData();
    }

    private void initData() {
        mItemSensorHz.setText("传感器采样频率:" + mSharedPreferences.getString("sensorhz", "32HZ"));

    }

    int currentFeaturesNum;

    private void initSharedPerferences() {
        mSharedPreferences = getActivity().getSharedPreferences(FILE_SHAREDPERFERENCES, MODE_PRIVATE);
        String featuresSelected = mSharedPreferences.getString("featuresSelected", "");
        String[] split = featuresSelected.split(",");
        for (int i = 0; i < split.length; i++) {
            String[] strings = split[i].split(":");
            if (Boolean.parseBoolean(strings[1])) {
                currentFeaturesNum++;
            }
        }
        mItemFeatures.setText("当前特征数:" + currentFeaturesNum);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_REQUST_SENSORHZ_LIST && resultCode == CODE_RESULT_OK) {
            String sensorhz = data.getStringExtra("sensorhz");
            if (!TextUtils.isEmpty(sensorhz)) {
                mSharedPreferences.edit().putString("sensorhz", sensorhz).commit();
                mItemSensorHz.setText("传感器采样频率:" + sensorhz);
                sensit = sensorhz;
            }
        }
        if (requestCode == CODE_REQUST_FEATURES_LIST && resultCode == CODE_RESULT_OK) {
            String featureNum = data.getStringExtra("featureNum");
            if (!TextUtils.isEmpty(featureNum)) {
                mItemFeatures.setText("当前特征数:" + featureNum);
            }
        }
    }
}
