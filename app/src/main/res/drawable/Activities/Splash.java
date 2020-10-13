package com.ranafkd.myproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.ranafkd.myproject.R;

public class Splash extends AppCompatActivity {

    private static int TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(com.ranafkd.myproject.Activities.Splash.this, com.ranafkd.myproject.Activities.Welcome.class);
                startActivity(mainIntent);
                finish();
            }
        }, TIME_OUT);
    }
}
