package com.vison.tmap;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.CameraPosition;
import com.tencent.mapsdk.raster.model.Circle;
import com.tencent.mapsdk.raster.model.CircleOptions;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.mapsdk.raster.model.Polyline;
import com.tencent.mapsdk.raster.model.PolylineOptions;
import com.tencent.tencentmap.mapsdk.map.CameraUpdate;
import com.tencent.tencentmap.mapsdk.map.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import com.tencent.tencentmap.mapsdk.map.UiSettings;
import com.vison.base_map.BaseMap;
import com.vison.base_map.CoordinateTransformUtil;
import com.vison.base_map.LngLat;
import com.vison.base_map.MapUiSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Sanerly
 * @CreateDate: 2019/12/10 16:49
 * @Description: 类描述
 */
public class TencentMapLayout extends BaseMap {

    private MapView tMapView;
    private TencentMap tMap;
    private Marker tMyMarker;
    private Circle mCircle;

    private Marker tDroneMarker;      // 飞机
    private Polyline tPolyline;             // 指点飞行路径
    private List<Marker> tMarkerList = new ArrayList<>(); // 航点飞行标记

    public TencentMapLayout(Context context, Location location) {
        super(context, location);
    }

    @Override
    public void init(FrameLayout layout) {
        tMapView = new MapView(getContext());
        layout.addView(tMapView);
        tMapView.onCreate(null);
        tMap = tMapView.getMap();
//        //设置实时路况开启
//        tMap.setTrafficEnabled(true);

        UiSettings uiSettings = tMapView.getUiSettings();

        //设置logo到屏幕底部中心
        uiSettings.setLogoPosition(UiSettings.LOGO_POSITION_CENTER_BOTTOM);
        //设置比例尺到屏幕右下角
        uiSettings.setScaleViewPosition(UiSettings.SCALEVIEW_POSITION_RIGHT_BOTTOM);
        //启用缩放手势(更多的手势控制请参考开发手册)
        uiSettings.setZoomGesturesEnabled(true);

        // 设置缩放级别
        tMap.moveCamera(CameraUpdateFactory.zoomTo(22));

        tMap.setOnMapClickListener(new TencentMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                // 转为wgs84
                double[] wgs84 = CoordinateTransformUtil.gcj02towgs84(latLng.getLongitude(), latLng.getLatitude());
                if (onMapClickListener != null) {
                    onMapClickListener.onMapClick(new LngLat(wgs84[0], wgs84[1]));
                }
                addPointMarker(new LngLat(wgs84[0], wgs84[1]));
            }
        });


        moveMyLocation();

        tMap.setOnMapLoadedListener(new TencentMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                isMapReady = true;
                setMyLocation(mLocation);
            }
        });

    }

    @Override
    public void moveMyLocation() {
        if (mLocation != null) {

            // 坐标转换
            LatLng latLng = getLatLng(mLocation);

            CameraPosition cameraPosition = CameraPosition.fromLatLngZoom(latLng, 22);

            //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            tMap.moveCamera(mCameraUpdate);
        }
    }

    @Override
    public void setMyLocation(Location location) {
        mLocation = location;
        if (!isMapReady || mLocation == null) {
            return;
        }
        mLocation = location;
        // 坐标转换
        LatLng latLng = getLatLng(location);
        if (tMyMarker == null) {
            tMyMarker = tMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(mMyIcon)));
        } else {
            tMyMarker.setPosition(latLng);
        }

        if (isHasArea) {
            if (mCircle == null) {
                mCircle = tMap.addCircle(new CircleOptions().
                        center(latLng).
                        radius(mMaxDistance).
                        fillColor(Color.argb(50, 2, 146, 255)).
                        strokeColor(Color.argb(50, 1, 1, 1)).
                        strokeWidth(3));
            } else {
                mCircle.setCenter(latLng);
            }
        }
    }


    @Override
    public void moveDroneLocation() {
        if (tDroneMarker != null) {
            CameraPosition cameraPosition = CameraPosition.fromLatLngZoom(tDroneMarker.getPosition(), 22);
            //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            tMap.moveCamera(mCameraUpdate);
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

        if (null == tDroneMarker) {
            // 设置 飞机 图标
            tDroneMarker = tMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(mPlaneIcon)));
        } else {
            tDroneMarker.setPosition(latLng);
        }
        tDroneMarker.setAnchor(0.5f, 0.5f);
        tDroneMarker.setRotation(360 - angle);
    }

    @Override
    public void setMapType(int type) {
        //设置卫星底图
        tMap.setSatelliteEnabled(type != 0);
    }

    @Override
    public void addPointMarker(LngLat lngLat) {
        if (isOnlyLook || mCircle == null) {
            return;
        }

        double[] gcj02 = CoordinateTransformUtil.wgs84togcj02(lngLat.getLongitude(), lngLat.getLatitude());
        LatLng latLng = new LatLng(gcj02[1], gcj02[0]);

        if (isHasArea && !mCircle.contains(latLng)) {
            Toast.makeText(getContext(), getContext().getString(mInvalidPointRes), Toast.LENGTH_SHORT).show();
            return;
        }

        if (mLngLats.size() < mMaxPoint) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            if (mPointRes == 0) {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(mPointResArray[mLngLats.size()]));
            } else {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(mPointRes));
            }
            Marker marker = tMap.addMarker(markerOptions);
            tMarkerList.add(marker);

            PolylineOptions options = new PolylineOptions().color(Color.parseColor("#FF959595")).width(8);
            for (Marker marker1 : tMarkerList) {
                options.add(marker1.getPosition());
            }

            if (tPolyline == null) {
                tPolyline = tMap.addPolyline(options);
                tPolyline.setWidth(4);
            } else {
                tPolyline.setPoints(options.getPoints());
            }
            mLngLats.add(lngLat);
        }
    }

    @Override
    public void deleteAllMarker() {
        mLngLats.clear();

        if (tMarkerList != null) {
            for (Marker marker : tMarkerList) {
                marker.remove();
            }
            tMarkerList.clear();
        }

        if (tPolyline != null) {
            tPolyline.remove();
            tPolyline = null;
        }
    }

    @Override
    public void deleteSingleMarker() {
        int index = mLngLats.size() - 1;
        if (index < 0) {
            return;
        }
        mLngLats.remove(index);

        if (tMarkerList != null) {
            tMarkerList.get(index).remove();
            tMarkerList.remove(index);
        }

        PolylineOptions options = new PolylineOptions().color(Color.parseColor("#FF959595")).width(8);
        for (Marker marker1 : tMarkerList) {
            options.add(marker1.getPosition());
        }

        if (tPolyline != null) {
            tPolyline.remove();
            tPolyline = null;
        }
        tPolyline = tMap.addPolyline(options);
    }

    @Override
    public void deleteFlyPolyline() {

    }

    @Override
    public void onRotate(float orientation) {

    }

    @Override
    public void uiSettings(MapUiSettings settings) {

    }

    @Override
    public void onResume() {
        tMapView.onResume();
    }

    @Override
    public void onPause() {
        tMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tMapView.onDestroy();
    }

    private LatLng getLatLng(Location location) {
        double[] gcj02 = CoordinateTransformUtil.wgs84togcj02(location.getLongitude(), location.getLatitude());
        return new LatLng(gcj02[1], gcj02[0]);
    }
}
