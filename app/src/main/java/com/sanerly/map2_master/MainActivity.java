package com.sanerly.map2_master;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vsion.map2.BaseMap;
import com.vsion.map2.LngLat;
import com.vsion.map2.LocationUtils;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private CoustomMapView mapView;
    private LocationUtils mLocationUtils;
    private int[] array = {R.mipmap.ic_map_plane, R.mipmap.ic_map_plane, R.mipmap.ic_map_plane, R.mipmap.ic_map_plane, R.mipmap.ic_map_plane, R.mipmap.ic_map_plane};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.cous_tom_map_view);
        setPermissions();
        mLocationUtils = new LocationUtils(this);
        mLocationUtils.setLocationListener(this);

        mapView.init(mLocationUtils.getLastKnownLocation(), false);

        mapView.getBaseMap().setPlaneIcon(R.mipmap.ic_map_location_drone)
                .setMyIcon(R.mipmap.ic_map_my_loaction)
                .setPointRes(array, 7)
                .setMaxPoint(5)
                .setHasArea(true, R.string.invalid)
                .setMaxDistance(30)
                .setOnMapClickListener(new BaseMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LngLat lngLat) {
                        mapView.getBaseMap().deleteAllMarker();
                    }
                })
                .build();
    }

    @Override
    public void onLocationChanged(Location location) {
        mapView.getBaseMap().setMyLocation(location);
        Log.d("dddd", location + "");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mapView) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mapView) {
            mapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mapView) {
            mapView.onDestroy();
        }
    }

    @SuppressLint("CheckResult")
    private void setPermissions() {
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (!aBoolean) {
                    Toast.makeText(MainActivity.this, "获取权限成功", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
