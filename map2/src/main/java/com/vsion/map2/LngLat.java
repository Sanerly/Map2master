package com.vsion.map2;

/**
 * Created by XiaoShu on 2017/11/10 0010.
 */

public class LngLat {
    private double longitude;     // 经度
    private double latitude;      // 纬度
    private boolean isSendSuccess;

    public LngLat(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }


    public boolean isSendSuccess() {
        return isSendSuccess;
    }

    public void setSendSuccess(boolean sendSuccess) {
        isSendSuccess = sendSuccess;
    }

    @Override
    public String toString() {
        return "LngLat{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
