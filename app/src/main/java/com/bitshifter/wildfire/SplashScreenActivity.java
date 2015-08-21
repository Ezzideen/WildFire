package com.bitshifter.wildfire;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class SplashScreenActivity extends Activity {

    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = SplashScreenActivity.this;
        initializeEverything();
    }

    private void initializeEverything() {

        //Generating DB
        MyDBHandler myDBHandler = new MyDBHandler(context,null,null,1);
        FetchCountryData.getCountryByCountryCode(myDBHandler.getWritableDatabase(),"119");
        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(context,ChooseUserActivity.class);
                    startActivity(intent);
                }
            }
        };
        timer.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
