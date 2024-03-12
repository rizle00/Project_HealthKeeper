package com.example.healthkeeper.bluetooth;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import org.jetbrains.annotations.NotNull;

public class BluetoothViewModel extends ViewModel {

    private MutableLiveData<Integer> heartLiveData = new MutableLiveData<>(0);
    private MutableLiveData<String> accidentLiveData = new MutableLiveData<>("0");
    private MutableLiveData<Double> tempLiveData = new MutableLiveData<>(0.0);
    private MutableLiveData<String> btLiveData = new MutableLiveData<>("off");


    public LiveData<Integer> getHeartLiveData() {
        return heartLiveData;
    }

    public LiveData<String> getAccidentLiveData() {
        return accidentLiveData;
    }

    public LiveData<Double> getTempLiveData() {
        return tempLiveData;
    }

    public LiveData<String> getBtLiveData() {
        return btLiveData;
    }

    public void setHeartData(int data) {
        heartLiveData.postValue(data);
    }

    public void setAccidentData(String data) {
        accidentLiveData.postValue(data);
    }

    public void setTempData(double data) {
        tempLiveData.postValue(data);
    }

    public void setBtData(String data) {
        btLiveData.postValue(data);
    }
}
