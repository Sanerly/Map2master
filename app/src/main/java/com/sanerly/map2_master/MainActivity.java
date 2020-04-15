package com.sanerly.map2_master;


import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vison.base_map.BaseMap;
import com.vison.base_map.LngLat;
import com.vison.base_map.LocationUtils;
import com.vison.base_map.MapUiSettings;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private CoustomMapView mapView;
    private Button btnPoint;
    private Button btnDelete;


    private LocationUtils mLocationUtils;
    private int[] array = {R.mipmap.ic_map_plane, R.mipmap.ic_map_plane,
            R.mipmap.ic_map_plane, R.mipmap.ic_map_plane, R.mipmap.ic_map_plane, R.mipmap.ic_map_plane};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.cous_tom_map_view);
        btnPoint = findViewById(R.id.btn_point);
        btnDelete = findViewById(R.id.btn_delete);
        setPermissions();
        mLocationUtils = new LocationUtils(this);
        mLocationUtils.setLocationListener(this);

        mapView.init(mLocationUtils.getLastKnownLocation());

        MapUiSettings uiSettings = new MapUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setZoomControlsEnabled(false);

        mapView.getBaseMap()
                .setPlaneIcon(R.mipmap.ic_map_location_drone)
                .setMyIcon(R.mipmap.ic_map_my_loaction)
                .setPointRes(array, 7)
                .setMaxPoint(5)
                .setHasArea(true)
                .setCompass(true)
                .setOutAreaText(R.string.invalid)
                .setMaxDistance(50)
                .setShowLine(true)
                .setOnMapClickListener(new BaseMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LngLat lngLat) {
//                        mapView.getBaseMap().deleteAllMarker();
                    }
                })
                .setCompass(false)
                .setUiSettings(uiSettings);
        mapView.getBaseMap().setMapType(0);

        mapView.getBaseMap().setDroneStartPoint(116.75094182128906, 23.494296583496094);
        mapView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mapView.getBaseMap().setDroneLocation(116.75099182128906, 23.494286583496094, 270);
            }
        },2000);

        btnPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.getBaseMap().setDroneLocation(116.75054182128906, 23.494296583496094, 170);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.getBaseMap().deleteFlyPolyline();
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        mapView.getBaseMap().setMyLocation(location);
//        mapView.getBaseMap().setDroneLocation(113.84213980235995, 22.610208195213353, 0);
        Log.d("dddd", mapView.getBaseMap().getMapReady() + "");
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
        permissions.request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE

        ).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (!aBoolean) {
                    Toast.makeText(MainActivity.this, "获取权限成功", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
