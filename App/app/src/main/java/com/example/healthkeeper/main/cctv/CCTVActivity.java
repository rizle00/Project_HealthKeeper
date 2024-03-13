package com.example.healthkeeper.main.cctv;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.healthkeeper.databinding.ActivityCctvBinding;

public class CCTVActivity extends AppCompatActivity {
    public static final String CCTV_URL = "https://www.bing.com/videos/riverview/relatedvideo?&q=%ec%98%81%ec%83%81&&mid=B2606CE5715B5C8884B2B2606CE5715B5C8884B2&&FORM=GVRPTV";

    ActivityCctvBinding binding;

    private WebView webPageWebView;
    private WebSettings webPageWebSettings;

    private Button fullScreenButton;
    private Button backHomeButton;

    private int originalOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCctvBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        webPageWebView = binding.webPageWebView;
        fullScreenButton = binding.fullScreenButton;
        backHomeButton = binding.backHomeButton;

        // 웹페이지를 위한 WebView 설정
        webPageWebView.loadUrl(CCTV_URL);
        webPageWebView.setWebViewClient(new WebViewClient()); // 현재 앱을 나가서 새로운 브라우저를 열지 않도록 함.
        webPageWebSettings = webPageWebView.getSettings();
        webPageWebSettings.setJavaScriptEnabled(true); // JavaScript 활성화
        webPageWebSettings.setSaveFormData(false);
        webPageWebSettings.setSavePassword(false);
        webPageWebSettings.setUseWideViewPort(true);
        webPageWebSettings.setSupportMultipleWindows(true);
        webPageWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webPageWebSettings.setLoadWithOverviewMode(true);
        webPageWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        originalOrientation = getRequestedOrientation();

        fullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CCTVActivity.this, CCTVFullmainActivity.class));
                finish();
            }
        });

        backHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CCTVActivity.this, MainActivity.class));
                finish();
            }
        });


    }

    @Override
    public void onDestroy() {
        // 액티비티가 종료될 때 호출, WebView 객체를 제거하여 메모리 누수를 방지
        super.onDestroy();
        if (webPageWebView != null) {
            webPageWebView.destroy();
        }
    }

    @Override
    public void onBackPressed() {
        if (webPageWebView.canGoBack()) {
            webPageWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
