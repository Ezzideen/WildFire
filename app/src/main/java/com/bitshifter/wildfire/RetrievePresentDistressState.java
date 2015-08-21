package com.bitshifter.wildfire;

import android.app.ProgressDialog;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 21/8/15.
 */
public class RetrievePresentDistressState {

    private static final String TAG = "WILDFIRE";
    private boolean distressState = false;
    private String articles = "";
    ProgressDialog progressDialog;
    JSONObject response = new JSONObject();

    public boolean isInDistressState() {
        return distressState;
    }

    public void setDistressState(boolean distressState) {
        this.distressState = distressState;
    }

    public String getArticles() {
        return articles;
    }

    public void setArticles(String articles) {
        this.articles = articles;
    }

    public void currentScenario(int countryCode) {
        Log.v(TAG,"RetrieveDistressState currentScenario");
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://api.rwlabs.org/v1/countries/" + Integer.toString(countryCode), new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        progressDialog = ProgressDialog.show(VictimActivity.context,"Something ..","Something .....");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            response = new JSONObject(new String(responseBody));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }


                    @Override
                    public void onFinish() {
                        super.onFinish();
                        try {
                            progressDialog.dismiss();

                            Log.v(TAG, response.toString());
                            JSONObject tmp = response.getJSONArray("data").getJSONObject(0).getJSONObject("fields");
                            Log.v(TAG, tmp.toString());
                            if (tmp.get("status").toString().equals("normal")) {
                                setDistressState(false);
                                Log.v(TAG, "no distress bro");
                            } else {
                                setDistressState(true);
                                String html = tmp.get("description-html").toString();
                                Log.v(TAG, html);
                                if (html != null) {
                                    setArticles(html);
                                    VictimActivity.callWebView(html);
                                }


                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
}
