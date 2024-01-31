package com.example.healthkeeper.bt;

import android.os.Parcel;
import android.os.Parcelable;

public class DeviceData implements Parcelable {
    private final String name;
    private final String uuid;
    private final String address;

    public DeviceData(String name, String uuid, String address) {
        this.name = name;
        this.uuid = uuid;
        this.address = address;
    }

    protected DeviceData(Parcel in) {
        name = in.readString();
        uuid = in.readString();
        address = in.readString();
    }

    public static final Creator<DeviceData> CREATOR = new Creator<DeviceData>() {
        @Override
        public DeviceData createFromParcel(Parcel in) {
            return new DeviceData(in);
        }

        @Override
        public DeviceData[] newArray(int size) {
            return new DeviceData[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(uuid);
        dest.writeString(address);
    }
}
