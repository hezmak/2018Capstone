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
import com.hardcopy.blechat.MainActivity;
import com.hardcopy.blechat.R;
import com.hardcopy.blechat.R.id;
import com.hardcopy.blechat.R.layout;
import com.hardcopy.blechat.utils.AppSettings;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    static    String strJson = "";

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

        //테스트
        ///*
        float rV1 = (float)(Math.floor(Math.random() * (600 - 15 + 1)) + 15);
        float rV2 = (float)(Math.floor(Math.random() * (400 - 15 + 1)) + 15);
        set1.addEntry(new Entry(counter, rV1));
        set2.addEntry(new Entry(counter, rV2));


        weather.setHumidity(rV1);
        weather.setTemperature(rV2);

        if (!validate()) {
        } else {
            // call AsynTask to perform network operation on separate thread
            HttpAsyncTask httpTask = new HttpAsyncTask((MainActivity) getActivity());

            Toast.makeText(mContext, Float.toString(weather.getDustdensity()), Toast.LENGTH_SHORT).show();
            httpTask.execute("https://dadalhwa.appspot.com/getdata",
                    Float.toString(weather.getDustdensity()), Float.toString(weather.getHumidity()), Float.toString(weather.getTemperature()));
        }
//*/
        //Add the measurement to the chart.
        set.addEntry(new Entry(x, y));
        //set1.addEntry(new Entry(counter, weather.getHumidity()));
        //set2.addEntry(new Entry(counter, weather.getTemperature()));

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





        new CountDownTimer(3000000, 10000) {

        public void onTick(long millisUntilFinished) {
            float randomValue = (float)(Math.floor(Math.random() * (200 - 15 + 1)) + 15);
            addEntry(counter, randomValue);
            counter++;
            weather.setDustdensity(randomValue);

            /*
            if(temp != weather.getDustdensity()){
                addEntry(counter, weather.getDustdensity());
                counter++;
            }
            */

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










    //수정본



    public static String POST(String url, Weather weather){
        InputStream is = null;
        String result = "";
        try {
            URL urlCon = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();

            String json = "";

            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("density", weather.getDustdensity());
            jsonObject.accumulate("humidity", weather.getHumidity());
            jsonObject.accumulate("temperature", weather.getTemperature());

            // convert JSONObject to JSON to String
            json = jsonObject.toString();
            Log.d("help", json);

            // ** Alternative way to convert Weather object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(weather);

            // Set some headers to inform server about the type of the content
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestProperty("Content-type", "application/json");

            // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
            httpCon.setDoOutput(true);
            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
            httpCon.setDoInput(true);

            OutputStream os = httpCon.getOutputStream();
            os.write(json.getBytes("euc-kr"));
            os.flush();
            // receive response as inputStream
            try {
                is = httpCon.getInputStream();
                // convert inputstream to string
                if(is != null)
                    result = convertInputStreamToString(is);
                else
                    result = "Did not work!";
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                httpCon.disconnect();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private   MainActivity mainAct;

        HttpAsyncTask(MainActivity mainActivity) {
            this.mainAct = mainActivity;
        }
        @Override
        protected String doInBackground(String... urls) {

            weather = new Weather();
            weather.setDustdensity(Float.valueOf(urls[1]));
            weather.setHumidity(Float.valueOf(urls[2]));
            weather.setTemperature(Float.valueOf(urls[3]));

            return POST(urls[0],weather);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            Toast.makeText (this.mainAct, result, Toast.LENGTH_SHORT).show();

            //mainAct.tvResponse.setText(result);
            strJson = result;
            mainAct.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainAct, "Received!", Toast.LENGTH_SHORT).show();
                    try {
                        JSONArray json = new JSONArray(strJson);
                        //mEditChat.setText(json.toString(1));

                        //Toast.makeText (mainAct, strJson, Toast.LENGTH_LONG).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    private boolean validate(){
        if(Float.toString(weather.getDustdensity()).trim().equals(""))
            return false;
        else if(Float.toString(weather.getHumidity()).trim().equals(""))
            return false;
        else if(Float.toString(weather.getTemperature()).trim().equals(""))
            return false;
        else
            return true;
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }






}
