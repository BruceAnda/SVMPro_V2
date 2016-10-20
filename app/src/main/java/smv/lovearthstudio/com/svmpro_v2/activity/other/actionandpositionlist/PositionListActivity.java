package smv.lovearthstudio.com.svmpro_v2.activity.other.actionandpositionlist;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import smv.lovearthstudio.com.svmpro_v2.R;

import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_RESULT_OK;

public class PositionListActivity extends ListActivity {

    private String[] positions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        positions = getResources().getStringArray(R.array.Positions);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, positions));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("position", position + 1);
        intent.putExtra("positionStr", positions[position]);
        setResult(CODE_RESULT_OK, intent);
        finish();
    }
}
