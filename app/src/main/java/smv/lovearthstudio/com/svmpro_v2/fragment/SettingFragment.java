package smv.lovearthstudio.com.svmpro_v2.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import smv.lovearthstudio.com.svmpro_v2.R;
import smv.lovearthstudio.com.svmpro_v2.activity.other.setting.FeaturesListActivity;
import smv.lovearthstudio.com.svmpro_v2.activity.other.setting.SensitActivity;

import static android.content.Context.MODE_PRIVATE;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_REQUST_FEATURES_LIST;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_REQUST_SENSORHZ_LIST;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_RESULT_OK;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.FILE_SHAREDPERFERENCES;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.sensit;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

    private TextView mTvSelectSensorHz, mTvSelectFeatures;
    private View view;
    private SharedPreferences mSharedPreferences;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        init();
        return view;
    }

    private void init() {
        findView();
        setListener();
        initSharedPerferences();
        initData();
    }

    private void initData() {
        mTvSelectSensorHz.setText("传感器采样频率:" + mSharedPreferences.getString("sensorhz", "32HZ"));

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
        mTvSelectFeatures.setText("当前特征数:" + currentFeaturesNum);
    }

    private void setListener() {
        mTvSelectSensorHz.setOnClickListener(this);
        mTvSelectFeatures.setOnClickListener(this);
    }

    private void findView() {
        mTvSelectSensorHz = (TextView) view.findViewById(R.id.tv_select_sensorhz);
        mTvSelectFeatures = (TextView) view.findViewById(R.id.tv_select_features);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_sensorhz:
                startActivityForResult(new Intent(getActivity(), SensitActivity.class), CODE_REQUST_SENSORHZ_LIST);
                break;
            case R.id.tv_select_features:
                startActivityForResult(new Intent(getActivity(), FeaturesListActivity.class), CODE_REQUST_FEATURES_LIST);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_REQUST_SENSORHZ_LIST && resultCode == CODE_RESULT_OK) {
            String sensorhz = data.getStringExtra("sensorhz");
            if (!TextUtils.isEmpty(sensorhz)) {
                mSharedPreferences.edit().putString("sensorhz", sensorhz).commit();
                mTvSelectSensorHz.setText("传感器采样频率:" + sensorhz);
                sensit = sensorhz;
            }
        }
        if (requestCode == CODE_REQUST_FEATURES_LIST && resultCode == CODE_RESULT_OK) {
            String featureNum = data.getStringExtra("featureNum");
            if (!TextUtils.isEmpty(featureNum)) {
                mTvSelectFeatures.setText("当前特征数:" + featureNum);
            }
        }
    }
}
