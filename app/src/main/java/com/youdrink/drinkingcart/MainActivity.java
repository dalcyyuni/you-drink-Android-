package com.youdrink.drinkingcart;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
    private TextView serialReceivedText;

    private StringBuffer str_read = new StringBuffer();
    private ImageButton buttonStart;
    private Button btnPosTopRight;
    private ImageButton buttonLeft;
    private ImageButton buttonRight;
    private ImageButton buttonReverse;
    private ImageButton buttonDrink;
    private Button buttonSound;
    private static MediaPlayer music;
    private MediaPlayer music2;
    private boolean check_start = false;
    private boolean isRandomMove = false;

    private void resetCart() {
        serialReceivedText.setText("");

        buttonStart.setVisibility(View.VISIBLE);
        buttonStart.setImageResource(R.drawable.start1);
        buttonLeft.setVisibility(View.INVISIBLE);
        buttonRight.setVisibility(View.INVISIBLE);
        buttonReverse.setVisibility(View.INVISIBLE);
        buttonDrink.setVisibility(View.INVISIBLE);
        if (music.isPlaying()) {
            music.stop();
            try {
                music.prepare();
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
            music.seekTo(0);
        }

        check_start = false;

        serialSend("b");
        isRandomMove = false;
        btnPosTopRight.setText("Random Move\nTurn On");

        serialSend("2");
    }

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
                    btnPosTopRight.setText("Stop");
                    check_start = true;
                    serialSend("1");
                } else {
                    resetCart();
                }
            }
        });

        btnPosTopRight = (Button) findViewById(R.id.btn_pos_top_right);
        btnPosTopRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!check_start) {
                    if (!isRandomMove) {            // buttonStatus/action : Random Move On
                        serialSend("a");

                        isRandomMove = true;
                        btnPosTopRight.setText("Random Move\nTurn Off");
                    } else {                        // buttonStatus/action : Random Move Off
                        serialSend("b");

                        isRandomMove = false;
                        btnPosTopRight.setText("Random Move\nTurn On");
                    }
                } else {                            // buttonStatus/action : Stop
                    resetCart();
                }
            }
        });
        buttonLeft = (ImageButton) findViewById(R.id.btn_left);
        buttonLeft.setVisibility(View.INVISIBLE);
        buttonLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                serialSend("4");
            }
        });

        buttonRight = (ImageButton) findViewById(R.id.btn_right);
        buttonRight.setVisibility(View.INVISIBLE);
        buttonRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                serialSend("5");
            }
        });

        buttonReverse = (ImageButton) findViewById(R.id.btn_reverse);
        buttonReverse.setVisibility(View.INVISIBLE);
        buttonReverse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                serialSend("3");
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
                break;
            case isToScan:
                buttonScan.setText("Scan");
                buttonStart.setEnabled(false);
                resetCart();
                break;
            case isScanning:
                buttonScan.setText("Scanning");
                break;
            case isDisconnecting:
                buttonScan.setText("isDisconnecting");
                buttonStart.setEnabled(false);
                resetCart();
                break;
            default:
                break;
        }
    }

    //Once connection data received, this function will be called
    @Override
    public void onSerialReceived(String theString) {
        buttonLeft = (ImageButton) findViewById(R.id.btn_left);
        buttonRight = (ImageButton) findViewById(R.id.btn_right);
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
                    buttonLeft.setVisibility(View.INVISIBLE);
                    buttonRight.setVisibility(View.INVISIBLE);
                    buttonReverse.setVisibility(View.VISIBLE);

                    break;
                case "goForward":

                    buttonLeft.setVisibility(View.VISIBLE);
                    buttonRight.setVisibility(View.VISIBLE);
                    buttonReverse.setVisibility(View.INVISIBLE);
                    break;
                case "arrived":
                    music.stop();

                    buttonLeft.setVisibility(View.INVISIBLE);
                    buttonRight.setVisibility(View.INVISIBLE);
                    buttonReverse.setVisibility(View.INVISIBLE);
                    buttonDrink.setVisibility(View.VISIBLE);
                    music2 = MediaPlayer.create(this, R.raw.yd_all);
                    music2.setLooping(false);
                    music2.start();

                    break;
                case "hold":
                    MediaPlayer music3 = MediaPlayer.create(this, R.raw.one);
                    music3.setLooping(false);
                    music3.start();
                    resetCart();
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
