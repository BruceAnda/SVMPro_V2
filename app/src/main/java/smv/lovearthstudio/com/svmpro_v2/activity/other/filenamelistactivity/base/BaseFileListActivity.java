package smv.lovearthstudio.com.svmpro_v2.activity.other.filenamelistactivity.base;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class BaseFileListActivity extends ListActivity {

    protected List<String> mFileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 递归列出当前目录
     *
     * @param f
     */
    public void listFile(File f) {
        if (f != null) {
            if (f.isDirectory()) {
                File[] fileArray = f.listFiles();
                if (fileArray != null) {
                    for (int i = 0; i < fileArray.length; i++) {
                        //递归调用
                        listFile(fileArray[i]);
                    }
                }
            } else {
                mFileList.add(f.getName());
            }
        }

    }

    public void showData(String dir) {
        listFile(new File(dir));
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mFileList));
    }
}
