package com.example.healthkeeper.member;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityPopupSearchAddressBinding;

@SuppressLint("SetJavaScriptEnabled") //경고 없앨려고 만든 어노테이션
public class PopupSearchAddressActivity extends AppCompatActivity {
    ActivityPopupSearchAddressBinding binding;
    WebView webView;

    final String TAG = "주소";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityPopupSearchAddressBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(binding.getRoot());
/*        Intent intent = new Intent(this,GuardianJoinActivity.class);
        intent.putExtra("data","value");
        setResult(0,intent);*/



        init_webView();



    }

    //팝업 바깥을 터치해도 팝업 안꺼지게 하기
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) return false;

        return true;
    }


    public void init_webView(){
        webView = binding.wvAddress;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "Android");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });
        webView.loadUrl("http://192.168.0.49/middle/and/address");
    }
  /*      { webView = binding.wvAddress;

        webView.getSettings().setJavaScriptEnabled(true);



        webView.addJavascriptInterface(new AnroidBridge(), "HealthKeeper");

        //DOMStorage 허용
        webView.getSettings().setDomStorageEnabled(true);

        //ssl 인증이 없는 경우 해결을 위한 부분
        webView.setWebChromeClient(new WebChromeClient() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                request.grant(request.getResources());
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // SSL 에러가 발생해도 계속 진행
                handler.proceed();
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:sample2_execDaumPostcode();");
                Log.d(TAG, "onPageFinished: 까지왔는지 ");
            }
        });
        webView.loadUrl("http://192.168.0.49/app/address");
        Log.d(TAG, "init_webView: 까지 왔는지 ");}*/




    class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processDATA(String data) {
            Bundle extra = new Bundle();
            Intent intent = new Intent();
            extra.putString("data", data);
            intent.putExtras(extra);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}