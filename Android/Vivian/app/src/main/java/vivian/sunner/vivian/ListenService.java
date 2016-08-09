package vivian.sunner.vivian;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class ListenService extends Service implements TextToSpeech.OnInitListener {
    static Parser parser;
    public boolean stopFlag = true;                                                                 // The flag to stop the TTS
    public static TextToSpeech t;

    // Broadcast Receiver implementation
    public BroadcastReceiver listenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*
            if (intent.getBundleExtra(Constants.DIR).getInt(Constants.DIRKEY) == Constants.A2S) {
                switch (intent.getBundleExtra(Constants.CMD).getInt(Constants.CMDKEY)) {
                    case Constants.STOP:
                        stopFlag = false;
                        break;
                    case Constants.SLOW:
                        t.setSpeechRate(0.8f);
                        break;
                    case Constants.NORMAL:
                        t.setSpeechRate(1);
                        break;
                    default:
                        Log.e(Constants.TAG, "異常廣播");
                }
            }
            */
            switch (intent.getExtras().getInt(Constants.CMD)) {
                case Constants.STOP:
                    stopFlag = false;
                    break;
                case Constants.SLOW:
                    t.setSpeechRate(0.8f);
                    break;
                case Constants.NORMAL:
                    t.setSpeechRate(1);
                    break;
                default:
                    Log.e(Constants.TAG, "異常廣播");
            }
        }
    };

    // Constructor
    public ListenService() {
        parser = new Parser();
        parser.readFromFile();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        loadTTSEngine();                                                                            // Load the TTS engine
        registerReceiver(listenReceiver, new IntentFilter(Constants.LISTEN_FILTER));                // build receiver to get command

        return super.onStartCommand(intent, flags, startId);
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
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    int size = parser.getNumberOfWord();

                    // Determine the direction
                    boolean reverse = (Math.random() > 0.5 ? true : false);
                    boolean randomStart = (Math.random() > 0.5 ? true : false);                     // 1/2 pr to shuffle the start position
                    if (reverse) {
                        for (int i = (randomStart ? 0 : (int) (Math.random() * (size - 1))); i < size && stopFlag == true; i++)
                            speakByIndex(i);
                    } else {
                        for (int i = (randomStart ? size - 1 : (int) (Math.random() * (size - 1))); i > 0 && stopFlag == true; i--)
                            speakByIndex(i);
                    }
                }
            }.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        t.stop();
        t.shutdown();
        unregisterReceiver(listenReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
                    break;
                default:
                    Log.e(Constants.TAG, "異常口音");
            }
        }

        // Speak the word
        t.speak(parser.getEn_Read(i), TextToSpeech.QUEUE_ADD, null);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
