package smv.lovearthstudio.com.svmpro_v2.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import smv.lovearthstudio.com.svmpro_v2.R;
import smv.lovearthstudio.com.svmpro_v2.model.Training;
import smv.lovearthstudio.com.svmpro_v2.util.Features;

/**
 * A simple {@link Fragment} subclass.
 */
public class FFTFragment extends Fragment {

    private Realm realm;
    private RealmResults<Training> trainings;
    @BindView(R.id.fft_list)
    ListView mFftList;
    private MyListAdapter adapter;

    public FFTFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fft, container, false);
        ButterKnife.bind(this, view);
        initList();
        return view;
    }

    private void initList() {
        realm = Realm.getDefaultInstance();
        trainings = realm.where(Training.class).findAll();
        adapter = new MyListAdapter(getActivity(), trainings);
        mFftList.setAdapter(adapter);
        mFftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(getActivity()).setTitle("提示！").setMessage("确定要删除本条数据吗？").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.where(Training.class).equalTo("values", adapter.getItem(position).getValues()).findAll().deleteAllFromRealm();
                            }
                        });
                    }
                }).show();
            }
        });
    }

    class MyListAdapter extends RealmBaseAdapter<Training> implements ListAdapter {

        public MyListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Training> data) {
            super(context, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.fft_chart, null);
                viewHolder = new ViewHolder();

                viewHolder.lineChart = (LineChart) convertView.findViewById(R.id.chart);
                initLineChart(viewHolder);

                viewHolder.fftBarChart = (BarChart) convertView.findViewById(R.id.fft_chart);
                initFftBarChart(viewHolder);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Training training = adapterData.get(position);

            double[] values = valuesToArray(training.getValues());

            //setData(viewHolder.lineChart, Features.fft(values));
            setData(viewHolder.lineChart, values);
            setFftData(viewHolder.fftBarChart, Features.fft(values));

            return convertView;
        }

        private void initLineChart(ViewHolder viewHolder) {
            Description desc = new Description();
            desc.setText("FFT图");
            viewHolder.lineChart.setDescription(desc);
            viewHolder.lineChart.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.lineChart.setTouchEnabled(false);
            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            viewHolder.lineChart.setMaxVisibleValueCount(60);

            // scaling can now only be done on x- and y-axis separately
            viewHolder.lineChart.setPinchZoom(false);

            // if disabled, scaling can be done on x- and y-axis separately
            viewHolder.lineChart.setPinchZoom(false);

            // set an alternative background color
            viewHolder.lineChart.setDrawGridBackground(false);

            YAxis leftAxis = viewHolder.lineChart.getAxisLeft();
            leftAxis.setDrawLabels(true);
            leftAxis.setDrawGridLines(false);
            leftAxis.setGranularityEnabled(false);

            XAxis xAxis = viewHolder.lineChart.getXAxis();
            xAxis.setLabelCount(9);
            xAxis.setTextSize(11f);
            xAxis.setTextColor(Color.WHITE);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);

            viewHolder.lineChart.getAxisRight().setEnabled(false);

            viewHolder.lineChart.setViewPortOffsets(10, 0, 10, 0);

            viewHolder.lineChart.getLegend().setEnabled(false);
        }


        private void initFftBarChart(ViewHolder viewHolder) {
            Description desc = new Description();
            desc.setText("FFT图");
            viewHolder.fftBarChart.setDescription(desc);
            viewHolder.fftBarChart.setTouchEnabled(false);
            viewHolder.fftBarChart.setBackgroundColor(Color.TRANSPARENT);
            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            viewHolder.fftBarChart.setMaxVisibleValueCount(60);

            // scaling can now only be done on x- and y-axis separately
            viewHolder.fftBarChart.setPinchZoom(false);

            // if disabled, scaling can be done on x- and y-axis separately
            viewHolder.fftBarChart.setPinchZoom(false);

            // set an alternative background color
            viewHolder.fftBarChart.setDrawGridBackground(false);

            YAxis leftAxis = viewHolder.fftBarChart.getAxisLeft();
            leftAxis.setDrawLabels(true);
            leftAxis.setDrawGridLines(false);
            leftAxis.setGranularityEnabled(false);

            XAxis xAxis = viewHolder.fftBarChart.getXAxis();
            xAxis.setTextSize(11f);
            xAxis.setTextColor(Color.WHITE);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);

            viewHolder.fftBarChart.getAxisRight().setEnabled(false);

            viewHolder.fftBarChart.setViewPortOffsets(10, 0, 10, 0);

            viewHolder.fftBarChart.getLegend().setEnabled(false);
        }

        private double[] valuesToArray(String values) {
            String[] split = values.split(" ");
            double[] result = new double[split.length];
            for (int i = 0; i < result.length; i++) {
                result[i] = Double.parseDouble(split[i]);
            }
            return result;
        }

        class ViewHolder {
            LineChart lineChart;
            BarChart fftBarChart;
        }

        private void setData(LineChart mChart, double[] datas) {

            ArrayList<Entry> values = new ArrayList<Entry>();

            for (int i = 0; i < datas.length; i++) {
                values.add(new Entry(i, (float) datas[i]));
            }

            LineDataSet set1;

            if (mChart.getData() != null &&
                    mChart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                set1.setValues(values);
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
            } else {
                // create a dataset and give it a type
                set1 = new LineDataSet(values, "DataSet 1");

                // set the line to be drawn like this "- - - - - -"
                set1.enableDashedLine(10f, 5f, 0f);
                set1.enableDashedHighlightLine(10f, 5f, 0f);
                set1.setColor(Color.BLACK);
                set1.setCircleColor(Color.BLACK);
                set1.setLineWidth(1f);
                set1.setCircleRadius(3f);
                set1.setDrawCircleHole(false);
                set1.setValueTextSize(9f);
                set1.setDrawFilled(true);
                //set1.setFormLineWidth(1f);
                // set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                set1.setFormSize(15.f);

                ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(set1); // add the datasets

                // create a data object with the datasets
                LineData data = new LineData(dataSets);

                // set data
                mChart.setData(data);
            }
        }
    }

    public void setFftData(BarChart mChart, double[] datas) {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < datas.length; i++) {
            yVals1.add(new BarEntry(i, (float) datas[i]));
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "The year 2017");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            mChart.setData(data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
