package com.example.healthkeeper.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.healthkeeper.databinding.ActivityCctvfullMainBinding;


public class CCTVFullmainActivity extends AppCompatActivity {
        ActivityCctvfullMainBinding binding;
        private Button restoreButton;
        private WebView webPageWebView;
        private WebSettings webPageWebSettings;
        private int originalOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityCctvfullMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        restoreButton = binding.restoreButton;
        webPageWebView=binding.webPageWebView;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//가로화면

        // 웹페이지를 위한 WebView 설정
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
        webPageWebView.loadUrl(CCTVActivity.CCTV_URL);
        originalOrientation = getRequestedOrientation();

        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//아직 처리못함... 다시봐야함
                startActivity(new Intent(CCTVFullmainActivity.this, CCTVActivity.class));
            }
        });


    }


}