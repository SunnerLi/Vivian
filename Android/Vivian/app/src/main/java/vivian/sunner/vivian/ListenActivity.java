package vivian.sunner.vivian;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import java.io.Serializable;
import java.util.Locale;

/*
    Add by sunner at 2016/8/9
    The Listen mode has two direction.
 */
public class ListenActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    // Fundemential
    String TAG = "listen";
    public static TextToSpeech t;

    // Custom Widget
    RollTextViewHandler rollTextViewHandler = new RollTextViewHandler();
    public TextView roll;
    public Switch ctrl, slow;

    // Flag
    public boolean haveSpeak = false;                                                               // The flag to record if allocate TTS

    // Broadcast Receiver implementation
    public BroadcastReceiver updateUIReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Show the chinese
            if (intent.getBundleExtra(Constants.DIR).getInt(Constants.DIRKEY) == Constants.S2A) {
                rollTextViewHandler.add(intent.getBundleExtra(Constants.TXT).getString(Constants.TXTKEY));
                Message message = new Message();
                changeTextHandler.sendMessage(message);
            }
        }
    };

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
                    Intent intent = new Intent();
                    intent.setClass(ListenActivity.this, ListenService.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.DIRKEY, Constants.A2S);
                    intent.putExtra(Constants.DIR, bundle);
                    startService(intent);
                    registerReceiver(updateUIReceiver, new IntentFilter(Constants.LISTEN_FILTER));
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Constants.LISTEN_FILTER);
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.DIRKEY, Constants.A2S);
                    intent.putExtra(Constants.DIR, bundle);
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt(Constants.CMDKEY, Constants.STOP);
                    intent.putExtra(Constants.CMD, bundle1);
                    sendBroadcast(intent);
                }
            }
        });
        slow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent();
                    intent.setAction(Constants.LISTEN_FILTER);
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.DIRKEY, Constants.A2S);
                    intent.putExtra(Constants.DIR, bundle);
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt(Constants.CMDKEY, Constants.SLOW);
                    intent.putExtra(Constants.CMD, bundle1);
                    sendBroadcast(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Constants.LISTEN_FILTER);
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.DIRKEY, Constants.A2S);
                    intent.putExtra(Constants.DIR, bundle);
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt(Constants.CMDKEY, Constants.NORMAL);
                    intent.putExtra(Constants.CMD, bundle1);
                    sendBroadcast(intent);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(updateUIReceiver);
    }
}
