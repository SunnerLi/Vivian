package vivian.sunner.vivian;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {
    // Fundamental
    TextToSpeech t;
    int textTimes = 0;
    Parser parser;
    static int index;

    // View object
    Button ch[];
    TextView textView;                                                                              // The textView to show answer of english
    String string[] = new String[4];                                                                // The string array to record word
    static int YorN = 0;
    ImageView play;                                                                                 // The play image

    // Setting value
    public static boolean openSpeaking = false;
    public static boolean showQText = true;
    public static boolean showAns = true;
    public static int QType = 0;
    public static int mixType = 0;

    /*
        Practice Process:
            1. generate index
            2. change view
            3. wait for user click
            4. check the answer
     */

    Handler practiceHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Constants.GIVE_QUESTION) {                                              // Check if it's practice signal
                parser.generate();
                if (QType == 2)
                    mixType = (int) (Math.random() * 2);
                index = (int) (Math.random() * 4);                                              // Get random order chinese
                for (int i = 0; i < 4; i++) {
                    if (QType == 0 || (QType == 2 && mixType == 0))
                        string[i] = parser.getch(i);
                    else
                        string[i] = parser.getEn(i);
                }
                for (int i = 0; i < 4; i++) {                                                       // Put into button
                    ch[i].setText(string[i]);
                }
                if (QType == 0 || (QType == 2 && mixType == 0))
                    textView.setText(parser.getEn(index));                                          // Show the english answer
                else
                    textView.setText(parser.getch(index));                                          // Show the chinese answer


                if (textTimes == 0)                                                                 // Change the country for each 10 times
                    t.setLanguage(new Locale("en", "GB"));
                else if (textTimes == 5)
                    t.setLanguage(new Locale("en", "US"));
                else if (textTimes == 10)
                    t.setLanguage(new Locale("en", "AS"));
                if (openSpeaking)
                    t.speak(parser.getEn(index), TextToSpeech.QUEUE_ADD, null);
                textTimes = (textTimes + 1) % 15;
            }
        }
    };

    Handler pressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Constants.PRESS_BUTTON) {
                if (showAns) {
                    for (int i = 0; i < 4; i++)
                        ch[i].setText(parser.getch(i) + "\n" + parser.getEn(i));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                textView.setText("");
                if (showQText == false)
                    play.setVisibility(View.INVISIBLE);
                fadeOut();                                                                          // Fade out the button

                boolean is_same;
                if (QType == 0 || (QType == 2 && mixType == 0))
                    is_same = parser.isSame(string[(int) msg.obj], parser.getch(index));
                else
                    is_same = parser.isSame(string[(int) msg.obj], parser.getEn(index));
                if (is_same) {
                    Toast.makeText(MainActivity.this, "Right!", Toast.LENGTH_SHORT).show();
                    YorN = Constants.CORRECT;                                                       // Show correct destop
                } else {
                    Toast.makeText(MainActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
                    YorN = Constants.WRONG;
                }
            }
        }
    };

    Handler recoverHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Constants.RECOVER_FIELD) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                recover();                                                                          // Fill the normal background
                fadeIn();                                                                           // Fade in the button
                if (showQText == false)
                    play.setVisibility(View.VISIBLE);

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadTTSEngine();                                                                            // Load the TTS engine
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadView();                                                                                 // Load View
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Start practice thread
        parser = new Parser();
        parser.readFromFile();
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

    // Implement loading the View
    public void loadView() {
        ch = new Button[4];
        ch[0] = (Button) findViewById(R.id.ch0);                                                    // load view object
        ch[1] = (Button) findViewById(R.id.ch1);
        ch[2] = (Button) findViewById(R.id.ch2);
        ch[3] = (Button) findViewById(R.id.ch3);
        textView = (TextView) findViewById(R.id.ans);

        if (ch[0] == null)
            Log.e("按鈕", "null");
        else
            Log.e("按鈕", "not null");
        ch[0].setOnClickListener(new View.OnClickListener() {                                       // Regist listener
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = Constants.PRESS_BUTTON;
                message.obj = 0;
                pressHandler.sendMessage(message);
            }
        });
        ch[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = Constants.PRESS_BUTTON;
                message.obj = 1;
                pressHandler.sendMessage(message);
            }
        });
        ch[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = Constants.PRESS_BUTTON;
                message.obj = 2;
                pressHandler.sendMessage(message);
            }
        });
        ch[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = Constants.PRESS_BUTTON;
                message.obj = 3;
                pressHandler.sendMessage(message);
            }
        });

        if (showQText == true) {
            play = (ImageView) findViewById(R.id.image);
            play.setVisibility(View.INVISIBLE);
        } else {
            textView.setAlpha(0);
            play = (ImageView) findViewById(R.id.image);
            play.setVisibility(View.VISIBLE);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    t.speak(parser.getEn(index), TextToSpeech.QUEUE_ADD, null);
                }
            });
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            Log.d("設定", "finish");

            //GBt.speak("This is the example of speech sentences english", TextToSpeech.QUEUE_ADD, null);
            //USt.speak("This is the example of speech sentences english", TextToSpeech.QUEUE_ADD, null);
            //ASt.speak("This is the example of speech sentences english", TextToSpeech.QUEUE_ADD, null);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = new Message();
            message.what = Constants.GIVE_QUESTION;
            practiceHandler.sendMessage(message);
        }
    }

    // Implement fading out the bottons
    public void fadeOut() {
        Animation fadeout = new AlphaAnimation(1, 0);
        fadeout.setInterpolator(new AccelerateInterpolator()); //add this
        fadeout.setDuration(500);

        // Declare the listener that set the alpha after animation is ended
        fadeout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                for (int i = 0; i < 4; i++)
                    ch[i].setAlpha(0);
                clearDesk();

                if (YorN == 1)
                    showYes();
                else
                    showNo();

                Message message = new Message();
                message.what = Constants.RECOVER_FIELD;
                recoverHandler.sendMessage(message);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        // Start the animation
        for (int i = 0; i < 4; i++)
            ch[i].startAnimation(fadeout);
    }

    // Implement fading out the bottons
    public void fadeIn() {
        Animation fadein = new AlphaAnimation(0, 1);
        fadein.setInterpolator(new DecelerateInterpolator()); //add this
        fadein.setDuration(500);

        // Declare the listener that set the alpha after animation is ended
        fadein.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Message message = new Message();
                message.what = Constants.GIVE_QUESTION;
                practiceHandler.sendMessage(message);

                for (int i = 0; i < 4; i++)
                    ch[i].setAlpha(1);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        // Start the animation
        for (int i = 0; i < 4; i++)
            ch[i].startAnimation(fadein);
    }

    // Clean Desk
    // ref: https://manijshrestha.wordpress.com/2011/04/05/android-sdk-changing-background-of-a-layout-at-run-time/
    public void clearDesk() {
        View layout = findViewById(R.id.layout);
        layout.setBackgroundResource(R.drawable.desk3);
    }

    // Show correct image
    public void showYes() {
        View layout = findViewById(R.id.layout);
        layout.setBackgroundResource(R.drawable.desk_yes);
    }

    // Show wrong image
    public void showNo() {
        View layout = findViewById(R.id.layout);
        layout.setBackgroundResource(R.drawable.desk_no);
    }

    // Recover the desk with button background
    public void recover() {
        View layout = findViewById(R.id.layout);
        layout.setBackgroundResource(R.drawable.desk2);
    }

    // Listen if the user click the setting venue
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();

            // Press setting buttom
            if (x > 650 && x < 710 && y > 50 && y < 150) {
                startSettingActivity();
            }

            // Press return buttom
            if (x > 0 && x < 85 && y > 50 && y < 150) {
                onDestroy();
                this.finish();
            }
        }
        return super.onTouchEvent(event);
    }

    // Implement go to setting activity
    public void startSettingActivity() {
        // Regist broadcast receiver
        registerReceiver(settingReceiver, new IntentFilter(Constants.SETTING_FILTER));

        // Create intent object
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SettingActivity.class);

        // Bring setting value
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.OPEN_SPEAKING_BUNDLE_KEY, openSpeaking);
        bundle.putBoolean(Constants.SHOW_ANSWER_BUNDLE_KEY, showAns);
        bundle.putBoolean(Constants.SHOW_Q_TEST_BUNDLE_KEY, showQText);
        bundle.putInt(Constants.Q_TYPE_BUNDLE_KEY, QType);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    // Implement boradcast receiver to deal with the setting broadcast
    public BroadcastReceiver settingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            switch (bundle.getInt(Constants.REVISE_INDEX)) {
                case Constants.OPEN_SPEAKING:
                    Log.i(Constants.TAG,
                            (bundle.getBoolean(Constants.REVISE_VALUE) == true ? "開啟" : "關閉"));
                    openSpeaking = bundle.getBoolean(Constants.REVISE_VALUE);
                    break;
                case Constants.SHOW_Q_TEXT:
                    Log.i(Constants.TAG,
                            (bundle.getBoolean(Constants.REVISE_VALUE) == true ? "開啟" : "關閉"));
                    showQText = bundle.getBoolean(Constants.REVISE_VALUE);
                    break;
                case Constants.SHOW_ANSWER:
                    Log.i(Constants.TAG,
                            (bundle.getBoolean(Constants.REVISE_VALUE) == true ? "開啟" : "關閉"));
                    showAns = bundle.getBoolean(Constants.REVISE_VALUE);
                    break;
                case Constants.Q_TYPE:
                    Log.i(Constants.TAG, "選項index: " + bundle.getInt(Constants.REVISE_VALUE));
                    QType = bundle.getInt(Constants.REVISE_VALUE);
                    break;
                default:
                    Log.e(Constants.TAG, "廣播錯誤");
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Disconnect the bounding and register
        try {
            unregisterReceiver(settingReceiver);
        } catch (IllegalArgumentException e) {

        }
        if (t != null) {
            t.stop();
            t.shutdown();
        }
    }
}