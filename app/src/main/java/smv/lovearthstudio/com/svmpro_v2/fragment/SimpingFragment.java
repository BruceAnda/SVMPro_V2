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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import smv.lovearthstudio.com.svmpro_v2.R;
import smv.lovearthstudio.com.svmpro_v2.activity.other.actionandpositionlist.ActionListActivity;
import smv.lovearthstudio.com.svmpro_v2.activity.other.actionandpositionlist.PositionListActivity;
import smv.lovearthstudio.com.svmpro_v2.activity.other.addfilenameactivity.TrainFileNameActivity;
import smv.lovearthstudio.com.svmpro_v2.model.Training;
import smv.lovearthstudio.com.svmpro_v2.widget.Item;

import static java.io.File.separator;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_REQUST_ACTIOIN;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_REQUST_POSITION;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_REQUST_TRAIN;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_RESULT_OK;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.TRAIN_FILE_DIR;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.dataToFeatures;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.sensit;

/**
 * 采集数据界面
 *
 * @author zhaoliang
 *         create at 16/11/10 下午2:08
 */
public class SimpingFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, SensorEventListener {

    /**
     * TAG,logcat输出使用
     */
    public static final String TAG = SimpingFragment.class.getName();

    /**
     * 界面上的控件
     */
    @BindView(R.id.simping_item_action)
    Item mItemAction;   // action
    @BindView(R.id.simping_item_position)
    Item mItemPosition;   // position
    @BindView(R.id.simping_item_train_name)
    Item mItemTrainName;    // 训练文件的名字
    @BindView(R.id.simping_item_lable)
    Item mItemlabel;        // 标记, 默认是101
    @BindView(R.id.simping_item_acu)
    Item mItemAcu;             // 加速度
    @BindView(R.id.simping_iten_current_train_num)
    Item mItemCurrentTrainNum;      // 当前采样数量
    @BindView(R.id.simping_item_total_train_num)
    Item mItemTotalTrainNum;        // 采样总数
    @BindView(R.id.sb_simple_num)
    SeekBar mSbNum;                 // 滑动条
    @BindView(R.id.iv_simple_start)
    ImageView mIvStart;             // 开始采样按钮

    @OnClick({R.id.simping_item_action, R.id.simping_item_position, R.id.simping_item_train_name, R.id.iv_simple_start})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.simping_item_action:     // 选择Action
                startActivityForResult(new Intent(getActivity(), ActionListActivity.class), CODE_REQUST_ACTIOIN);
                break;
            case R.id.simping_item_position:       // 选择Position
                startActivityForResult(new Intent(getActivity(), PositionListActivity.class), CODE_REQUST_POSITION);
                break;
            case R.id.simping_item_train_name:          // 选择Train文件名字
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
        View view = inflater.inflate(R.layout.fragment_simping, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    /**
     * 初始化的一些操作
     */
    private void init() {
        mSbNum.setProgress(100);
        mSbNum.setOnSeekBarChangeListener(this);

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        prepareFile();

        realm = Realm.getDefaultInstance();
    }

    /**
     * 准备文件
     */
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

    boolean isStart;

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
        mItemTotalTrainNum.setText("拖动设置采样数量:当前" + progress);
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
                mItemAction.setText("action:" + actionStr);
            }
        }
        if (requestCode == CODE_REQUST_POSITION && resultCode == CODE_RESULT_OK) {
            mPostion = data.getIntExtra("position", 1);
            String postionStr = data.getStringExtra("positionStr");
            if (!TextUtils.isEmpty(postionStr)) {
                mItemPosition.setText("position:" + postionStr);
            }
        }
        if (requestCode == CODE_REQUST_TRAIN && resultCode == CODE_RESULT_OK) {
            String trainFileName = data.getStringExtra("trainFileName");
            if (!TextUtils.isEmpty(trainFileName)) {
                mTrainFileName = trainFileName;
                mItemTrainName.setText("样本文件名称:" + trainFileName);
                prepareFile();
            }
        }
        label = mAction * 100 + mPostion;
        mItemlabel.setText("当前label:" + label);
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
        mItemAcu.setText("加速度:" + a);
        mItemCurrentTrainNum.setText("当前采样数量:" + mCurrenTrainNum);
        if (mCurrenTrainNum >= mTrainNum) {
            stopSimping();
        } else {
            if (currrenIndex >= num) {
                String[] features = dataToFeatures(data, (int) (1000 * 1000 / Double.parseDouble(sensit.replace("HZ", ""))));
                saveToRealm(data);
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

    private Realm realm;

    private void saveToRealm(final double[] values) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (double value : values) {
            stringBuilder.append(value + " ");
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Training training = realm.createObject(Training.class);
                training.setValues(stringBuilder.toString());
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
