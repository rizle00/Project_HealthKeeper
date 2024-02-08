package com.example.bluetooth;

import android.util.Log;

public class XiaomiSensor {
    public static final String DEVICE_TYPE = "5b 05";       // LYWSD03MMC
    //디바이스 타입 체크

    public long updateTime = 0;
    public float temperature = 0f;
    public int humidity = 0;

    public XiaomiSensor() {
    }

    public XiaomiSensor(long updateTime, float temperature, int humidity) {
        this.updateTime = updateTime;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public static boolean isType(byte[] serviceData)
    {
        Log.d("type", "isType: "+byteArrayToHex(serviceData).toLowerCase().contains(DEVICE_TYPE));
        return byteArrayToHex(serviceData).toLowerCase().contains(DEVICE_TYPE);
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for(final byte b: a)
            sb.append(String.format("%02x ", b&0xff));
        return sb.toString();
    }

}
