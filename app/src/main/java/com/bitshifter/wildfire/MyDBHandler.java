package com.bitshifter.wildfire;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by rohit on 20/8/15.
 */
public class MyDBHandler extends SQLiteOpenHelper {


    private static final String TAG = "WILDFIRE";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME ="wildfire.db";
    static final String TABLE_COUNTRY = "country";
    static final String COUNTRY_COLUMN_CODE = "_id";
    static final String COUNTRY_COLUMN_NAME = "name";
    static final String COUNTRY_EMERGENCY_FIRE_NUMBER = "fire";
    static final String COUNTRY_EMERGENCY_POLICE_NUMBER = "police";
    static final String COUNTRY_EMERGENCY_AMBULANCE_NUMBER = "ambulance";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
        Log.v(TAG,"MyDBHandler Constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG,"MyDBHandler onCreate");
        //Generating Country Table
        String queryCountryTable = "CREATE TABLE "+TABLE_COUNTRY + " (" +
                COUNTRY_COLUMN_CODE + " INTEGER PRIMARY KEY, "+
                COUNTRY_COLUMN_NAME + " TEXT ," +
                COUNTRY_EMERGENCY_POLICE_NUMBER + " TEXT ," +
                COUNTRY_EMERGENCY_AMBULANCE_NUMBER+ " TEXT ,"+
                COUNTRY_EMERGENCY_FIRE_NUMBER+ " TEXT "+
                ");";
        db.execSQL(queryCountryTable);
        FetchCountryData.insertCountriesIntoDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(TAG,"MyDBHandler onUpgrade");
        //TODO WILL DECIDE LATER
    }
}
