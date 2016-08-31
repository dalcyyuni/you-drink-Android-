package com.youdrink.drinkingcart;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends BlunoLibrary {
    private Button buttonScan;
    private Button buttonSerialSend;
    private EditText serialSendText;
    private TextView serialReceivedText;

    private StringBuffer str_read = new StringBuffer();

    private ImageButton buttonStart;
    private ImageButton buttonStop;
    private String str_code = "";
    private ImageButton button3;
    private ImageButton button4;
    private ImageButton buttonReverse;
    private ImageButton buttonDrink;
    private Button buttonSound;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onCreateProcess();          // onCreate Process by BlunoLibrary
        serialBegin(115200);        // set the Uart Baudrate on BLE chip to 115200

        // initialize the EditText of the received data
        serialReceivedText = (TextView) findViewById(R.id.serialReveicedText);

        // initialize the EditText of the sending data
        serialSendText = (EditText) findViewById(R.id.serialSendText);

        music = MediaPlayer.create(this, R.raw.title);
        music.setLooping(true);

        buttonStart = (ImageButton) findViewById(R.id.btn_start);
        buttonStart.setEnabled(false);
        buttonStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!check_start) {
                    music = MediaPlayer.create(getApplicationContext(), R.raw.title);
                    music.start();

                    buttonStart.setImageResource(R.drawable.stop1);
                    str_code = "1";
                    check_start = true;
                } else {
                    if (music.isPlaying()) {
                        music.stop();
                        try {
                            music.prepare();
                        } catch (IllegalStateException | IOException e) {
                            e.printStackTrace();
                        }
                        music.seekTo(0);
                    }

                    buttonStart.setImageResource(R.drawable.start1);
                    str_code = "2";
                    check_start = false;
                }

                serialSend(str_code);
            }
        });

        buttonStop = (ImageButton) findViewById(R.id.btn_stop);
        buttonStop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                serialReceivedText.setText("");

                buttonStart.setEnabled(true);
                buttonStart.setVisibility(View.VISIBLE);
                buttonStart.setImageResource(R.drawable.start1);
                button3.setVisibility(View.INVISIBLE);
                button4.setVisibility(View.INVISIBLE);
                buttonReverse.setVisibility(View.INVISIBLE);
                buttonDrink.setVisibility(View.INVISIBLE);
                music.stop();

                str_code = "2";
                check_start = false;
                serialSend(str_code);
            }
        });
        button3 = (ImageButton) findViewById(R.id.btn_left);
        button3.setVisibility(View.INVISIBLE);
        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                str_code = "4";
                serialSend(str_code);
            }
        });

        button4 = (ImageButton) findViewById(R.id.btn_right);
        button4.setVisibility(View.INVISIBLE);
        button4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                str_code = "5";
                serialSend(str_code);
            }
        });

        buttonReverse = (ImageButton) findViewById(R.id.btn_reverse);
        buttonReverse.setVisibility(View.INVISIBLE);
        buttonReverse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                str_code = "3";
                serialSend(str_code);
            }
        });
        buttonDrink = (ImageButton) findViewById(R.id.btn_drink);
        buttonDrink.setVisibility(View.INVISIBLE);
        buttonDrink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Random r = new Random();
                int num = (r.nextInt()) % 6;
                switch (num) {
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
                    } catch (IllegalStateException | IOException e) {
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

        //initialize the button for scanning the BLE device
        buttonScan = (Button) findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Alert Dialog for selecting the BLE device
                buttonScanOnClickProcess();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    protected void onResume() {
        super.onResume();
        System.out.println("BlUNOActivity onResume");
        //onResume Process by BlunoLibrary
        onResumeProcess();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //onActivityResult Process by BlunoLibrary
        onActivityResultProcess(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //onPause Process by BlunoLibrary
        onPauseProcess();
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
                Uri.parse("android-app://com.youdrink.drinkingcart/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        //onStop Process by BlunoLibrary
        onStopProcess();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //onDestroy Process by BlunoLibrary
        onDestroyProcess();
    }

    //Once connection state changes, this function will be called
    @Override
    public void onConnectionStateChange(connectionStateEnum theConnectionState) {
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
                buttonReverse.setVisibility(View.INVISIBLE);
                buttonDrink.setVisibility(View.INVISIBLE);
                break;
            case isScanning:
                buttonScan.setText("Scanning");
                buttonStart.setEnabled(false);
                buttonStart.setVisibility(View.VISIBLE);
                buttonStart.setImageResource(R.drawable.start1);
                check_start = false;
                button3.setVisibility(View.INVISIBLE);
                button4.setVisibility(View.INVISIBLE);
                buttonReverse.setVisibility(View.INVISIBLE);
                buttonDrink.setVisibility(View.INVISIBLE);
                break;
            case isDisconnecting:
                buttonScan.setText("isDisconnecting");
                buttonStart.setEnabled(false);
                buttonStart.setVisibility(View.VISIBLE);
                buttonStart.setImageResource(R.drawable.start1);
                check_start = false;
                button3.setVisibility(View.INVISIBLE);
                button4.setVisibility(View.INVISIBLE);
                buttonReverse.setVisibility(View.INVISIBLE);
                buttonDrink.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    //Once connection data received, this function will be called
    @Override
    public void onSerialReceived(String theString) {
        button3 = (ImageButton) findViewById(R.id.btn_left);
        button4 = (ImageButton) findViewById(R.id.btn_right);
        buttonReverse = (ImageButton) findViewById(R.id.btn_reverse);
        buttonStart = (ImageButton) findViewById(R.id.btn_start);
        buttonDrink = (ImageButton) findViewById(R.id.btn_drink);

        str_read.append(theString);

        if (str_read.indexOf("\n") > -1) {
            int a = str_read.indexOf("\n");
            System.out.println(str_read.toString());
            String sss = str_read.substring(0, a - 1);
            str_read.delete(0, a + 1);
            System.out.println("indexOf > -1");

            switch (sss) {
                case "startSpin":
                    buttonStart.setVisibility(View.INVISIBLE);
                    button3.setVisibility(View.INVISIBLE);
                    button4.setVisibility(View.INVISIBLE);
                    buttonReverse.setVisibility(View.VISIBLE);

                    break;
                case "goForward":

                    button3.setVisibility(View.VISIBLE);
                    button4.setVisibility(View.VISIBLE);
                    buttonReverse.setVisibility(View.INVISIBLE);
                    break;
                case "arrived":
                    music.stop();

                    button3.setVisibility(View.INVISIBLE);
                    button4.setVisibility(View.INVISIBLE);
                    buttonReverse.setVisibility(View.INVISIBLE);
                    buttonDrink.setVisibility(View.VISIBLE);
                    music2 = MediaPlayer.create(this, R.raw.yd_all);

                    music2.setLooping(false);
                    music2.start();

                    break;
                case "hold":
                    music3 = MediaPlayer.create(this, R.raw.one);
                    music3.setLooping(false);
                    music3.start();

                    check_sount = false;
                    buttonDrink.setVisibility(View.INVISIBLE);
                    buttonStart.setVisibility(View.VISIBLE);
                    buttonStart.setImageResource(R.drawable.start1);

                    break;
            }
        }

        //append the text into the EditText
        serialReceivedText.append(theString);
        //The Serial data from the BLUNO may be sub-packaged, so using a buffer to hold the String is a good choice.
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
                Uri.parse("android-app://com.youdrink.drinkingcart/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }
}
