package com.hanuman.radha.krishna;

import android.net.Uri;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class RadhaKrishna extends WebViewClient {
    public boolean shouldOverrideKeyEvent (WebView view, KeyEvent event) {

        return true;
    }

    public boolean shouldOverrideUrlLoading (WebView view, String url) {
        if (Uri.parse(url).getHost().startsWith("https://bhagavadgita.io")) {
            return true;
        }
        return false;
    }
}
