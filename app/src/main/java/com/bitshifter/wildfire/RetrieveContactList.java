package com.bitshifter.wildfire;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;


class RetrieveContactList {
    public static ArrayList<String> getList(Context context){
        Cursor cursor = null;
        ArrayList<String> strings = new ArrayList<>();
        try {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            int contactIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
            int nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int phoneNumberIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//            int photoIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID);
            cursor.moveToFirst();
            do {
                String phoneNumber = cursor.getString(phoneNumberIdx);
                strings.add(phoneNumber);
            } while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return strings;
    }
}
