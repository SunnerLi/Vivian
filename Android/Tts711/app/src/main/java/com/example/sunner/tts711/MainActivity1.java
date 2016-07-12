package com.example.sunner.tts711;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Locale;

public class MainActivity1 extends AppCompatActivity implements TextToSpeech.OnInitListener {
    /*
    TextToSpeech myTTS;
    Button UKBtn, USBtn, ASBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                LoadTTSEngine();
            }
        };
        thread.start();

        // Load the object
        UKBtn = (Button)findViewById(R.id.UKBtn);
        USBtn = (Button)findViewById(R.id.USBtn);
        ASBtn = (Button)findViewById(R.id.ASBtn);

        // Set listener
        UKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTTS.setLanguage(new Locale("en", "GB"));
                myTTS.speak("I'm england expert", TextToSpeech.QUEUE_ADD, null);
            }
        });
        USBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTTS.setLanguage(new Locale("en", "US"));
                myTTS.speak("I'm American expert", TextToSpeech.QUEUE_ADD, null);
            }
        });
        ASBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTTS.setLanguage(new Locale("en", "AS"));
                myTTS.speak("I'm American expert", TextToSpeech.QUEUE_ADD, null);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        final Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();

                Animation fadeIn = new AlphaAnimation(1, 0);
                fadeIn.setInterpolator(new AccelerateInterpolator()); //add this
                fadeIn.setDuration(1000);

                USBtn.startAnimation(fadeIn);
                UKBtn.startAnimation(fadeIn);
                ASBtn.startAnimation(fadeIn);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                USBtn.setAlpha(0);
                UKBtn.setAlpha(0);
                ASBtn.setAlpha(0);
            }
        };
        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.start();
    }

    // 實作載入TTS引擎
    public void LoadTTSEngine() {
        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(0, 1, checkTTSIntent);
    }

    // 檢查是否有裝TTS引擎
    private void startActivityForResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                myTTS = new TextToSpeech(this, (TextToSpeech.OnInitListener) this);

            } else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }

        }
    }
    */

    @Override
    public void onInit(int status) {

    }
}
