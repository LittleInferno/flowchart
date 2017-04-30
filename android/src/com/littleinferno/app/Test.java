package com.littleinferno.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.littleinferno.flowchart.R;
import com.ogaclejapan.arclayout.ArcLayout;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        ArcLayout arcLayout = (ArcLayout) findViewById(R.id.arc);




    }
}
