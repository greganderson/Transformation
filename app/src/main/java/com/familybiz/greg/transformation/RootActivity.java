package com.familybiz.greg.transformation;

import android.app.Activity;
import android.os.Bundle;


public class RootActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TransformView(this));
    }
}
