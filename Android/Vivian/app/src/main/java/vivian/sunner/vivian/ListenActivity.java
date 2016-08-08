package vivian.sunner.vivian;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;

/*
    Add by sunner at 2016/8/9
    The Listen mode has two direction.
 */
public class ListenActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    // Fundemential
    String TAG = "listen";
    TextToSpeech t;
    static Parser parser;

    // Custom Widget
    RollTextViewHandler rollTextViewHandler = new RollTextViewHandler();
    TextView roll;
    public Switch ctrl, slow;

    // Flag
    public boolean stopFlag = true;                                                                 // The flag to stop the TTS
    public boolean haveSpeak = false;                                                               // The flag to record if allocate TTS

    // The handler to update the RollTextView
    Handler changeTextHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            roll.setText(rollTextViewHandler.show());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);
        loadTTSEngine();                                                                            // Load the TTS engine
    }

    // Implement loading the TTS engine
    public void loadTTSEngine() {
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(0, 1, checkTTSIntent);
    }

    // Examine if the engine has been install
    private void startActivityForResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                t = new TextToSpeech(this, (TextToSpeech.OnInitListener) this);
            } else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Log.d("設定", "finish");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            parser = new Parser();
            parser.readFromFile();
            loadView();
        }
    }

    // Implement loading the View
    public void loadView() {
        ctrl = (Switch) findViewById(R.id.ctrl);                                                    // load view object
        roll = (TextView) findViewById(R.id.roll);
        slow = (Switch) findViewById(R.id.slow);
        ctrl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                slow.setVisibility(View.VISIBLE);
                haveSpeak = true;
                if (isChecked) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            int size = parser.getNumberOfWord();

                            // Determine the direction
                            boolean reverse = (Math.random() > 0.5 ? true : false);
                            if (reverse) {
                                for (int i = 0; i < size && stopFlag == true; i++)
                                    speakByIndex(i);
                            } else {
                                for (int i = size - 1; i > 0 && stopFlag == true; i--)
                                    speakByIndex(i);
                            }
                        }
                    }.start();
                } else
                    stopFlag = false;
            }
        });
        slow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    t.setSpeechRate(0.8f);
                else
                    t.setSpeechRate(1);
            }
        });
    }

    // Seperate from the main function(Speak)
    public void speakByIndex(int i) {
        // Set the language
        if (i % 5 == 0) {
            switch ((i / 5) % 3) {
                case 0:
                    t.setLanguage(new Locale("en", "GB"));
                    break;
                case 1:
                    t.setLanguage(new Locale("en", "US"));
                    break;
                case 2:
                    t.setLanguage(new Locale("en", "AS"));
            }
        }

        // Speak the word
        t.speak(parser.getEn_Read(i), TextToSpeech.QUEUE_ADD, null);

        // Show the chinese
        rollTextViewHandler.add(parser.getCh_Read(i));
        Message message = new Message();
        changeTextHandler.sendMessage(message);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopFlag = false;
        if (haveSpeak) {
            t.stop();
            t.shutdown();
        }
    }
}
