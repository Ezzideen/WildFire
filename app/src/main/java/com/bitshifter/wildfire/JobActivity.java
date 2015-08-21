package com.bitshifter.wildfire;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class JobActivity extends Activity {

    private Context context;


    private static final String PREFERENCE = "preference";
    private static EditText etJobCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        context = this;
        etJobCountry = (EditText) findViewById(R.id.etJobCountry);
    }

    class Job {
        String title;
        String link;

        public Job(String title, String link) {
            this.title = title;
            this.link = link;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }

    static ArrayList<Job> jobs;
    static ProgressDialog progressDialog;
    static String response;

    public void submitForm(View view) {
        MyDBHandler db = new MyDBHandler(this, null, null, 1);
        int countryCode = FetchCountryData.getCountryCodeFromCountryName(db.getWritableDatabase(),
                etJobCountry.getText().toString().trim());
        String BASE_URL = "http://reliefweb.int/jobs?country=" + countryCode;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(BASE_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                response = new String(responseBody);
            }

            @Override
            public void onStart() {
                super.onStart();
                progressDialog = ProgressDialog.show(context, "Processing...",
                        "Finding relevant information. Please wait.");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                parseResponse(response);
                progressDialog.dismiss();
                Intent intent = new Intent(context, WebViewActivity.class);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void parseResponse(String response) {
        jobs = new ArrayList<>();
//        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
        String BASE_URL = "http://reliefweb.int";
        Document document = Jsoup.parseBodyFragment(response);
        Elements items = document.getElementsByClass("item");
        for (Element item : items) {
            try {
                Elements links = item.getElementsByTag("a");
                String text = links.get(2).text();
                String href = links.get(2).attr("href");
                Job job = new Job(text, BASE_URL + href);
                jobs.add(job);
            } catch (Exception e) {
                //      Log.e("PARSE", e.toString());
            }
        }
        makeHtml(jobs);
    }

    private void makeHtml(ArrayList<Job> jobs) {
        String html = "<h2 style=\"text-align: center;\"><u>Jobs Near You</u></h2>\n";
        for (Job job : jobs) {
            html += "<h3>" + job.getTitle() + "</h3>\n";
            html += "<a href=\"" + job.getLink() + "\"> Click HERE </a>";
        }
        Log.i("HTML", html);
        SharedPreferences.Editor editor = getSharedPreferences(PREFERENCE, MODE_PRIVATE).edit();
        editor.putString("html", html);
        editor.commit();

    }
}
