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
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.hardcopy.blechat.R;
import com.hardcopy.blechat.R.id;
import com.hardcopy.blechat.R.layout;
import com.hardcopy.blechat.utils.AppSettings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class GraphFragment extends Fragment {

    private Context mContext = null;
    private IFragmentListener mFragmentListener = null;

    private CheckBox mCheckBackground;
    private TextView mTextIot;


    public GraphFragment(Context c, IFragmentListener l) {
        mContext = c;
        mFragmentListener = l;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        AppSettings.initializeAppSettings(mContext);

        View rootView = inflater.inflate(layout.fragment_graph, container, false);

        LineChart chart = (LineChart)rootView.findViewById(R.id.chart);
        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();

        valsComp1.add(new Entry(100.0f, 0));
        valsComp1.add(new Entry(50.0f, 1));
        valsComp1.add(new Entry(75.0f, 2));
        valsComp1.add(new Entry(50.0f, 3));

        LineDataSet setComp1=new LineDataSet(valsComp1, "company 1");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("1.0");
        xVals.add("2.0");
        xVals.add("3.0");
        xVals.add("4.0");

        LineData data = new LineData(xVals, dataSets);

        chart.setData(data);
        chart.invalidate();

        return rootView;
    }


}
