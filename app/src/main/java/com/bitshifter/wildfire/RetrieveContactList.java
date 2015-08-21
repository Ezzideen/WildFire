package com.bitshifter.wildfire;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.TextView;


class RetrieveContactList {
    public void getList(Context context, TextView textView){
        Cursor cursor = null;
        String res = "";
        try {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            int contactIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
            int nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int phoneNumberIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//            int photoIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID);
            cursor.moveToFirst();
            do {
                String idContact = cursor.getString(contactIdIdx);
                String name = cursor.getString(nameIdx);
                String phoneNumber = cursor.getString(phoneNumberIdx);
                res = res+" "+idContact+" "+name+" "+phoneNumber+"\n";
            } while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            textView.setText(res);
        }
    }
}
