package com.bitshifter.wildfire;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
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

    private static JSONObject json;
    private static SQLiteDatabase database;
    private static String country;
    //Fetching Json List of countries from ReliefWeb
    public static void insertCountriesIntoDatabase(SQLiteDatabase db){
        database = db;
        RequestParams params = new RequestParams();
        params.put("offset","0");
        params.put("limit", "250");

        ReliefWebRestClient.get("countries", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                ArrayList<Country> countries = parseJsonCountryList(response);
                insertCountries(database, countries);
            }
        });
    }

    //Parsing Json data and making an arraylist of Country
    private static ArrayList<Country> parseJsonCountryList(JSONObject json){
        ArrayList<Country> countryList = new ArrayList<Country>();
        try {
            JSONArray jsonArray = json.getJSONArray("data");
            int count = json.getInt("count");
            for (int i=0; i < count ;i++){
                int id = jsonArray.getJSONObject(i).getInt("id");
                String name = jsonArray.getJSONObject(i).getJSONObject("fields").getString("name");
                Country country = new Country(id,name);
                countryList.add(country);
                Log.v("COUNTRY",name);
            }
        }catch (JSONException e){
            Log.e("ERROR",e.toString());
        }
        return countryList;
    }

    private static void insertCountries(SQLiteDatabase database, ArrayList<Country> countries){

        for (Country country : countries){
            ContentValues values = new ContentValues();
            values.put(MyDBHandler.COUNTRY_COLUMN_CODE,country.getId());
            values.put(MyDBHandler.COUNTRY_COLUMN_NAME,country.getName());
            database.insert(MyDBHandler.TABLE_COUNTRY,null,values);
        }
    }

    //finding Country Name from Coordinates Using geonames api
    public static void getCountryCode(final Context context, final SQLiteDatabase db) {
        Location location = getLocation(context);
        RequestParams params = new RequestParams();
        params.put("lat", location.getLatitude());
        params.put("lng",location.getLongitude());
        params.put("username", "wildfire");
        GeoNameRESTClient.get("",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    country = response.getJSONArray("geonames").getJSONObject(0).getString("countryName");
                    String query = "SELECT "+ MyDBHandler.COUNTRY_COLUMN_CODE +
                            " FROM "+ MyDBHandler.TABLE_COUNTRY +
                            " WHERE " + MyDBHandler.COUNTRY_COLUMN_NAME +" = '"+ country +"' ;";
                    Cursor cursor = db.rawQuery(query,null);
                    cursor.moveToFirst();
                    int countryCode = 0;
                    if(!cursor.isAfterLast())
                        countryCode = cursor.getInt(0);//cursor.getColumnIndex(MyDBHandler.COUNTRY_COLUMN_CODE)
                    Toast.makeText(context,country+" : "+countryCode,Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    Log.e("JSON Exception", e.toString());
                }
            }
        });
    }

    private static Location getLocation(Context context) {
        GPSLocator gps = new GPSLocator(context);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            return gps.getLocation();
        }
        return null;
    }

}
