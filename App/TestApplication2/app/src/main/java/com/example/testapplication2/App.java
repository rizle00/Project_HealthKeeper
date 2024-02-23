package com.example.testapplication2;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import androidx.core.os.HandlerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App extends Application {
    // 가능한 코어수 가져오기
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    // 앱 전체적인 스레드 생성
    ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_CORES);
    Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
}
