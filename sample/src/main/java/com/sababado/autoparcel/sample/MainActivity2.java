package com.sababado.autoparcel.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity2 extends Activity {
    private static final String TAG = MainActivity2.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);

        final Model obj = getIntent().getParcelableExtra("arg");
        Log.v(TAG, "after: " + obj.toString());
    }
}
