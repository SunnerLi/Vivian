package vivian.sunner.vivian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

public class SettingActivity extends Activity {

    public Spinner questionTypeSpinner;
    public ArrayAdapter<String> typeList;
    public Switch openSpeakingSwitch, showQTextSwitch, showAnsSwitch;

    Runnable listenerRunnable = new Runnable() {
        @Override
        public void run() {

            questionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.setAction(Constants.SETTING_FILTER);
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.REVISE_INDEX, Constants.Q_TYPE);
                    bundle.putInt(Constants.REVISE_VALUE, position);
                    intent.putExtras(bundle);
                    sendBroadcast(intent);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            openSpeakingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Intent intent = new Intent();
                    intent.setAction(Constants.SETTING_FILTER);
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.REVISE_INDEX, Constants.OPEN_SPEAKING);
                    bundle.putBoolean(Constants.REVISE_VALUE, isChecked);
                    intent.putExtras(bundle);
                    sendBroadcast(intent);
                }
            });
            showQTextSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Intent intent = new Intent();
                    intent.setAction(Constants.SETTING_FILTER);
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.REVISE_INDEX, Constants.SHOW_Q_TEXT);
                    bundle.putBoolean(Constants.REVISE_VALUE, isChecked);
                    intent.putExtras(bundle);
                    sendBroadcast(intent);
                }
            });
            showAnsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Intent intent = new Intent();
                    intent.setAction(Constants.SETTING_FILTER);
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.REVISE_INDEX, Constants.SHOW_ANSWER);
                    bundle.putBoolean(Constants.REVISE_VALUE, isChecked);
                    intent.putExtras(bundle);
                    sendBroadcast(intent);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


    }

    @Override
    protected void onStart() {
        super.onStart();

        // load vier object
        questionTypeSpinner = (Spinner) findViewById(R.id.spinner);
        openSpeakingSwitch = (Switch) findViewById(R.id.switch1);
        showQTextSwitch = (Switch) findViewById(R.id.switch2);
        showAnsSwitch = (Switch) findViewById(R.id.switch3);

        // Initialize spinner
        typeList = new ArrayAdapter<String>(SettingActivity.this,
                android.R.layout.simple_spinner_dropdown_item, Constants.questionType);
        questionTypeSpinner.setAdapter(typeList);

        // Set listener
        new Thread(listenerRunnable).start();

        // Recover the setting value
        Bundle bundle = this.getIntent().getExtras();
        boolean openSpeaking = bundle.getBoolean(Constants.OPEN_SPEAKING_BUNDLE_KEY);
        boolean showQText = bundle.getBoolean(Constants.SHOW_Q_TEST_BUNDLE_KEY);
        boolean showAns = bundle.getBoolean(Constants.SHOW_ANSWER_BUNDLE_KEY);
        int QType = bundle.getInt(Constants.Q_TYPE_BUNDLE_KEY);

        // Set the view object status
        openSpeakingSwitch.setChecked(openSpeaking);
        showQTextSwitch.setChecked(showQText);
        showAnsSwitch.setChecked(showAns);
        questionTypeSpinner.setId(QType);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    // Listen if the user click the return button
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();

            // Press return buttom
            if (x > 0 && x < 85 && y > 50 && y < 150) {
                onDestroy();
                this.finish();
            }
        }
        return super.onTouchEvent(event);
    }
}
