package com.example.healthkeeper.bluetooth;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class BluetoothViewModel extends ViewModel {

    private MutableLiveData<Integer> heartLiveData = new MutableLiveData<>(0);
//    private MutableLiveData<String> accidentLiveData = new MutableLiveData<>("0");
    private MutableLiveData<Double> tempLiveData = new MutableLiveData<>(0.0);
    private MutableLiveData<String> btLiveData = new MutableLiveData<>("off");

    private MutableLiveData<HashMap<String,Object>> data = new MutableLiveData<>();


    public LiveData<Integer> getHeartLiveData() {
        return heartLiveData;
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

//    public LiveData<String> getAccidentLiveData() {
//        return accidentLiveData;
//    }
//    public void setAccidentData(String data) {
//        accidentLiveData.postValue(data);
//    }

    public void setTempData(double data) {
        tempLiveData.postValue(data);
    }

    public void setBtData(String data) {
        btLiveData.postValue(data);
    }

    public MutableLiveData<HashMap<String, Object>> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> map) {
        data.postValue(map);// setvalue 는 메인 스레드에서만 가능
    }
}
