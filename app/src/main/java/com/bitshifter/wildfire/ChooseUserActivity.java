package com.bitshifter.wildfire;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;

public class ChooseUserActivity extends Activity {

    private View victimButton, familyButton, volunteerButton;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Activating GPS
        GPSLocator gps = new GPSLocator(this);
        if(!gps.isGPSEnabled){
            gps.showSettingsAlert();
        }

        setContentView(R.layout.activity_choose_user);
        victimButton = findViewById(R.id.fb_victim);
        familyButton = findViewById(R.id.fb_family);
        volunteerButton = findViewById(R.id.fb_volunteer);
        header = findViewById(R.id.activity_transition_header);

        manageTransition();

    }

    void manageTransition() {
        Slide slideExitTransition = new Slide(Gravity.BOTTOM);
        slideExitTransition.excludeTarget(android.R.id.navigationBarBackground, true);
        slideExitTransition.excludeTarget(android.R.id.statusBarBackground, true);
        slideExitTransition.excludeTarget(R.id.activity_transition_header, true);
        getWindow().setExitTransition(slideExitTransition);
    }

    public void onVictimClick(View v) {
        Intent intent = new Intent(this, VictimActivity.class);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                this, Pair.create(victimButton, "fab"), Pair.create(header, "holder1")
        );
        startActivity(intent, transitionActivityOptions.toBundle());
    }

    public void onFamilyClick(View v) {
        Intent intent = new Intent(this, FamilyActivity.class);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                this, Pair.create(familyButton, "fab"), Pair.create(header, "holder1")
        );
        startActivity(intent, transitionActivityOptions.toBundle());
    }

    public void onVolunteerClick(View v) {
        Intent intent = new Intent(this, VolunteerActivity.class);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                this, Pair.create(volunteerButton, "fab"), Pair.create(header, "holder1")
        );
        startActivity(intent, transitionActivityOptions.toBundle());
    }
}
