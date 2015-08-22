package com.bitshifter.wildfire;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class ChooseUserActivity extends Activity {

    private View victimButton, familyButton, volunteerButton;
    private View header;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Activating GPS
        GPSLocator gps = new GPSLocator(this);
        if (!gps.isGPSEnabled) {
            gps.showSettingsAlert();
        }

        setContentView(R.layout.activity_choose_user);
        victimButton = findViewById(R.id.fb_victim);
        familyButton = findViewById(R.id.fb_family);
        volunteerButton = findViewById(R.id.fb_volunteer);
        header = findViewById(R.id.activity_transition_header);
        context = this;

        manageTransition();

    }

    void manageTransition() {
        Slide slideExitTransition = new Slide(Gravity.BOTTOM);
        slideExitTransition.excludeTarget(android.R.id.navigationBarBackground, true);
        slideExitTransition.excludeTarget(android.R.id.statusBarBackground, true);
        slideExitTransition.excludeTarget(R.id.activity_transition_header, true);
        getWindow().setExitTransition(slideExitTransition);
    }


    public void proceedToVictimActivity() {
        Intent intent = new Intent(this, VictimActivity.class);
        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                this, Pair.create(victimButton, "fab"), Pair.create(header, "holder1")
        );
        context.startActivity(intent, transitionActivityOptions.toBundle());
    }

    public void onVictimClick(View v) {

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Sending Distress")
                .setMessage("Are you sure you want to send distress message to all your contacts?")
                .setPositiveButton("Send Distress Call", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        proceedToVictimActivity();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                })
                .setIcon(R.drawable.alert)
                .show();

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

    public void openAboutUs(View view) {
        Intent intent = new Intent(this, AboutUsActivity.class);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                this, Pair.create(header, "holder1")
        );
        startActivity(intent, transitionActivityOptions.toBundle());
    }
}
