package com.crm.onenetcontroller.charts;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.crm.onenetcontroller.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MyMarkerView extends MarkerView {

    TextView tv_indicator;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tv_indicator = (TextView) findViewById(R.id.chart_marker_content);
        tv_indicator.setTextSize(15);
    }

    private boolean isReverse = true;

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        isReverse = !(highlight.getX() > 6);
        String content = "时间：\n";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        content += dateFormat.format(e.getData());
        tv_indicator.setText(content);
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        MPPointF mpPointF = super.getOffset();
        Log.i("iiiiiiiii", mpPointF.x + " : "+ mpPointF.y);
        if (!isReverse ) {
            mpPointF.x = -tv_indicator.getWidth();
        } else {
            mpPointF.x = 0;
        }
        mpPointF.y = -tv_indicator.getHeight();
        return mpPointF;
    }
}
