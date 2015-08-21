package com.bitshifter.wildfire;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;

public class VolunteerActivity extends Activity {

    private View header;
    private View fabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        header = findViewById(R.id.activity_transition_header);
        fabButton = findViewById(R.id.fab_button);
        manageTransition();
    }

    private void manageTransition() {

        Slide slideExitTransition = new Slide(Gravity.BOTTOM);
        slideExitTransition.excludeTarget(android.R.id.statusBarBackground, true);
        slideExitTransition.excludeTarget(android.R.id.navigationBarBackground, true);
    }

    public void provideAssistance(View view) {

        Intent intent = new Intent(this, ProvideAssistanceActivity.class);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                this, Pair.create(fabButton, "fab"), Pair.create(header, "holder1")
        );
        startActivity(intent, transitionActivityOptions.toBundle());
    }

    public void circulateMessage(View view) {

        Intent intent = new Intent(this, CirculateMessageActivity.class);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                this, Pair.create(fabButton,"fab"), Pair.create(header, "holder1")
        );
        startActivity(intent, transitionActivityOptions.toBundle());
    }

    public void onFabClick(View view) {
        Intent intent = new Intent(this, JobActivity.class);
        startActivity(intent);
    }
}
