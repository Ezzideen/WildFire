package com.bitshifter.wildfire;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CirculateMessageActivity extends Activity {

    static Context context;
    EditText etMessage;
    Button fabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circulate_message);
        context = this;
        etMessage = (EditText) findViewById(R.id.etMessageSetter);
        manageTransition();

        String message = getFirstTwilioSMS();
        etMessage.setText(message);
    }


    private void manageTransition() {
        Slide slideExitTransition = new Slide(Gravity.BOTTOM);
        slideExitTransition.excludeTarget(android.R.id.statusBarBackground, true);
        slideExitTransition.excludeTarget(android.R.id.navigationBarBackground, true);
    }

    private String getFirstTwilioSMS() {
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        if (cursor.moveToFirst()) { // must check the result to prevent exception

            do {
                for(int idx=0;idx<cursor.getColumnCount();idx++)
                {
                    if(cursor.getColumnName(idx).equals("body") && cursor.getString(idx).contains("Twilio") ){
                        return cursor.getString(idx);
                    }
                }
            } while (cursor.moveToNext());

        }

        return "No message found!!";

    }

    public void circulateMessage(View view) {

    }

}
