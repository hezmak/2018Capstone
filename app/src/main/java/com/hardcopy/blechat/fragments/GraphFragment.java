/*
 * Copyright (C) 2014 Bluetooth Connection Template
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hardcopy.blechat.fragments;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hardcopy.blechat.R;
import com.hardcopy.blechat.R.id;
import com.hardcopy.blechat.R.layout;
import com.hardcopy.blechat.utils.AppSettings;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GraphFragment extends Fragment {

    private Context mContext = null;
    private IFragmentListener mFragmentListener = null;

    private CheckBox mCheckBackground;
    private TextView mTextIot;
    LineChart mLineChart;

    static int counter = 1;
    float temp=0.0f;

    Weather weather;

    public GraphFragment(Context c, IFragmentListener l) {
        mContext = c;
        mFragmentListener = l;
    }





    private void addEntry(float x, float y)
    {
        //Grab a reference to the LineData and DataSet objects. Initialize them if they're null.
        LineData data = mLineChart.getData();

        if(data == null)
        {
            data = new LineData();
            this.mLineChart.setData(data);
        }

        ILineDataSet set = data.getDataSetByIndex(0);
        ILineDataSet set1 = data.getDataSetByIndex(1);
        ILineDataSet set2 = data.getDataSetByIndex(2);

        if(set == null)
        {
            set = new LineDataSet(null, "Values");
            data.addDataSet(set);

        }

        //float rV1 = (float)(Math.floor(Math.random() * (600 - 15 + 1)) + 15);
        //float rV2 = (float)(Math.floor(Math.random() * (400 - 15 + 1)) + 15);

        //Add the measurement to the chart.
        set.addEntry(new Entry(x, y));
        set1.addEntry(new Entry(counter, weather.getHumidity()));
        set2.addEntry(new Entry(counter, weather.getTemperature()));

        //Round robin the set.
        while(set.getEntryCount() > 10)
        {
            data.removeEntry(0, 0);
            data.removeEntry(0, 1);
            data.removeEntry(0, 2);
        }

        data.notifyDataChanged();
        //Let the chart know it's data has changed.
        this.mLineChart.notifyDataSetChanged();
        this.mLineChart.invalidate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        AppSettings.initializeAppSettings(mContext);
        weather = new Weather();

        View rootView = inflater.inflate(layout.fragment_graph, container, false);
        mLineChart = (LineChart)rootView.findViewById(R.id.chart);





        new CountDownTimer(3000000, 3000) {

        public void onTick(long millisUntilFinished) {
            //float randomValue = (float)(Math.floor(Math.random() * (200 - 15 + 1)) + 15);
            //Toast.makeText (mContext, Float.toString(weather.getDustdensity())+"/"+String.valueOf(counter), Toast.LENGTH_SHORT).show();
            //Entry newEntry = new Entry((float) randomValue, indexOfMyLine);
            if(temp != weather.getDustdensity()){
                addEntry(counter, weather.getDustdensity());
                counter++;
            }

            temp = weather.getDustdensity();
        }


    public void onFinish() {


    }
}.start();







        List<Entry> valsComp1 = new ArrayList<Entry>();
        List<Entry> valsComp2 = new ArrayList<Entry>();
        List<Entry> valsComp3 = new ArrayList<Entry>();

        Entry c1e1 = new Entry(0, 0.0f); // 0 == quarter 1
        valsComp1.add(c1e1);
        //Entry c1e2 = new Entry(1, 140f); // 1 == quarter 2 ...
        //valsComp1.add(c1e2);
        //Entry c1e3 = new Entry(2, 350f); // 2 == quarter 3 ...
        //valsComp1.add(c1e3);
        // and so on ...

        Entry c2e1 = new Entry(0, 0.0f); // 0 == quarter 1
        valsComp2.add(c2e1);


        Entry c3e1 = new Entry(0, 0.0f); // 0 == quarter 1
        valsComp3.add(c3e1);
        //...

        LineDataSet setComp1 = new LineDataSet(valsComp1, "FineDust");
        setComp1.setColors(new int[] { R.color.red4 }, mContext);
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineDataSet setComp2 = new LineDataSet(valsComp2, "Humidity");
        setComp2.setColors(new int[] { R.color.green4 }, mContext);
        setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineDataSet setComp3 = new LineDataSet(valsComp3, "Temperature");
        setComp3.setColors(new int[] { R.color.blue4 }, mContext);
        setComp3.setAxisDependency(YAxis.AxisDependency.LEFT);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);
        dataSets.add(setComp2);
        dataSets.add(setComp3);

        LineData data = new LineData(dataSets);
        data.setValueFormatter(new MyValueFormatter());
        mLineChart.setData(data);
        mLineChart.invalidate(); // refresh




        return rootView;
    }


}
