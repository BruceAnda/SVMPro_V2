package smv.lovearthstudio.com.svmpro_v2.activity.other.addfilenameactivity.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import smv.lovearthstudio.com.svmpro_v2.R;

/**
 * 添加文件名称的基类,公共的代码写在这里边
 */
public abstract class BaseAddFileNameActivity extends Activity implements AdapterView.OnItemClickListener {

    protected EditText mEtFileName;
    protected ListView mFileNameList;
    protected SharedPreferences mSharedPreferences;
    protected String mFileNamesStr;
    protected String[] mFileNameArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_add_file_name);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        initSharedPerferences();
        findView();
        setData();
        setListener();
    }

    /**
     * 初始化SharedPerferences
     */
    protected abstract void initSharedPerferences();

    /**
     * 查找控件
     */
    private void findView() {
        mEtFileName = (EditText) findViewById(R.id.et_file_name);
        mFileNameList = (ListView) findViewById(R.id.file_name_list);
    }

    /**
     * 设置数据
     */
    public void setData() {
        mFileNameArr = mFileNamesStr.split(",");
        mFileNameList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mFileNameArr));
    }

    /**
     * 设置事件
     */
    private void setListener() {
        mFileNameList.setOnItemClickListener(this);
    }

    public abstract void add(View view);

}
