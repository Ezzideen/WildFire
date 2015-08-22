package com.bitshifter.wildfire;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class SplashScreenActivity extends Activity {

    private ImageView imageView;
    private TextView textView;
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = SplashScreenActivity.this;
        imageView = (ImageView) findViewById(R.id.ivSplashIcon);
        textView = (TextView) findViewById(R.id.tvSplashText);

        //shake();
        //
        // interpolator();
        initializeEverything();
    }

    private void shake() {
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
        imageView.startAnimation(shake);
        //textView.startAnimation(shake);
    }

    private void initializeEverything() {
        //Generating DB
        MyDBHandler myDBHandler = new MyDBHandler(context,null,null,1);
        FetchCountryData.getCountryByCountryCode(myDBHandler.getWritableDatabase(), "119");
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

    public void interpolator() {
        Animation tvAnim = AnimationUtils.loadAnimation(this, R.anim.interpolar_animation);
        textView.startAnimation(tvAnim);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
