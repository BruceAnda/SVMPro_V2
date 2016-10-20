package smv.lovearthstudio.com.svmpro_v2.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import smv.lovearthstudio.com.svmpro_v2.R;
import smv.lovearthstudio.com.svmpro_v2.activity.other.addfilenameactivity.ModelFileNameActivity;
import smv.lovearthstudio.com.svmpro_v2.activity.other.addfilenameactivity.RangeFileNameActivity;
import smv.lovearthstudio.com.svmpro_v2.activity.other.addfilenameactivity.ScaleFileNameActivity;
import smv.lovearthstudio.com.svmpro_v2.activity.other.filenamelistactivity.TrainFileListActivity;
import smv.lovearthstudio.com.svmpro_v2.svmlib.svm_predict;
import smv.lovearthstudio.com.svmpro_v2.svmlib.svm_scale;
import smv.lovearthstudio.com.svmpro_v2.svmlib.svm_train;

import static java.io.File.separator;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_REQUST_MODEL_FILE_NAME;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_REQUST_RANGE_FILE_NAME;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_REQUST_SCALE_FILE_NAME;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_REQUST_TRAIN_FILE_NAME;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_RESULT_OK;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.MODEL_FILE_DIR;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.PREDICT_FILE_DIR;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.RANGE_FILE_DIR;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.SCALE_FILE_DIR;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.TRAIN_FILE_DIR;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainFragment extends Fragment implements View.OnClickListener {

    private View view;
    private TextView mTvSelectTrainFile, mTvSelectRangeFile, mTvSelectSacleFile, mTvSelectModelFile, mTvAccuracy;
    private ImageView mIvTrainStart;
    private String trainFileName = "train";
    private String rangeFileName = "range";
    private String scaleFileName = "scale";
    private String modelFileName = "model";
    private String predictResult = "predictResult";
    private String modelTrainInfo = "modelTrainInfo";
    private String prdictAccuracy = "prdictAccuracy";

    public TrainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_train, container, false);
        init();
        return view;
    }

    private void init() {
        findView();

        setListener();
    }

    private void findView() {
        mTvSelectTrainFile = (TextView) view.findViewById(R.id.tv_select_train_file_name);
        mTvSelectRangeFile = (TextView) view.findViewById(R.id.tv_select_range_file_name);
        mTvSelectSacleFile = (TextView) view.findViewById(R.id.tv_select_sacle_file_name);
        mTvSelectModelFile = (TextView) view.findViewById(R.id.tv_select_model_file_name);
        mTvAccuracy = (TextView) view.findViewById(R.id.tv_accuracy);
        mIvTrainStart = (ImageView) view.findViewById(R.id.iv_train_start);
    }

    private void setListener() {
        mTvSelectTrainFile.setOnClickListener(this);
        mTvSelectRangeFile.setOnClickListener(this);
        mTvSelectSacleFile.setOnClickListener(this);
        mTvSelectModelFile.setOnClickListener(this);
        mIvTrainStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_train_file_name:
                startActivityForResult(new Intent(getActivity(), TrainFileListActivity.class), CODE_REQUST_TRAIN_FILE_NAME);
                break;
            case R.id.tv_select_range_file_name:
                startActivityForResult(new Intent(getActivity(), RangeFileNameActivity.class), CODE_REQUST_RANGE_FILE_NAME);
                break;
            case R.id.tv_select_sacle_file_name:
                startActivityForResult(new Intent(getActivity(), ScaleFileNameActivity.class), CODE_REQUST_SCALE_FILE_NAME);
                break;
            case R.id.tv_select_model_file_name:
                startActivityForResult(new Intent(getActivity(), ModelFileNameActivity.class), CODE_REQUST_MODEL_FILE_NAME);
                break;
            case R.id.iv_train_start:
                new MyTrainTask().execute();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_REQUST_TRAIN_FILE_NAME && resultCode == CODE_RESULT_OK) {
            trainFileName = data.getStringExtra("trainFileName");
            mTvSelectTrainFile.setText("样本文件名称:" + trainFileName);
        }
        if (requestCode == CODE_REQUST_RANGE_FILE_NAME && resultCode == CODE_RESULT_OK) {
            rangeFileName = data.getStringExtra("rangeFileName");
            mTvSelectRangeFile.setText("归一化文件名称:" + rangeFileName);
        }
        if (requestCode == CODE_REQUST_SCALE_FILE_NAME && resultCode == CODE_RESULT_OK) {
            scaleFileName = data.getStringExtra("scaleFileName");
            mTvSelectSacleFile.setText("归一化后的样本文件名称:" + scaleFileName);
        }
        if (requestCode == CODE_REQUST_MODEL_FILE_NAME && resultCode == CODE_RESULT_OK) {
            modelFileName = data.getStringExtra("modelFileName");
            mTvSelectModelFile.setText("模型文件名称:" + modelFileName);
        }
    }

    class MyTrainTask extends AsyncTask<Void, Void, Void> {

        /**
         * 开始执行
         */
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            tarinModel(TRAIN_FILE_DIR + separator + trainFileName,
                    RANGE_FILE_DIR + separator + rangeFileName,
                    SCALE_FILE_DIR + separator + scaleFileName,
                    MODEL_FILE_DIR + separator + modelFileName,
                    PREDICT_FILE_DIR + separator + predictResult,
                    PREDICT_FILE_DIR + separator + modelTrainInfo,
                    PREDICT_FILE_DIR + separator + prdictAccuracy);
            return null;
        }

        /**
         * 执行结束
         *
         * @param aVoid
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(PREDICT_FILE_DIR + separator + prdictAccuracy)));
                String readLine = reader.readLine();
                mTvAccuracy.setText(readLine);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 训练模型
     *
     * @param trainFile
     * @param rangeFile
     * @param scaleFile
     * @param modelFile
     * @param predictResult
     * @param modelTrainInfo
     * @param prdictAccuracy
     */
    public void tarinModel(String trainFile, String rangeFile, String scaleFile, String modelFile, String predictResult, String modelTrainInfo, String prdictAccuracy) {
        creatScaleFile(new String[]{"-l", "0", "-u", "1", "-s", rangeFile, trainFile}, scaleFile);
        creatModelFile(new String[]{"-s", "0", "-c", "128.0", "-t", "2", "-g", "8.0", "-e", "0.1", scaleFile, modelFile}, modelTrainInfo);
        creatPredictFile(new String[]{scaleFile, modelFile, predictResult}, prdictAccuracy);
        //svm_train.main(new String[]{"-s", "0", "-c", "128.0", "-t", "2", "-g", "8.0", "-e", "0.1", scaleFile, modelFile});
        //svm_predict.main(new String[]{scaleFile, modelFile, predictResult});
    }


    /**
     * 训练数据train 进行归一化处理并生生scale文件
     *
     * @param args      String[] args = new String[]{"-l","0","-u","1",path+"/train"};
     * @param scalePath 结果输出文件路径
     */
    private static void creatScaleFile(String[] args, String scalePath) {
        FileOutputStream fileOutputStream = null;
        PrintStream printStream = null;
        try {
            File file = new File(scalePath);
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            printStream = new PrintStream(fileOutputStream);
            // old stream
            PrintStream oldStream = System.out;
            System.setOut(printStream);//重新定义system.out
            svm_scale.main(args);//开始归一化
            System.setOut(oldStream);//回复syste.out
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (printStream != null) {
                    printStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void creatModelFile(String[] args, String outInfo) {
        FileOutputStream fileOutputStream = null;
        PrintStream printStream = null;
        try {
            File file = new File(outInfo);
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            printStream = new PrintStream(fileOutputStream);
            // old stream
            PrintStream oldStream = System.out;
            System.setOut(printStream);//重新定义system.out
            svm_train.main(args);//开始训练模型
            System.setOut(oldStream);//回复syste.out
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (printStream != null) {
                    printStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void creatPredictFile(String[] args, String outInfo) {
        FileOutputStream fileOutputStream = null;
        PrintStream printStream = null;
        try {
            File file = new File(outInfo);
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            printStream = new PrintStream(fileOutputStream);
            // old stream
            PrintStream oldStream = System.out;
            System.setOut(printStream);//重新定义system.out
            svm_predict.main(args);//开始测试精度
            System.setOut(oldStream);//回复syste.out
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (printStream != null) {
                    printStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
