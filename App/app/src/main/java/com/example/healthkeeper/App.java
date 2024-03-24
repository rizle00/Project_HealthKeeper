package com.example.healthkeeper;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.ViewModelProvider;
import com.example.healthkeeper.bluetooth.BluetoothViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App extends Application {

    private BluetoothViewModel sharedViewModel;
    @Override
    public void onCreate() {
        super.onCreate();
        sharedViewModel = new ViewModelProvider.AndroidViewModelFactory(this).create(BluetoothViewModel.class);
    }

    public BluetoothViewModel getSharedViewModel() {
        return sharedViewModel;
    }
    // 가능한 코어수 가져오기
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    // 앱 전체적인 스레드 생성
    public ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_CORES);
   public Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());

}
