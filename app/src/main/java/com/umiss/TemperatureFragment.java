package com.umiss;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class TemperatureFragment extends Fragment {

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
                new DataPoint(1, 123),
                new DataPoint(2, 100),
                new DataPoint(3, 110),
                new DataPoint(4, 124),
                new DataPoint(5, 125),
                new DataPoint(6, 123),
                new DataPoint(7, 100),
                new DataPoint(8, 110),
                new DataPoint(9, 124),
                new DataPoint(10, 125)
        });
        graph.getViewport().setScalable(true);
        series.setDrawDataPoints(true);
        graph.addSeries(series);
    }
}
