package com.bitshifter.wildfire;

import android.app.Activity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;

public class VolunteerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        manageTransition();
    }

    private void manageTransition() {

        Slide slideExitTransition = new Slide(Gravity.BOTTOM);
        slideExitTransition.excludeTarget(android.R.id.statusBarBackground, true);
        slideExitTransition.excludeTarget(android.R.id.navigationBarBackground, true);
    }

}
