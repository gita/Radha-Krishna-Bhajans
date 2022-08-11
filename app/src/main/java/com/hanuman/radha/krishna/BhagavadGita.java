package com.hanuman.radha.krishna;

import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BhagavadGita extends AppCompatActivity {

    WebView wv;

    @Override
    public void onBackPressed() {
        if(wv.canGoBack()) {
            wv.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bhagavad_gita);

        wv = (WebView) findViewById(R.id.wv);


        wv.getSettings().setJavaScriptEnabled(true);
        wv.setFocusable(true);
        wv.setFocusableInTouchMode(true);
        wv.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setDatabaseEnabled(true);
        wv.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//        wv.getSettings().setAppCacheEnabled(true);
        wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        wv.loadUrl("https://bhagavadgita.io");
        wv.setWebViewClient(new RadhaKrishna());
    }
}