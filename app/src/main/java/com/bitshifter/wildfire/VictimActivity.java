package com.bitshifter.wildfire;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class VictimActivity extends Activity {

    static TextView tvCountry, tvLocation;
    static TextView tvAmbulance, tvFire, tvPolice;
    static String ambulance, fire, police;
    static RetrievePresentDistressState distressState;
    static Context context;
    private PopupWindow popupWindow;
    static private Button webViewButton;
    public static final String EXTRA_HTML = "com.bitshifter.wildfire.victimActivity.EXTRA_HTML";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victim);
        manageTransition();
        context=this;
        tvCountry = (TextView) findViewById(R.id.tvCountry);
        tvLocation = (TextView) findViewById(R.id.tvLocation);

        webViewButton = (Button) findViewById(R.id.bWebView);

        MyDBHandler db = new MyDBHandler(this,null,null,1);
        FetchCountryData.getCountryCode(this,db.getWritableDatabase());
//        Toast.makeText(this,FetchCountryData.getCountryByCountryCode(db.getWritableDatabase(),Integer.toString(119)).getId(),Toast.LENGTH_LONG).show();
    }


    private void manageTransition() {

        Slide slideExitTransition = new Slide(Gravity.BOTTOM);
        slideExitTransition.excludeTarget(android.R.id.statusBarBackground, true);
        slideExitTransition.excludeTarget(android.R.id.navigationBarBackground, true);
    }

    public static void setLocationText(Country country, Location location) {
        ArrayList<String> phoneNumbers = RetrieveContactList.getList(context);
        String s = country.getName() + "\n" + location.getLatitude() + " , "+  location.getLongitude();
        tvLocation.setText("Found You!!");
        tvCountry.setText(s);
        RetrievePresentDistressState distressState = new RetrievePresentDistressState();
        distressState.currentScenario(country.getId());
        ambulance = country.getAmbulanceNumber();
        fire = country.getFireNumber();
        police = country.getPoliceNumber();

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

        tvAmbulance = (TextView) layout.findViewById(R.id.tvAmbulance);
        tvFire = (TextView) layout.findViewById(R.id.tvFire);
        tvPolice = (TextView) layout.findViewById(R.id.tvPolice);

        tvAmbulance.setText("0000"+ambulance);
        tvFire.setText("0000" + fire);
        tvPolice.setText("0000"+police);

        bClose = (Button) layout.findViewById(R.id.bClosePop);
        bClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
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
                intent.putExtra(EXTRA_HTML, passHtml);
                context.startActivity(intent);
            }
        });

    }

}
