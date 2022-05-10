package com.suny.client;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class StubActivity  extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TextView)findViewById(R.id.btn_click)).setText("stub activity");
    }
}
