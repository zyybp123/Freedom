package com.bpz.freedom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Example of a call to a native method
        final TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this,HomeActivity.class));
            }
        });

        new Timer().schedule(new TimerTask() {
            int count;

            @Override
            public void run() {
                if (count >= 30){
                    cancel();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        tv.setText(String.format(Locale.getDefault(), "add%d", count++));
                    }
                });
            }
        }, 1000, 2000);

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
