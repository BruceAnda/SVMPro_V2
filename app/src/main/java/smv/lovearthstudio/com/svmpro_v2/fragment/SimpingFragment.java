package smv.lovearthstudio.com.svmpro_v2.fragment;


import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import smv.lovearthstudio.com.svmpro_v2.R;
import smv.lovearthstudio.com.svmpro_v2.activity.other.actionandpositionlist.ActionListActivity;
import smv.lovearthstudio.com.svmpro_v2.activity.other.actionandpositionlist.PositionListActivity;
import smv.lovearthstudio.com.svmpro_v2.activity.other.addfilenameactivity.TrainFileNameActivity;

import static java.io.File.separator;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_REQUST_ACTIOIN;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_REQUST_POSITION;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_REQUST_TRAIN;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_RESULT_OK;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.TRAIN_FILE_DIR;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.dataToFeatures;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.sensit;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimpingFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, SensorEventListener {

    /**
     * TAG,logcat输出使用
     */
    public static final String TAG = SimpingFragment.class.getName();

    /**
     * view和控件
     */
    private View view;
    private TextView mTvSimpleAction, mTvSimplePosition, mTvSimpleTrain, mTvSimpleNum, mTvResult, mTvTrainNum;
    private SeekBar mSbNum;
    private ImageView mIvStart;

    /**
     * lable,和样本文件名称
     */
    private int mAction = 1, mPostion = 1, mTrainNum = 100, label = 101, mCurrenTrainNum = 0;
    private String mTrainFileName = "train";

    /**
     * 传感器管理器类
     */
    SensorManager mSensorManager;
    private RandomAccessFile mTrainFile;


    public SimpingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_simping, container, false);
        init();
        return view;
    }

    /**
     * 初始化的一些操作
     */
    private void init() {
        findView();

        setListener();

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        prepareFile();
    }

    private void prepareFile() {
        try {
            mTrainFile = new RandomAccessFile(TRAIN_FILE_DIR + separator + mTrainFileName, "rwd");
            mTrainFile.seek(mTrainFile.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找控件
     */
    private void findView() {
        mTvSimpleAction = (TextView) view.findViewById(R.id.tv_simple_action);
        mTvSimplePosition = (TextView) view.findViewById(R.id.tv_simple_position);
        mTvSimpleTrain = (TextView) view.findViewById(R.id.tv_simple_train);
        mTvSimpleNum = (TextView) view.findViewById(R.id.tv_simple_num);
        mSbNum = (SeekBar) view.findViewById(R.id.sb_simple_num);
        mSbNum.setProgress(100);
        mTvResult = (TextView) view.findViewById(R.id.tv_result);
        mTvTrainNum = (TextView) view.findViewById(R.id.tv_train_num);
        mIvStart = (ImageView) view.findViewById(R.id.iv_simple_start);
    }

    /**
     * 设置交互事件监听
     */
    private void setListener() {
        mTvSimpleAction.setOnClickListener(this);
        mTvSimplePosition.setOnClickListener(this);
        mTvSimpleTrain.setOnClickListener(this);
        mSbNum.setOnSeekBarChangeListener(this);
        mIvStart.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    boolean isStart;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_simple_action:     // 选择Action
                startActivityForResult(new Intent(getActivity(), ActionListActivity.class), CODE_REQUST_ACTIOIN);
                break;
            case R.id.tv_simple_position:       // 选择Position
                startActivityForResult(new Intent(getActivity(), PositionListActivity.class), CODE_REQUST_POSITION);
                break;
            case R.id.tv_simple_train:          // 选择Train文件名字
                startActivityForResult(new Intent(getActivity(), TrainFileNameActivity.class), CODE_REQUST_TRAIN);
                break;
            case R.id.iv_simple_start:          // 开始采集样本
                isStart = !isStart;
                if (isStart) {
                    startSimping();
                } else {
                    stopSimping();
                }
                break;
        }
    }

    /**
     * 开始采样
     */
    private void startSimping() {
        mIvStart.setImageResource(R.mipmap.stop);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), (int) (1000 * 1000 / Double.parseDouble(sensit.replace("HZ", ""))));
    }

    /**
     * 停止采样
     */
    private void stopSimping() {
        mCurrenTrainNum = 0;
        mIvStart.setImageResource(R.mipmap.play);
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mTvSimpleNum.setText("拖动设置采样数量:当前" + progress);
        mTrainNum = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
        if (requestCode == CODE_REQUST_ACTIOIN && resultCode == CODE_RESULT_OK) {
            mAction = data.getIntExtra("action", 1);
            String actionStr = data.getStringExtra("actionStr");
            if (!TextUtils.isEmpty(actionStr)) {
                mTvSimpleAction.setText("action:" + actionStr);
            }
        }
        if (requestCode == CODE_REQUST_POSITION && resultCode == CODE_RESULT_OK) {
            mPostion = data.getIntExtra("position", 1);
            String postionStr = data.getStringExtra("positionStr");
            if (!TextUtils.isEmpty(postionStr)) {
                mTvSimplePosition.setText("position:" + postionStr);
            }
        }
        if (requestCode == CODE_REQUST_TRAIN && resultCode == CODE_RESULT_OK) {
            String trainFileName = data.getStringExtra("trainFileName");
            if (!TextUtils.isEmpty(trainFileName)) {
                mTrainFileName = trainFileName;
                mTvSimpleTrain.setText("样本文件名称:" + trainFileName);
                prepareFile();
            }
        }
        label = mAction * 100 + mPostion;
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
        mTvResult.setText("label:" + label + "=====加速度:" + a);
        mTvTrainNum.setText("当前采样数量:" + mCurrenTrainNum);
        if (mCurrenTrainNum >= mTrainNum) {
            stopSimping();
        } else {
            if (currrenIndex >= num) {
                String[] features = dataToFeatures(data, (int) (1000 * 1000 / Double.parseDouble(sensit.replace("HZ", ""))));
                saveToFile(features);
                currrenIndex = 0;
                mCurrenTrainNum++;
            }
        }
        data[currrenIndex++] = a;
    }

    /**
     * 保存特征值保存到文件中
     *
     * @param features
     */
    private void saveToFile(String[] features) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(label);
        for (String feature : features) {
            stringBuilder.append(" " + feature);
        }
        stringBuilder.append("\n");
        try {
            mTrainFile.writeBytes(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
