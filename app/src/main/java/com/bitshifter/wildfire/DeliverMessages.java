package com.bitshifter.wildfire;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import io.oauth.OAuth;


public class DeliverMessages {
    public static void sendSms(String to, String body){
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Basic QUMxNWU5NTMzNGI5Njk3ZDRlMmViZjRlNDJiNzRjMTczMDo3NDliMTA0NzdlZmYxMWMxNDA1ZTIwMTBlOTQ2ZTMxZA==");
        RequestParams requestParams = new RequestParams();
        requestParams.put("From", "+37259120232");
        requestParams.put("To", to);
        requestParams.put("Body", body);
        client.post("https://api.twilio.com/2010-04-01/Accounts/AC15e95334b9697d4e2ebf4e42b74c1730/Messages",requestParams,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.v("ab",new String (responseBody));
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.v("ab",new String (responseBody));
            }
        });
    }

    public static void sendEmail(OAuth oauth){
//        oauth.initialize("kHWTSmXyRM-6T5mdso_MqImBzF4");
//        oauth.popup("google_mail", new OAuthCallback() {
//            @Override
//            public void onFinished(OAuthData data) {
//                Toast.makeText(getApplicationContext(), data.token, Toast.LENGTH_LONG).show();
//                Log.v("ab", data.token);
//                client.get("https://www.googleapis.com/drive/v2/files?access_token=" + data.token, null, new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int i, Header[] headers, JSONObject result) {
//                        Log.v("ab", result.toString());
//                        makeFileList(result);
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                        Log.v("ab", errorResponse.toString());
//                    }
//                });
//            }
//        });
//        AsyncHttpClient client = new AsyncHttpClient();
//        stringEntity = new StringEntity();
//        client.addHeader("Content-Type", "application/json");
//        client.addHeader("Authorization", "8dffa822a34fa35c6e5aee09e2da8ebc6d289150");
//
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("From", "+37259120232");
//        requestParams.put("To", "+919903207591");
//        requestParams.put("Body", "U suck man big time!");
//        client.post()
//        client.post(context, "https://api.twilio.com/2010-04-01/Accounts/AC15e95334b9697d4e2ebf4e42b74c1730/Messages",,new AsyncHttpResponseHandler(){
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                Log.v("ab",new String (responseBody));
//            }
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Log.v("ab",new String (responseBody));
//            }
//        });


    }
}
