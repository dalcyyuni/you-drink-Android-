package com.dfrobot.angelo.blunobasicdemo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Main2Activity extends BlunoLibrary {
    private Button buttonScan;
    private Button buttonSerialSend;
    private EditText serialSendText;
    private TextView serialReceivedText;

    //
    private StringBuffer str_read= new StringBuffer();

    private ImageButton buttonStart;
    private ImageButton buttonStop;
    private String str_code = new String("");
    private ImageButton button3;
    private ImageButton button4;
    private ImageButton button_reverse;
    private ImageButton button_drink;
    private Button buttonSound;
    private SeekBar barSound;
    private static MediaPlayer music;
    private MediaPlayer music2;
    private MediaPlayer music3;
    private boolean check_start = false;
    private boolean check_connect = false;

    private boolean check_sount = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        onCreateProcess();                                                        //onCreate Process by BlunoLibrary

       System.out.println();
        serialBegin(115200);                                                    //set the Uart Baudrate on BLE chip to 115200

        serialReceivedText = (TextView) findViewById(R.id.serialReveicedText);

                //initial the EditText of the received data



        serialSendText = (EditText) findViewById(R.id.serialSendText);            //initial the EditText of the sending data
        //
        music = MediaPlayer.create(this, R.raw.title);
        music.setLooping(true);

        buttonStart = (ImageButton) findViewById(R.id.btn_start);
        buttonStart.setEnabled(false);
        buttonStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(check_start==false)
                {
                    music = MediaPlayer.create(getApplicationContext(), R.raw.title);
                    //serialReceivedText.setText("");
                    str_code = "1";
                    serialSend(str_code);
                   // buttonStart.setText("Stop");
                    buttonStart.setImageResource(R.drawable.stop1);

                    check_start = true;

                    music.start();

                }else
                {
                   // serialReceivedText.setText("");
                    str_code = "2";
                    serialSend(str_code);
                 //   buttonStart.setText("Start");
                    buttonStart.setImageResource(R.drawable.start1);
                    check_start = false;
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


                    }
                }


            }
        });


        buttonStop = (ImageButton) findViewById(R.id.btn_stop);
        buttonStop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                serialReceivedText.setText("");
                str_code = "2";
                serialSend(str_code);
                check_start = false;

                buttonStart.setEnabled(true);
                buttonStart.setVisibility(View.VISIBLE);
                buttonStart.setImageResource(R.drawable.start1);
                button3.setVisibility(View.INVISIBLE);
                button4.setVisibility(View.INVISIBLE);
                button_reverse.setVisibility(View.INVISIBLE);
                button_drink.setVisibility(View.INVISIBLE);
                music.stop();

            }
        });
        button3 = (ImageButton) findViewById(R.id.btn_3);
        button3.setVisibility(View.INVISIBLE);
        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                serialReceivedText.setText("");
                str_code = "4";
                serialSend(str_code);
            }
        });

        button4 = (ImageButton) findViewById(R.id.btn_4);
        button4.setVisibility(View.INVISIBLE);
        button4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                serialReceivedText.setText("");
                str_code = "5";
                serialSend(str_code);
            }
        });

        button_reverse = (ImageButton)findViewById(R.id.btn_reverse);
        button_reverse.setVisibility(View.INVISIBLE);
        button_reverse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                serialReceivedText.setText("");
                str_code = "3";
                serialSend(str_code);
            }
        });
        button_drink = (ImageButton)findViewById(R.id.btn_drink);
        button_drink.setVisibility(View.INVISIBLE);
        button_drink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Random r = new Random();
                int num = (r.nextInt()) % 6;
                switch (num)
                {
                    case 0:
                        music2 = MediaPlayer.create(getApplicationContext(), R.raw.yd_all);
                        break;
                    case 1:
                        music2 = MediaPlayer.create(getApplicationContext(), R.raw.yd1);
                        break;
                    case 2:
                       music2 = MediaPlayer.create(getApplicationContext(), R.raw.yd2);
                        break;
                    case 3:
                        music2 = MediaPlayer.create(getApplicationContext(), R.raw.yd3);
                        break;
                    case 4:
                        music2 = MediaPlayer.create(getApplicationContext(), R.raw.yd4);
                        break;
                    case 5:
                       music2 = MediaPlayer.create(getApplicationContext(), R.raw.yd5);
                        break;
                }


                music2.start();
            }
        });


        music.setLooping(false);
        buttonSound = (Button) findViewById(R.id.btn_sound);
        buttonSound.setVisibility(View.INVISIBLE);
        buttonSound.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                music = MediaPlayer.create(getApplicationContext(), R.raw.title);
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


        //button3.setVisibility(View.INVISIBLE);
       // button4.setVisibility(View.INVISIBLE);
       // button_reverse.setVisibility(View.INVISIBLE);
        //


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
        onResumeProcess();
        //onResume Process by BlunoLibrary


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
                buttonStart.setEnabled(true);

                break;
            case isConnecting:
                buttonScan.setText("Connecting");
                buttonStart.setEnabled(true);

                break;
            case isToScan:
                buttonScan.setText("Scan");
                buttonStart.setEnabled(false);
                buttonStart.setVisibility(View.VISIBLE);
                buttonStart.setImageResource(R.drawable.start1);
                check_start = false;
                button3.setVisibility(View.INVISIBLE);
                button4.setVisibility(View.INVISIBLE);
                button_reverse.setVisibility(View.INVISIBLE);
                button_drink.setVisibility(View.INVISIBLE);
                break;
            case isScanning:
                buttonScan.setText("Scanning");
                buttonStart.setEnabled(false);
                buttonStart.setVisibility(View.VISIBLE);
                buttonStart.setImageResource(R.drawable.start1);
                check_start = false;
                button3.setVisibility(View.INVISIBLE);
                button4.setVisibility(View.INVISIBLE);
                button_reverse.setVisibility(View.INVISIBLE);
                button_drink.setVisibility(View.INVISIBLE);
                break;
            case isDisconnecting:
                buttonScan.setText("isDisconnecting");
                buttonStart.setEnabled(false);
                buttonStart.setVisibility(View.VISIBLE);
                buttonStart.setImageResource(R.drawable.start1);
                check_start = false;
                button3.setVisibility(View.INVISIBLE);
                button4.setVisibility(View.INVISIBLE);
                button_reverse.setVisibility(View.INVISIBLE);
                button_drink.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSerialReceived(String theString) {                            //Once connection data received, this function will be called
        // TODO Auto-generated method stub
       // System.out.println(theString);
        System.out.println(serialReceivedText.getText().toString());
        serialReceivedText.append(theString);

      // System.out.println(serialReceivedText.getText().toString());

            button3 = (ImageButton) findViewById(R.id.btn_3);
            button4 = (ImageButton)findViewById(R.id.btn_4);
            button_reverse = (ImageButton)findViewById(R.id.btn_reverse);
            buttonStart = (ImageButton) findViewById(R.id.btn_start);
            button_drink = (ImageButton)findViewById(R.id.btn_drink);
         str_read.append(theString);

         if(str_read.indexOf("\n")>-1)
         {
             int a = str_read.indexOf("\n");
             System.out.println(str_read.toString());
             String sss = str_read.substring(0,a-1);
             str_read.delete(0,a+1);
             System.out.println("indecof > -1");

             if(sss.equals("startSpin"))
             {
                buttonStart.setVisibility(View.INVISIBLE);
                 button3.setVisibility(View.INVISIBLE);
                 button4.setVisibility(View.INVISIBLE);
                 button_reverse.setVisibility(View.VISIBLE);

             }else if(sss.equals("goForward")){

                 button3.setVisibility(View.VISIBLE);
                 button4.setVisibility(View.VISIBLE);
                 button_reverse.setVisibility(View.INVISIBLE);
             }
             else if(sss.equals("arrived")) {
                 music.stop();

                 button3.setVisibility(View.INVISIBLE);
                 button4.setVisibility(View.INVISIBLE);
                 button_reverse.setVisibility(View.INVISIBLE);
                 button_drink.setVisibility(View.VISIBLE);
                 music2 = MediaPlayer.create(this, R.raw.yd_all);


                     music2.setLooping(false);
                     music2.start();




             }else if(sss.equals("hold"))
             {
                 music3 = MediaPlayer.create(this, R.raw.one);
                 music3.setLooping(false);
                 music3.start();

                 check_sount = false;
                button_drink.setVisibility(View.INVISIBLE);
                 buttonStart.setVisibility(View.VISIBLE);
                 buttonStart.setImageResource(R.drawable.start1);

             }



         }




        //append the text into the EditText
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