package com.bitshifter.wildfire;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by rohit on 20/8/15.
 */
public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME ="wildfire.db";
    static final String TABLE_COUNTRY = "country";
    static final String COUNTRY_COLUMN_CODE = "code";
    static final String COUNTRY_COLUMN_NAME = "name";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
        Log.i("OnCreate", "COnstructor DB");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Generating Country Table
        String queryCountryTable = "CREATE TABLE "+TABLE_COUNTRY + " (" +
                COUNTRY_COLUMN_CODE + " INTEGER PRIMARY KEY, "+
                COUNTRY_COLUMN_NAME + " TEXT " +
                ");";
        db.execSQL(queryCountryTable);

        FetchCountryData.insertCountriesIntoDatabase(db);
        Log.i("OnCreate", "CREATING DB");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO WILL DECIDE LATER
    }
}
