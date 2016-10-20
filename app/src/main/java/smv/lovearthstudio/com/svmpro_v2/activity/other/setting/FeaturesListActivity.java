package smv.lovearthstudio.com.svmpro_v2.activity.other.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import smv.lovearthstudio.com.svmpro_v2.R;

import static smv.lovearthstudio.com.svmpro_v2.util.Util.CODE_RESULT_OK;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.FILE_SHAREDPERFERENCES;
import static smv.lovearthstudio.com.svmpro_v2.util.Util.selectFeatures;

public class FeaturesListActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView mTvShowSelectNum;
    private Button mBtnSelectAll, mBtnCancelSelectAll, mBtnDeleteAll, mBtnFinish;
    private ListView mFreaturesList;
    String[] mFreatures;
    LayoutInflater inflater;
    private int currentCheckNum;
    private SharedPreferences mSharedPreferences;
    private String featuresSelected;
    private MyFeaturesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features_list);
        mFreatures = getResources().getStringArray(R.array.Features);
        inflater = LayoutInflater.from(this);
        init();
    }

    private void init() {
        findView();
        initSharedPreferences();
        setListener();
        setData();
    }

    private void initSharedPreferences() {
        mSharedPreferences = getSharedPreferences(FILE_SHAREDPERFERENCES, MODE_PRIVATE);
        featuresSelected = mSharedPreferences.getString("featuresSelected", "");
        String[] split = featuresSelected.split(",");
        for (String s : split) {
            String[] feature = s.split(":");
            selectFeatures.put(Integer.parseInt(feature[0]), Boolean.parseBoolean(feature[1]));
        }
        for (int i = 0; i < selectFeatures.size(); i++) {
            if (selectFeatures.get(i)) {
                currentCheckNum++;
            }
        }
    }

    private void findView() {
        mTvShowSelectNum = (TextView) findViewById(R.id.tv_show_select_num);
        mBtnSelectAll = (Button) findViewById(R.id.btn_select_all);
        mBtnCancelSelectAll = (Button) findViewById(R.id.btn_cancel_select_all);
        mBtnDeleteAll = (Button) findViewById(R.id.btn_delete_select_all);
        mBtnFinish = (Button) findViewById(R.id.btn_finish);
        mFreaturesList = (ListView) findViewById(R.id.features_list);
    }

    private void setListener() {
        mBtnSelectAll.setOnClickListener(this);
        mBtnCancelSelectAll.setOnClickListener(this);
        mBtnDeleteAll.setOnClickListener(this);
        mBtnFinish.setOnClickListener(this);
        mFreaturesList.setOnItemClickListener(this);
    }

    private void setData() {
        mTvShowSelectNum.setText("已选中" + currentCheckNum + "项");
        adapter = new MyFeaturesAdapter();
        mFreaturesList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select_all:
                selectAll();
                break;
            case R.id.btn_cancel_select_all:
                cancelSelectAll();
                break;
            case R.id.btn_delete_select_all:
                deleteSelectAll();
                break;
            case R.id.btn_finish:
                back();
                break;
        }
    }

    private void selectAll() {
        for (int i = 0; i < 19; i++) {
            selectFeatures.put(i, true);
            dataChange();
            currentCheckNum = selectFeatures.size();
        }
    }

    private void dataChange() {
        mTvShowSelectNum.setText("已选中" + currentCheckNum + "项");
        adapter.notifyDataSetChanged();
    }

    private void cancelSelectAll() {
        for (int i = 0; i < 19; i++) {
            //mSelectFeatures.put(i, !mSelectFeatures.get(i));
            if (selectFeatures.get(i)) {
                currentCheckNum--;
            } else {
                currentCheckNum++;
            }
            selectFeatures.put(i, !selectFeatures.get(i));
            dataChange();
        }
    }

    private void deleteSelectAll() {
        for (int i = 0; i < 19; i++) {
            selectFeatures.put(i, false);
            currentCheckNum = 0;
            dataChange();
        }
    }

    private void back() {
        saveToSharedPreferences();
        Intent intent = new Intent();
        intent.putExtra("featureNum", String.valueOf(currentCheckNum));
        setResult(CODE_RESULT_OK, intent);
        finish();
    }

    private void saveToSharedPreferences() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < selectFeatures.size(); i++) {
            stringBuffer.append(i + ":" + selectFeatures.get(i) + ",");
        }
        featuresSelected = stringBuffer.substring(0, stringBuffer.lastIndexOf(","));
        mSharedPreferences.edit().putString("featuresSelected", featuresSelected).commit();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.cbFeature.toggle();
        selectFeatures.put(position, holder.cbFeature.isChecked());
        if (holder.cbFeature.isChecked()) {
            currentCheckNum++;
        } else {
            currentCheckNum--;
        }
        mTvShowSelectNum.setText("已选中" + currentCheckNum + "项");
    }

    class MyFeaturesAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mFreatures.length;
        }

        @Override
        public String getItem(int position) {
            return mFreatures[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.features_list_item, null);
                viewHolder.tvFeature = (TextView) convertView.findViewById(R.id.tv_feature);
                viewHolder.cbFeature = (CheckBox) convertView.findViewById(R.id.cb_feature);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvFeature.setText(mFreatures[position]);
            viewHolder.cbFeature.setChecked(selectFeatures.get(position));
            return convertView;
        }

    }

    class ViewHolder {
        TextView tvFeature;
        CheckBox cbFeature;
    }
}
