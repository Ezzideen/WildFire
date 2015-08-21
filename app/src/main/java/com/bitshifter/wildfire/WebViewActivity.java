package com.bitshifter.wildfire;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends Activity {

    private static Context context;
    private Intent intent;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();

        setContentView(R.layout.activity_web_view);
        context = this;
        webView = (WebView) findViewById(R.id.webView);
        webView.loadData(intent.getStringExtra(VictimActivity.EXTRA_HTML), "text/html", "UTF-8");

    }

}
