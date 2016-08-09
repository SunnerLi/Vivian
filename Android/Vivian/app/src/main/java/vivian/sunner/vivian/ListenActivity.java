package vivian.sunner.vivian;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

/*
    Add by sunner at 2016/8/9
    The Listen mode has two direction.
 */
public class ListenActivity extends AppCompatActivity  {
    // Fundemential
    String TAG = "listen";

    // Custom Widget
    public Switch ctrl, slow;

    // Flag
    public boolean haveSpeak = false;                                                               // The flag to record if allocate TTS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ctrl = (Switch) findViewById(R.id.ctrl);                                                    // load view object
        slow = (Switch) findViewById(R.id.slow);
        ctrl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                slow.setVisibility(View.VISIBLE);
                if (isChecked) {
                    Intent intent = new Intent();
                    intent.setClass(ListenActivity.this, ListenService.class);
                    startService(intent);
                    haveSpeak = true;
                } else {
                    // broadcast for stop command
                    Intent intent = new Intent();
                    intent.setAction(Constants.LISTEN_FILTER);
                    Bundle bundle = new Bundle();                                                   // put the command integer
                    bundle.putInt(Constants.CMD, Constants.STOP);
                    intent.putExtras(bundle);
                    sendBroadcast(intent);
                }
            }
        });
        slow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // broadcast for slow command
                    Intent intent = new Intent();
                    intent.setAction(Constants.LISTEN_FILTER);
                    Bundle bundle = new Bundle();                                                   // put the command integer
                    bundle.putInt(Constants.CMD, Constants.SLOW);
                    intent.putExtras(bundle);
                    sendBroadcast(intent);
                } else {
                    // broadcast for normalize command
                    Intent intent = new Intent();
                    intent.setAction(Constants.LISTEN_FILTER);
                    Bundle bundle = new Bundle();                                                   // put the command integer
                    bundle.putInt(Constants.CMD, Constants.NORMAL);
                    intent.putExtras(bundle);
                    sendBroadcast(intent);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (haveSpeak) {
            Intent intent = new Intent();
            intent.setClass(ListenActivity.this, ListenService.class);
            stopService(intent);
        }
    }
}
