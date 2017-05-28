package com.umiss;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

public class HeartBeatsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f1, container, false);
        plot(view);
        return view;
    }

    private void plot(View view){
        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 123),
                new DataPoint(1, 100),
                new DataPoint(2, 110),
                new DataPoint(3, 124),
                new DataPoint(4, 125),
                new DataPoint(5, 123),
                new DataPoint(6, 100),
                new DataPoint(7, 110),
                new DataPoint(8, 124),
                new DataPoint(9, 125)
        });
        series.setDrawDataPoints(true);
        graph.addSeries(series);
    }
}
