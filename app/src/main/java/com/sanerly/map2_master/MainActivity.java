package com.sanerly.map2_master;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vison.base_map.BaseMap;
import com.vison.base_map.LngLat;
import com.vison.base_map.LocationUtils;
import com.vison.base_map.MapUiSettings;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private CoustomMapView mapView;
    private Button btnPoint;
    private Button btnDelete;
    private Button btnHide;
    private TextView tvLocationInfo;
    private boolean isShowHomeLine = false;


    private LocationUtils mLocationUtils;
    private int[] array = {R.mipmap.ic_map_plane, R.mipmap.ic_map_plane, R.mipmap.ic_map_plane,
            R.mipmap.ic_map_plane, R.mipmap.ic_map_plane, R.mipmap.ic_map_plane,
            R.mipmap.ic_map_plane, R.mipmap.ic_map_plane, R.mipmap.ic_map_plane,
            R.mipmap.ic_map_plane, R.mipmap.ic_map_plane, R.mipmap.ic_map_plane,
            R.mipmap.ic_map_plane, R.mipmap.ic_map_plane, R.mipmap.ic_map_plane};
    private List<LngLat> mLngLats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.cous_tom_map_view);
        btnPoint = findViewById(R.id.btn_point);
        btnDelete = findViewById(R.id.btn_delete);
        btnHide = findViewById(R.id.btn_hide);
        tvLocationInfo = findViewById(R.id.tv_location_info);
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
                .setPointRes(array, array.length)
                .setHasArea(true)
                .setCompass(true)
                .setOutAreaText(R.string.invalid)
                .setMaxDistance(50)
                .setOnlyLook(false)
                .setShowInfoWindow(true)
                .setZoomLevel(19)
                .setMinZoomLevel(3)
                .setFillColor(Color.parseColor("#504086FF"))
                .setStrokeColor(Color.parseColor("#093BB9"))
                .setStrokeWidth(6)
                .setGoogleLocationConvert(true)
                .setOnMapClickListener(new BaseMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LngLat lngLat) {

//                        mapView.getBaseMap().deleteAllMarker();
                    }
                })
                .setCompass(false)
                .setUiSettings(uiSettings);
        mapView.getBaseMap().setMapType(0);

//        mapView.getBaseMap().setDroneStartPoint(116.75094182128906, 23.494296583496094);
//        mapView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mapView.getBaseMap().setDroneLocation(113.84213980235995, 22.610208195213353, 0);
//            }
//        }, 2000);

        btnPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mapView.getBaseMap().setHomePoint(113.84213980235995, 22.610208195213353, R.mipmap.ic_map_home);
//                mLngLats = new ArrayList<>();
//                mLngLats.addAll(mapView.getBaseMap().getLngLats());
//                mapView.getBaseMap().deleteAllMarker();
//                for (int i = 0; i < mLngLats.size(); i++) {
//                    Log.d("MainActivity", "坐标点: " + mLngLats.get(i).toString());
//                }
//                mapView.getBaseMap().drawMoveTrack(mLngLats, Color.parseColor("#FFFF0000"));

//                updateLocation(location);

//                mapView.getBaseMap().deleteHomePolyline();
                mapView.getBaseMap().setHomePoint(113.84213980235995, 22.610308195213353, R.mipmap.ic_map_home);
//                mapView.getBaseMap().setDroneLocation(113.841444,22.59595, 0);
//                mapView.getBaseMap().setShowPhoneLine(true);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mapView.getBaseMap().deleteMoveTrack();
//                mHandler.sendEmptyMessageDelayed(2, 2000);
//                mapView.getBaseMap().addMarker(R.mipmap.ic_marker, 113.84213980235995, 22.610208195213353);
                //设置范围
//                distance+=20;
//                mapView.getBaseMap().changeMaxDistance(distance);
                //设置航点，不判断范围
//                mapView.getBaseMap().setPointMarker(new LngLat(113.84213980235995, 22.610208195213353));
//                distance += 0.5;
//                //移动到当前位置
//                mapView.getBaseMap().moveCurrentLocation(113.84213980235995, 22.610208195213353,45);

                mapView.getBaseMap().setDroneLocation(113.85213980235995, 22.610208195213353, 0);

//                mapView.getBaseMap().setResetMaxDistance(800);
            }
        });

        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowHomeLine=!isShowHomeLine;

//                mapView.getBaseMap().setShowHomeLine(isShowHomeLine);
                mapView.getBaseMap().setShowPhoneLine(isShowHomeLine);
            }
        });

    }

    int count = 0;
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 2) {
                count += 10;
                if (count >= 360) {
                    count = 0;
                }

                mapView.getBaseMap().setDroneLocation(113.84213980235995, 22.610208195213353, mapView.getBaseMap().bearing() + count);
                mHandler.sendEmptyMessageDelayed(2, 200);
            } else if (msg.what == 3) {
                Log.d("MainActivity", "定位: " + mapView.getBaseMap().moveMyLocation());
                if (!mapView.getBaseMap().moveMyLocation()) {
                    mHandler.sendEmptyMessageDelayed(3, 500);
                }
            }
            return false;
        }
    });


    @Override
    public void onLocationChanged(Location location) {
        mapView.getBaseMap().setMyLocation(location);
        Log.d("MainActivity", "定位: " + location);
//        mapView.getBaseMap().setDroneLocation(113.84213980235995, 22.610208195213353, 0);
        Log.d("MainActivity", "定位: " + mapView.getBaseMap().getMapReady() + "");

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
