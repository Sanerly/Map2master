package com.vsion.map2;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Sanerly
 * @CreateDate: 2019/7/15 17:33
 * @Description: 类描述
 */
public class GoogleMapLayout extends BaseMap implements OnMapReadyCallback {

    private MapView gMapView;
    private GoogleMap gMap;

    private Marker aMyMarker;      // 手机定位点
    private Marker aDroneMarker;      // 飞机
    private Polyline aPolyline;             // 指点飞行路径
    private List<Marker> aMarkerList = new ArrayList<>(); // 航点飞行标记
    private Circle mCircle;

    public GoogleMapLayout(Context context, Location location) {
        super(context, location);
    }


    @Override
    public void init(FrameLayout layout) {
        gMapView = new MapView(getContext());
        layout.addView(gMapView);
        gMapView.onCreate(null);
        gMapView.getMapAsync(this);
    }

    @Override
    public void moveMyLocation() {
        if (aMyMarker != null) {
            CameraPosition cameraPosition = gMap.getCameraPosition();
            //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(aMyMarker.getPosition(),
                    cameraPosition.zoom > 19 ? cameraPosition.zoom : 19, cameraPosition.tilt, cameraPosition.bearing));
            gMap.moveCamera(mCameraUpdate);
        }
    }

    @Override
    public void setMyLocation(Location location) {
        if (!isMapReady) {
            return;
        }
        mLocation = location;
        // 坐标转换
        double[] gcj02 = CoordinateTransformUtil.wgs84togcj02(location.getLongitude(), location.getLatitude());
        LatLng latLng = new LatLng(gcj02[1], gcj02[0]);
        if (aMyMarker == null && mMyIcon > 0) {
            aMyMarker = gMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(mMyIcon)));
        } else {
            aMyMarker.setPosition(latLng);
        }
        if (isHasArea) {
            if (mCircle == null) {
                mCircle = gMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(mMaxDistance)
                        .fillColor(Color.argb(50, 2, 146, 255))
                        .strokeColor(Color.argb(50, 1, 1, 1))
                        .strokeWidth(3));
            } else {
                mCircle.setCenter(latLng);
            }
        }
    }

    @Override
    public void moveDroneLocation() {
        if (aDroneMarker != null) {
            CameraPosition cameraPosition = gMap.getCameraPosition();
            //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(aDroneMarker.getPosition(),
                    cameraPosition.zoom > 19 ? cameraPosition.zoom : 19, cameraPosition.tilt, cameraPosition.bearing));
            gMap.moveCamera(mCameraUpdate);
        }
    }

    @Override
    public void setDroneLocation(double longitude, double latitude, float angle) {
        if (!isMapReady) {
            return;
        }
        // 坐标转换
        double[] gcj02 = CoordinateTransformUtil.wgs84togcj02(longitude, latitude);
        LatLng latLng = new LatLng(gcj02[1], gcj02[0]);

        if (null == aDroneMarker) {
            // 设置 飞机 图标
            aDroneMarker = gMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(mPlaneIcon)));
        } else {
            aDroneMarker.setPosition(latLng);
        }
        //设置旋转角度
        aDroneMarker.setAnchor(0.5f, 0.5f);
        aDroneMarker.setRotation(gMap.getCameraPosition().bearing + angle);
    }

    @Override
    public void setMapType(int type) {
        switch (type) {
            case 0:
                if (gMapView != null) {
                    gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                break;
            case 1:
                if (gMap != null) {
                    gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
                break;
            case 2:
                if (gMap != null) {
                    gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                }
                break;
        }
    }

    @Override
    public void addPointMarker(LngLat lngLat) {

        if (isOnlyLook) {
            return;
        }

        if (isHasArea) {
            if (null == mLocation || mMaxDistance < CoordinateTransformUtil.getDistance(mLocation.getLongitude(), mLocation.getLatitude(), lngLat.getLongitude(), lngLat.getLatitude())) {
                Toast.makeText(getContext(), getContext().getString(mInvalidPointRes), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (mLngLats.size() < mMaxPoint) {
            // 转为gcj02
            double[] gcj02 = CoordinateTransformUtil.wgs84togcj02(lngLat.getLongitude(), lngLat.getLatitude());
            LatLng latLng = new LatLng(gcj02[1], gcj02[0]);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            if (mPointRes == 0) {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(mPointResArray[mLngLats.size()]));
            } else {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(mPointRes));
            }
            Marker marker = gMap.addMarker(markerOptions);
            aMarkerList.add(marker);

            PolylineOptions options = new PolylineOptions().color(Color.parseColor("#FF959595")).width(8);
            for (Marker marker1 : aMarkerList) {
                options.add(marker1.getPosition());
            }
            if (aPolyline == null) {
                aPolyline = gMap.addPolyline(options);
            } else {
                aPolyline.setPoints(options.getPoints());
            }
            mLngLats.add(lngLat);
        }
    }

    @Override
    public void deleteAllMarker() {
        mLngLats.clear();

        if (aMarkerList != null) {
            for (Marker marker : aMarkerList) {
                marker.remove();
            }
            aMarkerList.clear();
        }

        if (aPolyline != null) {
            aPolyline.remove();
            aPolyline = null;
        }
    }

    @Override
    public void deleteSingleMarker() {
        int index = mLngLats.size() - 1;
        if (index < 0) {
            return;
        }
        mLngLats.remove(index);

        if (aMarkerList != null) {
            aMarkerList.get(index).remove();
            aMarkerList.remove(index);
        }

        PolylineOptions options = new PolylineOptions().color(Color.parseColor("#FF959595")).width(8);
        for (Marker marker1 : aMarkerList) {
            options.add(marker1.getPosition());
        }

        if (aPolyline != null) {
            aPolyline.remove();
            aPolyline = null;
        }
        aPolyline = gMap.addPolyline(options);
    }


    @Override
    public void onRotate(float orientation) {
        CameraPosition cameraPosition = gMap.getCameraPosition();
        //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(cameraPosition.target, cameraPosition.zoom, cameraPosition.tilt, orientation));
        gMap.moveCamera(mCameraUpdate);
    }


    @Override
    public void onResume() {
        if (gMapView == null) {
            return;
        }
        gMapView.onResume();
    }

    @Override
    public void onPause() {
        if (gMapView == null) {
            return;
        }
        gMapView.onPause();
    }

    @Override
    public void onDestroy() {
        if (gMapView == null) {
            return;
        }
        gMapView.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        UiSettings uiSettings = googleMap.getUiSettings();
        // 指南针
        uiSettings.setCompassEnabled(true);

        uiSettings.setAllGesturesEnabled(true);

        uiSettings.setMyLocationButtonEnabled(false);

        uiSettings.setMapToolbarEnabled(false);
        //设置地图是否可以倾斜
        uiSettings.setTiltGesturesEnabled(false);
        //设置地图是否可以旋转
        uiSettings.setRotateGesturesEnabled(false);
        //放大缩小按钮
        uiSettings.setZoomControlsEnabled(false);
        // 设置缩放级别
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(19);
        gMap.animateCamera(zoom);
        // 设置最小缩放级别为2km
        gMap.setMinZoomPreference(12);

        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // 转为wgs84
                double[] wgs84 = CoordinateTransformUtil.gcj02towgs84(latLng.longitude, latLng.latitude);
                if (onMapClickListener != null) {
                    onMapClickListener.onMapClick(new LngLat(wgs84[0], wgs84[1]));
                }
                addPointMarker(new LngLat(wgs84[0], wgs84[1]));
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isMapReady = true;
            }
        }, 1500);

        if (mLocation != null) {
            CameraPosition cameraPosition = gMap.getCameraPosition();
            // 坐标转换
            double[] gcj02 = CoordinateTransformUtil.wgs84togcj02(mLocation.getLongitude(), mLocation.getLatitude());
            LatLng latLng = new LatLng(gcj02[1], gcj02[0]);
            //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 19, cameraPosition.tilt, cameraPosition.bearing));
            gMap.moveCamera(mCameraUpdate);
//            setMyLocation(mLocation);
        }
    }
}
