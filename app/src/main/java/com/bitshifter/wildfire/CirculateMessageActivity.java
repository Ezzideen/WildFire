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
import android.widget.Toast;

import java.util.ArrayList;

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
                for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                    if (cursor.getColumnName(idx).equals("body") && cursor.getString(idx).contains("Twilio")) {
                        return cursor.getString(idx);
                    }
                }
            } while (cursor.moveToNext());

        }

        return "No message found!!";

    }

    public void circulateMessage(View view) {
        String text = etMessage.getText().toString().trim();
        if (!text.equals("No message found!!") && !text.equals("")) {
        CirculateMessageActivity.sendMessages(text);
            Toast.makeText(context, "Message has been forwarded to all your Contacts", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Please check the input message", Toast.LENGTH_LONG).show();
        }

    }

    private static void sendMessages(String MessageBody) {
        ArrayList<String> phoneNumbers = RetrieveContactList.getList(context);
        for (int i = 0; i < phoneNumbers.size(); i++) {
            DeliverMessages.sendSms(phoneNumbers.get(i), MessageBody);
        }

    }
}
