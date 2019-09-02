package com.vsion.map2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

/**
 * @Author: Sanerly
 * @CreateDate: 2019/8/13 9:09
 * @Description: 类描述
 */
public class LocationUtils {
    private static final double EARTH_RADIUS = 6378137.0D;
    private LocationManager locationManager;

    public LocationUtils(Context context) {
        this.locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    }

    @SuppressLint({"MissingPermission"})
    public Location getLastKnownLocation() {
        return this.locationManager.getLastKnownLocation("gps");
    }

    @SuppressLint({"MissingPermission"})
    public void setLocationListener(LocationListener locationListener) {
        this.locationManager.requestLocationUpdates("gps", 1000L, 0.0F, locationListener);
    }

    public void removeLocationListener(LocationListener locationListener) {
        this.locationManager.removeUpdates(locationListener);
    }

    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2.0D * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2.0D), 2.0D) + Math.cos(Lat1) * Math.cos(Lat2) * Math.pow(Math.sin(b / 2.0D), 2.0D)));
        s *= 6378137.0D;
        s = (double)(Math.round(s * 10000.0D) / 10000L);
        return s;
    }

    private static double rad(double d) {
        return d * 3.141592653589793D / 180.0D;
    }
}
