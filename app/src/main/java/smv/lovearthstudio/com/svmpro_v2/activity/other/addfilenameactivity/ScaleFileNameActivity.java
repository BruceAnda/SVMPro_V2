package smv.lovearthstudio.com.svmpro_v2.activity.other.addfilenameactivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import smv.lovearthstudio.com.svmpro_v2.activity.other.addfilenameactivity.base.BaseAddFileNameActivity;

import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_RESULT_OK;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.FILE_SHAREDPERFERENCES;

public class ScaleFileNameActivity extends BaseAddFileNameActivity {

    private String scaleFileNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initSharedPerferences() {
        mSharedPreferences = getSharedPreferences(FILE_SHAREDPERFERENCES, MODE_PRIVATE);
        scaleFileNames = "scaleFileNames";
        mFileNamesStr = mSharedPreferences.getString(scaleFileNames, "");
        if (!mFileNamesStr.contains("scale")) {
            mSharedPreferences.edit().putString(scaleFileNames, "scale,").commit();
            mFileNamesStr = mSharedPreferences.getString(scaleFileNames, "");
        }
    }

    @Override
    public void add(View view) {
        String fileName = mEtFileName.getText().toString().trim();
        if (!TextUtils.isEmpty(fileName)) {
            mFileNamesStr += (fileName + ",");
            mSharedPreferences.edit().putString(scaleFileNames, mFileNamesStr).commit();
            setData();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("scaleFileName", mFileNameArr[position]);
        setResult(CODE_RESULT_OK, intent);
        finish();
    }
}
