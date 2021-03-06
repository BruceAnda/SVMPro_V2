package smv.lovearthstudio.com.svmpro_v2.activity.other.filenamelistactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import smv.lovearthstudio.com.svmpro_v2.activity.other.filenamelistactivity.base.BaseFileListActivity;

import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_RESULT_OK;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.RANGE_FILE_DIR;

public class RangeFileListActivity extends BaseFileListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showData(RANGE_FILE_DIR);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("rangeFileName", mFileList.get(position));
        setResult(CODE_RESULT_OK, intent);
        finish();
    }
}
