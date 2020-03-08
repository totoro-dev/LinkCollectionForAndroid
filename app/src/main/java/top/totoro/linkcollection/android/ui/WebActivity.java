package top.totoro.linkcollection.android.ui;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import top.totoro.linkcollection.android.R;
import top.totoro.linkcollection.android.base.BaseActivity;
import top.totoro.linkcollection.android.dialog.LoadingDialog;

/**
 * Create by HLM on 2020-02-18
 */
public class WebActivity extends BaseActivity {

    public static String link = "https://baidu.com";

    private WebView wvWeb;

    @Override
    protected void onStart() {
        super.onStart();
        wvWeb = findViewById(R.id.web);
        wvWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 页面加载结束
                LoadingDialog.getInstance().dismiss();
            }
        });
        wvWeb.setWebChromeClient(new WebChromeClient());
        WebSettings settings = wvWeb.getSettings();
        if (settings != null) {
            settings.setJavaScriptEnabled(true);
            // 5.1以上默认禁止了https和http混用，以下方式是开启http的访问
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        wvWeb.loadUrl(link);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadingDialog.getInstance().show(getSupportFragmentManager());
        setContentView(R.layout.activity_web);
    }
}
