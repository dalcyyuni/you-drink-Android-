package com.dfrobot.angelo.blunobasicdemo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;

public class MainActivity extends BlunoLibrary {
	private Button buttonScan;
	private Button buttonSerialSend;
	private EditText serialSendText;
	private TextView serialReceivedText;

	//

	private Button buttonStart;
	private Button buttonStop;
	private String str_code = new String("");
	private Button button3;
	private Button button4;
	private Button buttonSound;
	private SeekBar barSound;
	private MediaPlayer music;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		onCreateProcess();                                                        //onCreate Process by BlunoLibrary


		serialBegin(115200);                                                    //set the Uart Baudrate on BLE chip to 115200

		serialReceivedText = (TextView) findViewById(R.id.serialReveicedText);    //initial the EditText of the received data
		serialSendText = (EditText) findViewById(R.id.serialSendText);            //initial the EditText of the sending data
		//

		buttonStart = (Button) findViewById(R.id.btn_start);
		buttonStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				serialReceivedText.setText("");
				str_code = "1";
				serialSend(str_code);

			}
		});

		buttonStop = (Button) findViewById(R.id.btn_stop);
		buttonStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				serialReceivedText.setText("");
				str_code = "2";
				serialSend(str_code);
			}
		});

		button3 = (Button) findViewById(R.id.btn_3);
		button3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				serialReceivedText.setText("");
				str_code = "3";
				serialSend(str_code);
			}
		});

		button4 = (Button) findViewById(R.id.btn_4);
		button4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				serialReceivedText.setText("");
				str_code = "4";
				serialSend(str_code);
			}
		});

		music = MediaPlayer.create(this, R.raw.konan);
		music.setLooping(true);
		buttonSound = (Button) findViewById(R.id.btn_sound);
		buttonSound.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (music.isPlaying()) {
					music.stop();
					try {
						music.prepare();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					music.seekTo(0);
					buttonSound.setText("Music Start");

				} else {
					music.start();
					buttonSound.setText("Music Stop");


				}
			}
		});



		//
		buttonSerialSend = (Button) findViewById(R.id.buttonSerialSend);        //initial the button for sending the data
		buttonSerialSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				serialReceivedText.setText("");
				serialSend(serialSendText.getText().toString());                //send the data to the BLUNO
			}
		});

		buttonScan = (Button) findViewById(R.id.buttonScan);                    //initial the button for scanning the BLE device
		buttonScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				buttonScanOnClickProcess();                                        //Alert Dialog for selecting the BLE device
			}
		});
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	protected void onResume() {
		super.onResume();
		System.out.println("BlUNOActivity onResume");
		onResumeProcess();                                                        //onResume Process by BlunoLibrary
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		onActivityResultProcess(requestCode, resultCode, data);                    //onActivityResult Process by BlunoLibrary
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {
		super.onPause();
		onPauseProcess();                                                        //onPause Process by BlunoLibrary
	}

	protected void onStop() {
		super.onStop();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Main Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://com.dfrobot.angelo.blunobasicdemo/http/host/path")
		);
		AppIndex.AppIndexApi.end(client, viewAction);
		onStopProcess();                                                        //onStop Process by BlunoLibrary
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.disconnect();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		onDestroyProcess();                                                        //onDestroy Process by BlunoLibrary
	}

	@Override
	public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
		switch (theConnectionState) {                                            //Four connection state
			case isConnected:
				buttonScan.setText("Connected");
				break;
			case isConnecting:
				buttonScan.setText("Connecting");
				break;
			case isToScan:
				buttonScan.setText("Scan");
				break;
			case isScanning:
				buttonScan.setText("Scanning");
				break;
			case isDisconnecting:
				buttonScan.setText("isDisconnecting");
				break;
			default:
				break;
		}
	}

	@Override
	public void onSerialReceived(String theString) {                            //Once connection data received, this function will be called
		// TODO Auto-generated method stub
		serialReceivedText.append(theString);                            //append the text into the EditText
		//The Serial data from the BLUNO may be sub-packaged, so using a buffer to hold the String is a good choice.
		//serialReceivedText.setText(theString);
		//serialReceivedText.
		((ScrollView) serialReceivedText.getParent()).fullScroll(View.FOCUS_DOWN);
	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Main Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://com.dfrobot.angelo.blunobasicdemo/http/host/path")
		);
		AppIndex.AppIndexApi.start(client, viewAction);
	}
}