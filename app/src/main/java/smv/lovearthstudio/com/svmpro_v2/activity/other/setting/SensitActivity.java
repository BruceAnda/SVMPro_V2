package smv.lovearthstudio.com.svmpro_v2.activity.other.setting;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import smv.lovearthstudio.com.svmpro_v2.R;

import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_RESULT_OK;

public class SensitActivity extends ListActivity {

    private String[] sersorHZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sersorHZ = getResources().getStringArray(R.array.SensorHZ);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sersorHZ));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("sensorhz", sersorHZ[position]);
        setResult(CODE_RESULT_OK, intent);
        finish();
    }
}
