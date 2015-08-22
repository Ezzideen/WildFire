package com.bitshifter.wildfire;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;


public class FamilyActivity extends Activity {

    private static final String PREFERENCE = "preference";
    private LinearLayout rowContainer;
    private static final int SCALE_DELAY = 120;
    private static EditText etName, etLocation, etPhone;
    static private Button webViewButton;
    static Context context;
    public static final String EXTRA_HTML = "com.bitshifter.wildfire.familyActivity.EXTRA_HTML";
    static String MessageBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        rowContainer = (LinearLayout) findViewById(R.id.ll_container);
        //
        etLocation = (EditText) findViewById(R.id.etLocation);
        etName = (EditText) findViewById(R.id.etName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        context = this;
        webViewButton = (Button) findViewById(R.id.bWebView);
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

                for (int i = 0; i < rowContainer.getChildCount(); i++) {
                    View rowView = rowContainer.getChildAt(i);
                    rowView.animate().setStartDelay(i * SCALE_DELAY).scaleX(1).scaleY(1);
                }
            }

            @Override
            public void onTransitionEnd(Transition transition) {
            }

            @Override
            public void onTransitionCancel(Transition transition) {
            }

            @Override
            public void onTransitionPause(Transition transition) {
            }

            @Override
            public void onTransitionResume(Transition transition) {
            }
        });

    }

    @Override
    public void onBackPressed() {


        for (int i = 0; i < rowContainer.getChildCount(); i++) {
            View rowView = rowContainer.getChildAt(i);
            rowView.animate()
                    .setStartDelay(i * SCALE_DELAY)
                    .scaleX(0).scaleY(0)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            finishAfterTransition();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
        }
    }

    public void onFabClick(View v) {
        String countryName = etLocation.getText().toString().trim();
        String name = etName.getText().toString().trim();
        MyDBHandler db = new MyDBHandler(this, null, null, 1);
        final int countryCode = FetchCountryData.getCountryCodeFromCountryName(db.getWritableDatabase(), countryName);
        final Country country = FetchCountryData.getCountryByCountryCode(db.getWritableDatabase(), Integer.toString(countryCode));
        RetrievePresentDistressState retrievePresentDistressState = new RetrievePresentDistressState();
        retrievePresentDistressState.currentScenario(countryCode, 1);
        MessageBody = "";
        if (countryCode == 0)
            Toast.makeText(context, "Please Enter the Country Name correctly!", Toast.LENGTH_LONG).show();
        else {
            MessageBody = "Urgent! Need Help!\n\r" + etName.getText() + " is stuck in"
                    + etLocation.getText() + "\n\rPlease provide assistance in any way possible and Please spread the word around.";

            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setTitle("Sending Distress")
                    .setMessage("Are you sure you want to send distress message to all your contacts?")
                    .setPositiveButton("Send Distress Message", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                                    FamilyActivity.sendMessages(MessageBody);
                            Toast.makeText(context, "All your Contacts have been informed", Toast.LENGTH_LONG).show();
                            MessageBody = "Here are some Emergency Numbers for your area!\n\rAmbulance: " + country.getAmbulanceNumber()
                                    + "\n\rFire: " + country.getFireNumber() + "\n\rPolice: " + country.getPoliceNumber();

                            if (!etPhone.getText().toString().trim().equals("")) {
                                            DeliverMessages.sendSms(etPhone.getText().toString(), MessageBody);
                                Toast.makeText(context, "Emergency Contacts delivered to Victim", Toast.LENGTH_LONG).show();
                            }
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


    }


    private static void sendMessages(String MessageBody) {
        ArrayList<String> phoneNumbers = RetrieveContactList.getList(context);
        for (int i = 0; i < phoneNumbers.size(); i++) {
            DeliverMessages.sendSms(phoneNumbers.get(i), MessageBody);
        }
    }

    public static void callWebView(String html) {
        final String passHtml = html;

        webViewButton.setVisibility(View.VISIBLE);
        webViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);

                SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE, MODE_PRIVATE).edit();
                editor.putString("html", passHtml);
                editor.commit();

                context.startActivity(intent);
            }
        });

    }

}