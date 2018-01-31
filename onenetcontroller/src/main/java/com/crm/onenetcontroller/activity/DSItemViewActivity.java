package com.crm.onenetcontroller.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;

import com.chinamobile.iot.onenet.module.Command;
import com.chinamobile.iot.onenet.module.DataPoint;
import com.crm.mylibrary.activity.base.BaseActivity;
import com.crm.mylibrary.contract.BaseContract;
import com.crm.mylibrary.rxbus.Events;
import com.crm.onenetcontroller.R;
import com.crm.onenetcontroller.charts.MyMarkerView;
import com.crm.onenetcontroller.databinding.ActivityDeviceBinding;
import com.crm.onenetcontroller.databinding.ActivityDsitemViewBinding;
import com.crm.onenetcontroller.onenet.APIService;
import com.crm.onenetcontroller.onenet.DSItem;
import com.crm.onenetcontroller.onenet.DataPointData;
import com.crm.onenetcontroller.onenet.DataPointItem;
import com.crm.onenetcontroller.onenet.DataStream;
import com.crm.onenetcontroller.onenet.OneNetRetrofitUtils;
import com.crm.onenetcontroller.onenet.ResData;
import com.crm.onenetcontroller.onenet.utils.GsonConvertUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;

import org.xutils.ex.DbException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class DSItemViewActivity extends BaseActivity {

    private ActivityDsitemViewBinding mBinding;

    private String deviceId;

    @Override
    protected void rxBusEventProcess(@NonNull Events events) throws Exception {

    }

    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) throws DbException {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dsitem_view);
        Intent intent = getIntent();
        DSItem dsItem = (DSItem)intent.getSerializableExtra("data0");
        this.deviceId = (String)intent.getSerializableExtra("data1");
        Map<String, String> params = new HashMap<>();
        params.put("datastream_id", dsItem.getId());
//        params.put("end", new Date().toString());
//        params.put("duration", "3600");
        params.put("limit", "10");
        String url = DataPoint.urlForQuerying(deviceId, params);
        Observable<ResponseBody> observable = OneNetRetrofitUtils.get().getRetrofit().create(APIService.class).getByUrl(url);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull ResponseBody responseBody) throws Exception {
                        DataPointData dataPointData = GsonConvertUtil.convert(responseBody.string(), DataPointData.class);

                        List<Entry> entries = new ArrayList<Entry>();

                        for(DataStream dataStream : dataPointData.getDatastreams()) {
                            for(int i=0;i<dataStream.getDatapoints().size();i++) {
                                DataPointItem dataPointItem = dataStream.getDatapoints().get(i);
                                java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                entries.add(new Entry(i, dataPointItem.getValue(), dateFormat.parse(dataPointItem.getAt())));
                            }
                        }

                        initLineChart(entries);
                    }
                });

        mBinding.btnLedoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand("led off");
            }
        });

        mBinding.btnControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = mBinding.etControlText.getText().toString();
                sendCommand(txt);
            }
        });

    }

    private void sendCommand(String cmd) {
        Map<String, String> map = new HashMap<>();
        map.put("cmd", cmd);

        String url = Command.urlForSending(this.deviceId);
        Observable<ResponseBody> observable = OneNetRetrofitUtils.get().getRetrofit().create(APIService.class).postByUrl(url, map);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull ResponseBody responseBody) throws Exception {

                    }
                });
    }

    private void initLineChart(List<Entry> entries) {
        LineDataSet dataSet = new LineDataSet(entries, "温度"); // add entries to dataset
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.RED); // styling, ...
        dataSet.setFillAlpha(150);
        dataSet.setFillColor(Color.BLUE);
        dataSet.setDrawFilled(true);

        dataSet.setValueTextSize(20);
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                return String.valueOf(value);
            }
        });

        LineData lineData = new LineData(dataSet);
        mBinding.chart.setData(lineData);


        XAxis xAxis = mBinding.chart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return String.valueOf(v);
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        mBinding.chart.setMarker(new MyMarkerView(DSItemViewActivity.this, R.layout.chart_marker_view));

        Matrix m=new Matrix();
        m.postScale(1.5f, 1f);//两个参数分别是x,y轴的缩放比例。例如：将x轴的数据放大为之前的1.5倍
        mBinding.chart.getViewPortHandler().refresh(m, mBinding.chart, false);//将图表动画显示之前进行缩放

        mBinding.chart.animateX(500); // 立即执行的动画,x轴
        mBinding.chart.invalidate(); // refresh
    }

    @Override
    protected BaseContract.IBasePresenter getP() {
        return null;
    }
}
