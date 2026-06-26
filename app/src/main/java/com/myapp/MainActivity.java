package com.myapp;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.webkit.*;

public class MainActivity extends Activity {
    android.webkit.ValueCallback<android.net.Uri[]> mFilePathCallback;
    public static final int FILE_CHOOSER_RESULT_CODE = 100;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        webView.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(webView);
        initWebView();
    }

    private void initWebView() {
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setDatabaseEnabled(true);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setAllowFileAccessFromFileURLs(true);
        s.setAllowUniversalAccessFromFileURLs(true);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);
        s.setSupportZoom(false);
        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest r) { return false; }
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) { return false; }
            @Override public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) { handler.proceed(); }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override public boolean onShowFileChooser(WebView w, ValueCallback<Uri[]> cb, FileChooserParams p) {
                if (mFilePathCallback != null) mFilePathCallback.onReceiveValue(null);
                mFilePathCallback = cb;
                try { startActivityForResult(p.createIntent(), FILE_CHOOSER_RESULT_CODE); }
                catch (Exception e) { mFilePathCallback = null; return false; }
                return true;
            }
        });
        webView.loadUrl("https://example.com");
    }

    @Override protected void onActivityResult(int rc, int result, Intent data) {
        if (rc == FILE_CHOOSER_RESULT_CODE && mFilePathCallback != null) {
            Uri[] r = null;
            if (result == RESULT_OK && data != null && data.getDataString() != null)
                r = new Uri[]{Uri.parse(data.getDataString())};
            mFilePathCallback.onReceiveValue(r);
            mFilePathCallback = null;
        }
    }

    @Override public void onBackPressed() {
        if (webView.canGoBack()) webView.goBack(); else super.onBackPressed();
    }
}