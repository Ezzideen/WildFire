package com.bitshifter.wildfire;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class VictimActivity extends Activity {

    private static final String PREFERENCE = "preference";
    static TextView tvCountry, tvLocation, tvCordinates;
    static TextView tvAmbulance, tvFire, tvPolice;
    static String ambulance, fire, police;
    static RetrievePresentDistressState distressState;
    static Context context;
    private PopupWindow popupWindow;
    static private Button webViewButton;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victim);
        manageTransition();
        context = this;
        tvCountry = (TextView) findViewById(R.id.tvCountry);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvCordinates = (TextView) findViewById(R.id.tvCordinates);

        webViewButton = (Button) findViewById(R.id.bWebView);

        relativeLayout = (RelativeLayout) findViewById(R.id.victimMasterLayout);

        MyDBHandler db = new MyDBHandler(this, null, null, 1);
        FetchCountryData.getCountryCode(this, db.getWritableDatabase(), 0);
//        Toast.makeText(this,FetchCountryData.getCountryByCountryCode(db.getWritableDatabase(),Integer.toString(119)).getId(),Toast.LENGTH_LONG).show();
    }


    private void manageTransition() {

        Slide slideExitTransition = new Slide(Gravity.BOTTOM);
        slideExitTransition.excludeTarget(android.R.id.statusBarBackground, true);
        slideExitTransition.excludeTarget(android.R.id.navigationBarBackground, true);
    }

    private static ProgressDialog progressDialog;

    static String MessageBody;
    public static void setLocationText(Country country, Location location, String countryShortId) {
        tvLocation.setText("Found You!!");
        tvCountry.setText(country.getName());
        tvCordinates.setText("( " + location.getLatitude() + ", " + location.getLongitude() + " )");
        RetrievePresentDistressState distressState = new RetrievePresentDistressState();
        distressState.currentScenario(country.getId(), 0);
        ambulance =  country.getAmbulanceNumber();
        fire = country.getFireNumber();
        police = country.getPoliceNumber();
        MessageBody = "Urgent! Need Help!\n\rI am stuck at"
                + country.getName() + " ( " + location.getLatitude() + ", " + location.getLongitude() + " )";

        //Adding Dialogue Box
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Sending Distress")
                .setMessage("Are you sure you want to send distress message to all your contacts?")
                .setPositiveButton("Send Distress Message", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                     VictimActivity.sendMessages(MessageBody);
                        Toast.makeText(context, "All your Contacts have been informed", Toast.LENGTH_LONG).show();
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

    public static void sendMessages(String MessageBody) {
        ArrayList<String> phoneNumbers = RetrieveContactList.getList(context);
        for (int i = 0; i < phoneNumbers.size(); i++) {
            DeliverMessages.sendSms(phoneNumbers.get(i), MessageBody);
        }

    }

    public void onFabClick(View view) {

        Button bClose;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.popout_emergency,
                (ViewGroup) findViewById(R.id.layout_popout)
        );

        popupWindow = new PopupWindow(layout, 700, 1000, true);
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
        relativeLayout.setVisibility(View.GONE);

        tvAmbulance = (TextView) layout.findViewById(R.id.tvAmbulance);
        tvFire = (TextView) layout.findViewById(R.id.tvFire);
        tvPolice = (TextView) layout.findViewById(R.id.tvPolice);

        tvAmbulance.setText("0000" + ambulance);
        tvFire.setText("0000" + fire);
        tvPolice.setText("0000" + police);

        bClose = (Button) layout.findViewById(R.id.bClosePop);
        bClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                relativeLayout.setVisibility(View.VISIBLE);
            }
        });

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
