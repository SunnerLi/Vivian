package vivian.sunner.vivian;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Locale;

public class ListenService extends Service {
    static Parser parser;
    public boolean stopFlag = true;                                                                 // The flag to stop the TTS

    // Broadcast Receiver implementation
    public BroadcastReceiver listenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBundleExtra(Constants.DIR).getInt(Constants.DIRKEY) == Constants.A2S) {
                switch (intent.getBundleExtra(Constants.CMD).getInt(Constants.CMDKEY)) {
                    case Constants.STOP:
                        stopFlag = false;
                        break;
                    case Constants.SLOW:
                        ListenActivity.t.setSpeechRate(0.8f);
                        break;
                    case Constants.NORMAL:
                        ListenActivity.t.setSpeechRate(1);
                        break;
                    default:
                        Log.e(Constants.TAG, "異常廣播");
                }
            }
        }
    };

    public ListenService() {
        parser = new Parser();
        parser.readFromFile();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver(listenReceiver, new IntentFilter(Constants.LISTEN_FILTER));
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
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ListenActivity.t.stop();
        ListenActivity.t.shutdown();
        unregisterReceiver(listenReceiver);
    }

    @Nullable
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
                    ListenActivity.t.setLanguage(new Locale("en", "GB"));
                    break;
                case 1:
                    ListenActivity.t.setLanguage(new Locale("en", "US"));
                    break;
                case 2:
                    ListenActivity.t.setLanguage(new Locale("en", "AS"));
            }
        }

        // Speak the word
        ListenActivity.t.speak(parser.getEn_Read(i), TextToSpeech.QUEUE_ADD, null);

        // Send the word to show
        Intent intent = new Intent();
        intent.setAction(Constants.LISTEN_FILTER);
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DIRKEY, Constants.S2A);
        intent.putExtra(Constants.DIR, bundle);
        Bundle bundle1 = new Bundle();
        bundle1.putString(Constants.TXTKEY, parser.getCh_Read(i));
        intent.putExtra(Constants.TXT, bundle1);
        sendBroadcast(intent);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
