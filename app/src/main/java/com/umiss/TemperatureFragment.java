package com.umiss;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.koushikdutta.async.future.FutureCallback;

import java.util.ArrayList;
import java.util.List;

import network.UMISSRest;

public class TemperatureFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f1, container, false);
        plot(view);
        return view;
    }

    private void plot(View view){
        final GraphView graph = (GraphView) view.findViewById(R.id.graph);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data",getActivity().MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "token");

        final List<String> list = new ArrayList<>();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Pegando dados do servidor...");
        progressDialog.show();

        UMISSRest.get(UMISSRest.TEMPERATURES, token, new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                try {
                    JsonArray r = result.get("data").getAsJsonArray();

                    Log.d("JsonArray", r.toString());
                    for (int i = 0; i < r.size(); i++) {

                        String temperatures = r.get(i).getAsJsonObject().get("attributes").
                                getAsJsonObject().get("temperature").getAsString();
                        list.add(temperatures);
                        Log.d("temperatures", temperatures);
                    }
                }catch (NullPointerException n){

                    Log.d("NullpointerException", n.toString());
                }

                DataPoint[] dataPoints = new DataPoint[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    // add new DataPoint object to the array for each of your list entries
                    dataPoints[i] = new DataPoint(i, Integer.parseInt(list.get(i))); // not sure but I think the second argument should be of type double
                }

                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
                series.setDrawDataPoints(true);
                graph.addSeries(series);

                progressDialog.dismiss();
            }
        }, getActivity().getApplicationContext());
    }
}
