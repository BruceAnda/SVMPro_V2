package smv.lovearthstudio.com.svmpro_v2.activity.other.actionandpositionlist;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import smv.lovearthstudio.com.svmpro_v2.R;

import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_RESULT_OK;

/**
 * 选择Action的列表
 */
public class ActionListActivity extends ListActivity {

    private String[] mActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActions = getResources().getStringArray(R.array.Actions);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mActions));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("action", position + 1);    // position 是从0开始的
        intent.putExtra("actionStr", mActions[position]);
        setResult(CODE_RESULT_OK, intent);
        finish();
    }
}
