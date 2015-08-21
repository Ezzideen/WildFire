package com.bitshifter.wildfire;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;


public class FamilyActivity extends Activity {

    private LinearLayout rowContainer;
    private static final int SCALE_DELAY = 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        rowContainer = (LinearLayout) findViewById(R.id.ll_container);

        manageTransition();
    }

    private void manageTransition() {

        Slide slideExitTransition = new Slide(Gravity.BOTTOM);
        slideExitTransition.excludeTarget(android.R.id.statusBarBackground, true);
        slideExitTransition.excludeTarget(android.R.id.navigationBarBackground, true);

        getWindow().getEnterTransition().addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                getWindow().getEnterTransition().removeListener(this);

                for(int i = 0; i< rowContainer.getChildCount(); i++) {
                    View rowView = rowContainer.getChildAt(i);
                    rowView.animate().setStartDelay(i*SCALE_DELAY).scaleX(1).scaleY(1);
                }
            }

            @Override
            public void onTransitionEnd(Transition transition) {}
            @Override
            public void onTransitionCancel(Transition transition) {}
            @Override
            public void onTransitionPause(Transition transition) {}
            @Override
            public void onTransitionResume(Transition transition) {}
        });

    }

    @Override
    public void onBackPressed() {


        for(int i=0; i< rowContainer.getChildCount(); i++) {
            View rowView = rowContainer.getChildAt(i);
            rowView.animate()
                    .setStartDelay(i*SCALE_DELAY)
                    .scaleX(0).scaleY(0)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {}

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            finishAfterTransition();
                        }
                        @Override
                        public void onAnimationCancel(Animator animation) {}
                        @Override
                        public void onAnimationRepeat(Animator animation) {}
                    });
        }
    }

    public void onFabClick(View v){

    }
}