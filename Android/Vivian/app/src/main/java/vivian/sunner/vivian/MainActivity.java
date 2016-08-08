package vivian.sunner.vivian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button practiceChoose, listen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadView();
    }

    // Implement loading the View
    public void loadView() {
        practiceChoose = (Button) findViewById(R.id.practiceChoose);                                           // load view object
        listen = (Button) findViewById(R.id.listen);
        practiceChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, PracticeChooseActivity.class);
                startActivity(intent);
            }
        });
        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ListenActivity.class);
                startActivity(intent);
            }
        });
    }
}
