package smv.lovearthstudio.com.svmpro_v2.fragment;


import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import smv.lovearthstudio.com.svmpro_v2.R;
import smv.lovearthstudio.com.svmpro_v2.activity.other.filenamelistactivity.ModelFileListActivity;
import smv.lovearthstudio.com.svmpro_v2.activity.other.filenamelistactivity.RangeFileListActivity;
import smv.lovearthstudio.com.svmpro_v2.util.Features;
import smv.lovearthstudio.com.svmpro_v2.widget.Item;

import static java.io.File.separator;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_REQUST_MODEL_FILE_LIST;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_REQUST_RANGE_FILE_LIST;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_RESULT_OK;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.MODEL_FILE_DIR;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.RANGE_FILE_DIR;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.dataToFeatures;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.sensit;

/**
 * 预测页面
 *
 * @author zhaoliang
 *         create at 2016/11/11 下午2:18
 */
public class PredictFragment extends Fragment implements SensorEventListener {

    private String mModelFileName = "model";
    private String mRangeFileName = "range";

    @BindView(R.id.predict_item_model_file_name)
    Item mItemModelFileName;
    @BindView(R.id.predict_item_range_file_name)
    Item mItemRangeFileName;
    @BindView(R.id.predict_item_action)
    Item mItemAction;
    @BindView(R.id.predict_item_position)
    Item mItemPosition;
    @BindView(R.id.predict_item_acc)
    Item mItemAcc;
    @BindView(R.id.iv_start_predict)
    ImageView mIvStartPredict;

    @OnClick({R.id.predict_item_model_file_name, R.id.predict_item_range_file_name, R.id.iv_start_predict})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.predict_item_model_file_name:
                startActivityForResult(new Intent(getActivity(), ModelFileListActivity.class), CODE_REQUST_MODEL_FILE_LIST);
                break;
            case R.id.predict_item_range_file_name:
                startActivityForResult(new Intent(getActivity(), RangeFileListActivity.class), CODE_REQUST_RANGE_FILE_LIST);
                break;
            case R.id.iv_start_predict:
                isStartPredict = !isStartPredict;
                if (isStartPredict) {
                    startPredict();
                } else {
                    stopPredict();
                }
                break;

        }
    }

    /**
     * 传感器管理器类
     */
    SensorManager mSensorManager;
    private String[] mActionsArr;
    private String[] mPositionArr;

    public PredictFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_predict, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        readActionAndPostionArr();
    }

    private void readActionAndPostionArr() {
        mActionsArr = getResources().getStringArray(R.array.Actions);
        mPositionArr = getResources().getStringArray(R.array.Positions);
    }

    boolean isStartPredict;


    /**
     * 开始预测
     */
    private void startPredict() {
        mIvStartPredict.setImageResource(R.mipmap.stop);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), (int) (1000 * 1000 / Double.parseDouble(sensit.replace("HZ", ""))));
        readRange();
        readModel();
    }

    private svm_model mSvmModel;

    private void readModel() {
        try {
            mSvmModel = svm.svm_load_model(new BufferedReader(new InputStreamReader(new FileInputStream(MODEL_FILE_DIR + separator + mModelFileName))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    List<String> range = new ArrayList<>();

    /**
     * 读取range文件,做归一化操作
     */
    private void readRange() {
        range.clear();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(RANGE_FILE_DIR + separator + mRangeFileName)));
            String msg = null;
            while ((msg = bufferedReader.readLine()) != null) {
                range.add(msg);
            }
            readFeaturesRange();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    double[][] mFeaturesRange;
    double mScaleLower, mScaleUpper; // 归一化最大值和最小值[0, 1]规划
    int mFeaturesCount;

    /**
     * 读取特征值的范围
     */
    private void readFeaturesRange() {
        mFeaturesCount = range.size() - 2;
        mFeaturesRange = new double[mFeaturesCount][2];
        String lowerAndUpper = range.get(1);
        String[] split = lowerAndUpper.split(" ");
        mScaleLower = Double.parseDouble(split[0]);
        mScaleUpper = Double.parseDouble(split[1]);
        for (int i = 0; i < mFeaturesCount; i++) {
            String[] featuresLowerAndUpper = range.get(i + 2).split(" ");
            mFeaturesRange[i][0] = Double.parseDouble(featuresLowerAndUpper[1]);
            mFeaturesRange[i][1] = Double.parseDouble(featuresLowerAndUpper[2]);
        }
    }

    /**
     * 停止预测
     */
    private void stopPredict() {
        mIvStartPredict.setImageResource(R.mipmap.play);
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_REQUST_MODEL_FILE_LIST && resultCode == CODE_RESULT_OK) {
            String modelFileName = data.getStringExtra("modelFileName");
            if (!TextUtils.isEmpty(modelFileName)) {
                mItemModelFileName.setText("model文件名称:" + modelFileName);
                mModelFileName = modelFileName;
            }
        }
        if (requestCode == CODE_REQUST_RANGE_FILE_LIST && resultCode == CODE_RESULT_OK) {
            String rangeFileName = data.getStringExtra("rangeFileName");
            if (!TextUtils.isEmpty(rangeFileName)) {
                mItemRangeFileName.setText("归一化文件名称:" + rangeFileName);
                mRangeFileName = rangeFileName;
            }
        }
    }

    int num = 128;
    int currrenIndex = 0;
    double[] data = new double[num];

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        double a = Math.sqrt(x * x + y * y + z * z);
        mItemAcc.setText("加速度:" + a);
        if (currrenIndex >= num) {
            String[] features = dataToFeatures(data, (int) (1000 * 1000 / Double.parseDouble(sensit.replace("HZ", ""))));
            double code = predictUnscaleData(features);
            int act = (int) (code / 100);
            int postion = (int) (code - act * 100);
            mItemAction.setText("action:" + mActionsArr[act - 1]);
            mItemPosition.setText("position:" + mPositionArr[postion - 1]);
            currrenIndex = 0;
        }
        data[currrenIndex++] = a;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 预测没有归一化的数据
     *
     * @param unScaleData
     * @return
     */
    public double predictUnscaleData(String[] unScaleData) {
        svm_node[] px = new svm_node[mFeaturesCount];
        String[] tempNode;
        svm_node p;
        for (int i = 0; i < mFeaturesCount; i++) {
            tempNode = unScaleData[i].split(":");
            p = new svm_node();
            p.index = Integer.parseInt(tempNode[0]);
            p.value = Features.zeroOneLibSvm(mScaleLower, mScaleUpper, Double.parseDouble(tempNode[1]), mFeaturesRange[i][0], mFeaturesRange[i][1]);
            px[i] = p;
        }
        double v = svm.svm_predict(mSvmModel, px);
        System.out.println("-------code:" + v);
        return v;
    }
}
