package com.jojo.lnpreader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jojo.lnpreader.utils.PrefHelper;

public class ContentViewerActivity extends AppCompatActivity {

    WebView wvContent;
    PrefHelper pf;

    ProgressBar prg;
    TextView tvLoad;

    private static final String DEFAULT_URL = "https://www.lightnovelpub.com" +
            "/novel/lord-of-the-mysteries-275/chapter-1-16091324";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pf = new PrefHelper(this);
        prg = findViewById(R.id.prgLoading);
        tvLoad = findViewById(R.id.tvLoading);

        wvContent = findViewById(R.id.wvContentTest);
        wvContent.getSettings().setJavaScriptEnabled(true);

        wvContent.setWebViewClient(new MyWebViewClient());

        String savedURL = pf.getSavedURL();

        if(savedURL == null){
            // If there's no saved URL, start with chapter 1.
            wvContent.loadUrl(DEFAULT_URL);
            setTitle("LOTM Chapter " + getChapNumber(DEFAULT_URL));
        }
        else{
            wvContent.loadUrl(savedURL);
            setTitle("LOTM Chapter " + getChapNumber(savedURL));
        }
    }

    private String getChapNumber(String url){
        String[] chapData = url.split("/");

        //URL format validation check to make sure
        //we're fetching with the proper format
        if(chapData.length == 6){
            String chapInfo = chapData[chapData.length-1];
            String[] title = chapInfo.split("-");

            if(title[0].equals("chapter")){
                return title[1];
            }
            else{
                return "";
            }
        }
        else{
            return "";
        }
    }

    @Override
    protected void onStop() {
//        Log.d("URL",wvContent.getUrl());
        pf.saveCurrentURL(wvContent.getUrl());
        super.onStop();
    }

    @Override
    protected void onDestroy() {
//        Log.d("URL",wvContent.getUrl());
        pf.saveCurrentURL(wvContent.getUrl());
        super.onDestroy();
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            // This method is called when a new URL is about to be loaded.
            // Return true to indicate that the WebView should handle the URL,
            // or false to allow the OS to handle it (default behavior).

            // Example: Intercept URLs and load them in the WebView
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
            // This method is called when the webpage starts loading.
            // You can perform actions such as showing a progress bar.
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // This method is called when the webpage finishes loading.
            // You can perform actions such as hiding a progress bar.

            //Force dark mode on the text content.
            view.evaluateJavascript("javascript:(function() { document.body.style." +
                    "background = 'black'; document.body.style.color = 'white'; })()", null);
            prg.setVisibility(ProgressBar.GONE);
            tvLoad.setVisibility(TextView.GONE);

            String urlInfo = view.getUrl();

            if(urlInfo != null){
                setTitle("LOTM Chapter " + getChapNumber(urlInfo));
            }
            else{
                setTitle("LNP");
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, android.webkit.WebResourceError error) {
            // This method is called if there is an error during loading.
            // You can handle the error and take appropriate actions.
        }
    }

}