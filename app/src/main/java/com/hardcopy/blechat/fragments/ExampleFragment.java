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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hardcopy.blechat.MainActivity;
import com.hardcopy.blechat.R;

import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import android.text.method.ScrollingMovementMethod;
import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.content.Context;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hardcopy.blechat.MainActivity;
import com.hardcopy.blechat.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExampleFragment extends Fragment implements View.OnClickListener {

	private Context mContext = null;
	private IFragmentListener mFragmentListener = null;
	private Handler mActivityHandler = null;
	String sms_data = "Fine dust concentration is too high. Be carefull!!";

	TextView mTextChat, textView, textView_dummy;
	EditText mEditChat;
	Button mBtnSend, button, button2;

	Weather weather;




	public ExampleFragment(Context c, IFragmentListener l, Handler h) {
		mContext = c;
		mFragmentListener = l;
		mActivityHandler = h;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);

		mTextChat = (TextView) rootView.findViewById(R.id.text_chat);
		textView = (TextView)rootView.findViewById(R.id.textView);
		textView_dummy = (TextView)rootView.findViewById(R.id.textView_dummy);
		//textView.setBackgroundResource(R.drawable.cloudy01);

		mTextChat.setMaxLines(1000);
		mTextChat.setVerticalScrollBarEnabled(true);
		mTextChat.setMovementMethod(new ScrollingMovementMethod());

		mEditChat = (EditText) rootView.findViewById(R.id.edit_chat);
		mEditChat.setOnEditorActionListener(mWriteListener);

		mBtnSend = (Button) rootView.findViewById(R.id.button_send);
		button = (Button) rootView.findViewById(R.id.button);
		button2 = (Button) rootView.findViewById(R.id.button2);
		mBtnSend.setOnClickListener(this);
		button.setOnClickListener(this);
		button2.setOnClickListener(this);

		weather = new Weather();
		final Context context = this.getContext();



		return rootView;
	}

	@Override
	public void onClick(View v) {



		switch(v.getId()) {
			case R.id.button_send:
				String message = mEditChat.getText().toString();
				if(message != null && message.length() > 0)
					sendMessage(message);
				break;
			case R.id.button:
				message = "Start";


				Toast.makeText (this.mContext, "START", Toast.LENGTH_SHORT).show();





				button.setEnabled(false);
				button2.setEnabled(true);
				sendMessage(message);
				break;
			case R.id.button2:
				message = "Stop";
				button.setEnabled(true);
				button2.setEnabled(false);
				sendMessage(message);
				break;

		}
	}


	// The action listener for the EditText widget, to listen for the return key
	private TextView.OnEditorActionListener mWriteListener =
			new TextView.OnEditorActionListener() {
				public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
					// If the action is a key-up event on the return key, send the message
					if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
						String message = view.getText().toString();
						if(message != null && message.length() > 0)
							sendMessage(message);
					}
					return true;
				}
			};

	// Sends user message to remote
	private void sendMessage(String message) {
		if(message == null || message.length() < 1)
			return;
		// send to remote
		if(mFragmentListener != null)
			mFragmentListener.OnFragmentCallback(IFragmentListener.CALLBACK_SEND_MESSAGE, 0, 0, message, null,null);
		else
			return;
		// show on text view
		if(mTextChat != null) {
			mTextChat.append("\nSend: ");
			mTextChat.append(message);
			int scrollamout = mTextChat.getLayout().getLineTop(mTextChat.getLineCount()) - mTextChat.getHeight();
			if (scrollamout > mTextChat.getHeight())
				mTextChat.scrollTo(0, scrollamout);
		}
		mEditChat.setText("");
	}

	private static final int NEW_LINE_INTERVAL = 1000;
	private long mLastReceivedTime = 0L;
	String[] word;
	String[] parse;
	float finedust=10.7f;

	// Show messages from remote
	public void showMessage(String message) {



		if (message != null && message.length() > 0) {
			long current = System.currentTimeMillis();

			if (current - mLastReceivedTime > NEW_LINE_INTERVAL) {
				mTextChat.append("\nRcv: ");
				Log.d("RCV", "Received");

				if (textView_dummy.getText().toString().contains("]")) {

					word = textView_dummy.getText().toString().split("/");

					//Toast.makeText(mContext, word.toString()+"^", Toast.LENGTH_SHORT).show();

					parse = word[0].toString().split(":");
					float humidity = Float.parseFloat(parse[1]);
					parse = word[1].toString().split(":");
					float temperature = Float.parseFloat(parse[1]);
					parse = word[2].toString().split(":");
					float finedust = Float.parseFloat(parse[1]);


					weather.setDustdensity(finedust);
					weather.setHumidity(humidity);
					weather.setTemperature(temperature);



						if (finedust < 30.0) {
							textView.setBackgroundResource(R.drawable.good01);
							textView.setText("Fine Dust: Good");
						} else if (finedust >= 30.0 && finedust < 80.0) {
							textView.setBackgroundResource(R.drawable.normal01);
							textView.setText("Fine Dust: Normal");
						} else {
							textView.setBackgroundResource(R.drawable.bad01);
							textView.setText("Fine Dust: Bad");

							final addDialogFragment dialog = addDialogFragment.newInstance(new addDialogFragment.NameInputListener() {
								@Override
								public void onNameInputComplete(String name) {
									if (name != null) {

										SmsManager sms = SmsManager.getDefault();
										sms.sendTextMessage(name, null, sms_data, null, null);

									}
								}
							});
							dialog.show(getFragmentManager(), "addDialog");

						}

					}
					textView_dummy.setText("");
				}
				mTextChat.append(message);
				int scrollamout = mTextChat.getLayout().getLineTop(mTextChat.getLineCount()) - mTextChat.getHeight();
				if (scrollamout > mTextChat.getHeight())
					mTextChat.scrollTo(0, scrollamout);

				//

				textView_dummy.append(message);
				//textView.setText(message);
				mLastReceivedTime = current;
			}
		}








}
