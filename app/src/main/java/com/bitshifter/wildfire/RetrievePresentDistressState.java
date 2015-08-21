package com.bitshifter.wildfire;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by root on 21/8/15.
 */
public class RetrievePresentDistressState {
    private boolean distressState=false;
    private String articles="";

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

    public void currentScenario(int countryCode){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://api.rwlabs.org/v1/countries/" + Integer.toString(countryCode), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.v("ab",response.toString());
                    JSONObject tmp = response.getJSONArray("data").getJSONObject(0).getJSONObject("fields");
                    Log.v("ab",tmp.toString());
                    if(tmp.get("status").toString().equals("normal")) {
                        setDistressState(false);
                        Log.v("ab", "no distress bro");
                    }
                    else {
                        setDistressState(true);
                        String html=tmp.get("description-html").toString();
                        Log.v("ab",html);
                        if(html!=null)
                            setArticles(html);
                    }
                }
                catch (Exception e){
                    Log.v("ab",e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.v("ab",errorResponse.toString());
            }
        });
    }
}
