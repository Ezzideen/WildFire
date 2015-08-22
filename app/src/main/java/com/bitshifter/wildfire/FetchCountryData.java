package com.bitshifter.wildfire;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rohit on 20/8/15.
 */
public class FetchCountryData {

    private static final String TAG = "WILDFIRE";
    private static JSONObject json;
    private static SQLiteDatabase database;
    private static String country;
    private static String countryShortId;
    private static ArrayList<Country> countries;
    //Fetching Json List of countries from ReliefWeb
    public static void insertCountriesIntoDatabase(SQLiteDatabase db){
        Log.v(TAG,"FetchCountryData insertCountriesIntoDatabase");
        database = db;
        RequestParams params = new RequestParams();
        params.put("offset","0");
        params.put("limit", "250");

        ReliefWebRestClient.get("countries", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "inside ReliefWebRestClient.get");
                JSONObject response = null;
                try {
                    response = new JSONObject(new String(responseBody));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                countries = parseJsonCountryList(response);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
                insertCountries(database, countries);
            }
        });
//        ReliefWebRestClient.get("countries", params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Log.i(TAG,"inside ReliefWebRestClient.get");
//                ArrayList<Country> countries = parseJsonCountryList(response);
//                insertCountries(database, countries);
//            }
//
//        });
    }

    //Parsing Json data and making an arraylist of Country
    private static ArrayList<Country> parseJsonCountryList(JSONObject json){
        Log.v(TAG,"FetchCountryData parseJsonCountryList");
        ArrayList<Country> countryList = new ArrayList<Country>();
        try {
            JSONArray jsonArray = json.getJSONArray("data");
            int count = json.getInt("count");
            for (int i=0; i < count ;i++){
                Country country;
                int id = jsonArray.getJSONObject(i).getInt("id");
                String name = jsonArray.getJSONObject(i).getJSONObject("fields").getString("name");
                try{
                    String fireNumber = EmergencyContacts.getContacts().getJSONObject(name).getString("fire");
                    String policeNumber = EmergencyContacts.getContacts().getJSONObject(name).getString("police");
                    String ambulanceNumber = EmergencyContacts.getContacts().getJSONObject(name).getString("ambulance");
                    country = new Country(id, name, fireNumber, policeNumber, ambulanceNumber);
                }catch (JSONException e){
                    country = new Country(id, name, "", "", "");
                }
                countryList.add(country);
            }
        }catch (JSONException e){
            Log.e(TAG,e.toString());
        }
        return countryList;
    }

    private static void insertCountries(SQLiteDatabase database, ArrayList<Country> countries){
        Log.v(TAG, "FetchCountryData insertCountries");
        for (Country country : countries){
            ContentValues values = new ContentValues();
            values.put(MyDBHandler.COUNTRY_COLUMN_CODE,country.getId());
            values.put(MyDBHandler.COUNTRY_COLUMN_NAME,country.getName());
            values.put(MyDBHandler.COUNTRY_EMERGENCY_FIRE_NUMBER,country.getFireNumber());
            values.put(MyDBHandler.COUNTRY_EMERGENCY_POLICE_NUMBER,country.getPoliceNumber());
            values.put(MyDBHandler.COUNTRY_EMERGENCY_AMBULANCE_NUMBER, country.getAmbulanceNumber());
            Log.i(TAG, values.toString());
            try {
                long l = database.insertOrThrow(MyDBHandler.TABLE_COUNTRY, null, values);
                Log.i(TAG, "INSERT :" + l);
            }catch (SQLException e){
                Log.e(TAG,e.toString());
            }
        }
    }

    //finding Country Name from Coordinates Using geonames api
    public static void getCountryCode(final Context context, final SQLiteDatabase db, final int selector) {
        Log.v(TAG,"FetchCountryData getCountryCode");
        final Location location = getLocation(context);
        RequestParams params = new RequestParams();
        params.put("lat", location.getLatitude());
        params.put("lng",location.getLongitude());
        params.put("username", "wildfire");
        int countryCode;
        GeoNameRESTClient.get("", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JSONObject response = new JSONObject();
                try {
                    response = new JSONObject(new String(responseBody));
                    country = response.getJSONArray("geonames").getJSONObject(0).getString("countryName");
                    countryShortId = response.getJSONArray("geonames").getJSONObject(0).getString("countryCode");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public void onFinish() {
                super.onFinish();

                Log.i(TAG, "JSON :" + country);
                Log.i(TAG, "COUNTRY NAME : " + country);
//                Cursor cursor = db.query(MyDBHandler.TABLE_COUNTRY, new String[]{MyDBHandler.COUNTRY_COLUMN_CODE},
//                        MyDBHandler.COUNTRY_COLUMN_NAME + " = ? ", new String[]{country}, null, null, null);
//                cursor.moveToFirst();
//                Log.i(TAG, "Cursor :" + cursor);
//                int countryCode = 0;
//                if (!cursor.isAfterLast())
//                    countryCode = cursor.getInt(0);//cursor.getColumnIndex(MyDBHandler.COUNTRY_COLUMN_CODE)
                int countryCode = getCountryCodeFromCountryName(db,country);
                countryCode = 168;

                Log.i(TAG, "COUNTRY CODE :" + countryCode);
                // Toast.makeText(context, country + " : " + countryCode, Toast.LENGTH_LONG).show();

                Country c = getCountryByCountryCode(db, Integer.toString(countryCode));
                Log.i(TAG, "Country code :" + c.getId());
//                Log.i(TAG,"Distress State :"+distressState.isInDistressState());
                switch (selector) {
                    case 0:
                        VictimActivity.setLocationText(c, location, countryShortId);
                        break;
                    case 2:
                        ProvideAssistanceActivity.setLocationText(c, location, countryShortId);
                        break;
                }
            }
        });
    }

    public static int getCountryCodeFromCountryName(SQLiteDatabase db, String countryName){
        Cursor cursor = db.query(MyDBHandler.TABLE_COUNTRY, new String[]{MyDBHandler.COUNTRY_COLUMN_CODE},
                MyDBHandler.COUNTRY_COLUMN_NAME + " = ? ", new String[]{countryName}, null, null, null);
        cursor.moveToFirst();
        Log.i(TAG, "Cursor :" + cursor);
        int countryCode = 0;
        if (!cursor.isAfterLast())
            countryCode = cursor.getInt(0);//cursor.getColumnIndex(MyDBHandler.COUNTRY_COLUMN_CODE)
        return countryCode;
    }

    public static Country getCountryByCountryCode(SQLiteDatabase db,String code){
        Cursor cursor = db.query(MyDBHandler.TABLE_COUNTRY,
                new String[]{MyDBHandler.COUNTRY_COLUMN_NAME,MyDBHandler.COUNTRY_EMERGENCY_FIRE_NUMBER,
                        MyDBHandler.COUNTRY_EMERGENCY_AMBULANCE_NUMBER, MyDBHandler.COUNTRY_EMERGENCY_POLICE_NUMBER},
                MyDBHandler.COUNTRY_COLUMN_CODE+ " = ? ",new String[] {code}, null, null, null);
        cursor.moveToFirst();
        Country c = null;
        if(!cursor.isAfterLast())
            c = new Country(Integer.parseInt(code),cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
        return c;
    }

    private static Location getLocation(Context context) {
        Log.v(TAG,"FetchCountryData getLocation");
        GPSLocator gps = new GPSLocator(context);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            return gps.getLocation();
        }
        return null;
    }

}
