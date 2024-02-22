package com.example.healthkeeper.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.healthkeeper.databinding.ActivityCctvBinding;

public class CCTVActivity extends AppCompatActivity {
    ActivityCctvBinding binding;

    private WebView webPageWebView;
    private WebSettings webPageWebSettings;

    private Button fullScreenButton;
    private Button restoreButton;
    private Button backHomeButton;

    private boolean isFullscreen = false;
    private int originalOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCctvBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        webPageWebView = binding.webPageWebView;
        fullScreenButton = binding.fullScreenButton;
        restoreButton = binding.restoreButton;
        backHomeButton = binding.backHomeButton;

        // 웹페이지를 위한 WebView 설정
        webPageWebView.loadUrl("https://www.bing.com/videos/riverview/relatedvideo?&q=%ec%98%81%ec%83%81&&mid=B2606CE5715B5C8884B2B2606CE5715B5C8884B2&&FORM=GVRPTV");
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

        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//아직 처리못함... 다시봐야함
                startActivity(new Intent(CCTVActivity.this, CCTVActivity.class));
            }
        });

        fullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFullscreen();
            }
        });

        backHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CCTVActivity.this, MainActivity.class));
            }
        });
    }

    private void restorescreen() {
        // 전체화면에서 원래 상태로 복구
        setRequestedOrientation(originalOrientation);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        isFullscreen = false;

        // 전체화면 버튼 숨기고, 이전 화면 버튼 보이기
        fullScreenButton.setVisibility(View.VISIBLE);
        restoreButton.setVisibility(View.GONE);
    }

    private void toggleFullscreen() {
        if (!isFullscreen) {      // 전체화면으로 변경
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            isFullscreen = true;

            // 전체화면 버튼 숨기고, 이전 화면 버튼 보이기
            fullScreenButton.setVisibility(View.GONE);
            restoreButton.setVisibility(View.VISIBLE);
        } else {                 // 원상태로 복구
            restorescreen();
        }
    }

    @Override
    public void onBackPressed() {
        if (webPageWebView.canGoBack()) {
            webPageWebView.goBack();
        } else {
            finish();
        }
    }
}
