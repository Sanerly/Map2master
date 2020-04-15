package com.vison.amap;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.vison.base_map.BaseMap;
import com.vison.base_map.CoordinateTransformUtil;
import com.vison.base_map.LngLat;
import com.vison.base_map.MapUiSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Sanerly
 * @CreateDate: 2019/7/15 17:31
 * @Description: 类描述
 */
public class GaodeMapLayout extends BaseMap {

    private TextureMapView aMapView;
    private AMap aMap;
    private Marker aMyMarker;      // 手机定位点
    private Marker aDroneMarker;      // 飞机
    private Polyline aPolyline;             // 指点飞行路径
    private Polyline aFlyPolyline;             // 起飞点到当前位置连线
    private List<Marker> aMarkerList = new ArrayList<>(); // 航点飞行标记
    private Circle mCircle;

    public GaodeMapLayout(Context context, Location location) {
        super(context, location);
    }


    @Override
    public void init(FrameLayout layout) {
        aMapView = new TextureMapView(getContext());
        layout.addView(aMapView);

        aMapView.onCreate(null);
        aMap = aMapView.getMap();

        UiSettings uiSettings = aMap.getUiSettings();
        //设置地图是否可以倾斜
        uiSettings.setTiltGesturesEnabled(false);
        //设置地图是否可以旋转
        uiSettings.setRotateGesturesEnabled(false);
        //放大缩小按钮
        uiSettings.setZoomControlsEnabled(false);
        //显示比例尺
        uiSettings.setScaleControlsEnabled(true);
        // logo显示在右下角
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);

        uiSettings.setCompassEnabled(false);

        // 设置最小缩放级别为2km
        aMap.setMinZoomLevel(12);
        // 设置缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(22));

        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
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


        if (mLocation != null) {
            CameraPosition cameraPosition = aMap.getCameraPosition();
            // 坐标转换
            double[] gcj02 = CoordinateTransformUtil.wgs84togcj02(mLocation.getLongitude(), mLocation.getLatitude());
            LatLng latLng = new LatLng(gcj02[1], gcj02[0]);
            //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 22, cameraPosition.tilt, cameraPosition.bearing));
            aMap.moveCamera(mCameraUpdate);
        }


        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                isMapReady = true;
                setMyLocation(mLocation);
            }
        });
    }

    @Override
    public void moveMyLocation() {
        if (aMyMarker != null) {
            CameraPosition cameraPosition = aMap.getCameraPosition();
            //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(aMyMarker.getPosition(),
                    cameraPosition.zoom > 19 ? cameraPosition.zoom : 19, cameraPosition.tilt, cameraPosition.bearing));
            aMap.moveCamera(mCameraUpdate);
        }
    }

    @Override
    public void setMyLocation(Location location) {
        if (!isMapReady || mLocation == null) {
            return;
        }
        mLocation = location;
        // 坐标转换
        double[] gcj02 = CoordinateTransformUtil.wgs84togcj02(location.getLongitude(), location.getLatitude());
        LatLng latLng = new LatLng(gcj02[1], gcj02[0]);
        if (aMyMarker == null) {
            aMyMarker = aMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(mMyIcon)));
        } else {
            aMyMarker.setPosition(latLng);
        }

        if (isHasArea) {
            if (mCircle == null) {
                mCircle = aMap.addCircle(new CircleOptions().
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
        if (aDroneMarker != null) {
            CameraPosition cameraPosition = aMap.getCameraPosition();
            //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(aDroneMarker.getPosition(),
                    cameraPosition.zoom > 19 ? cameraPosition.zoom : 19, cameraPosition.tilt, cameraPosition.bearing));
            aMap.moveCamera(mCameraUpdate);
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
            aDroneMarker = aMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(mPlaneIcon)));
        } else {
            aDroneMarker.setPosition(latLng);
        }
        aDroneMarker.setAnchor(0.5f, 0.5f);
        aDroneMarker.setRotateAngle(360 - aMap.getCameraPosition().bearing - angle);


        if (isShowLine && mStartLongitude!=0 && mStartLongitude!=0) {
            PolylineOptions options = new PolylineOptions().color(Color.parseColor("#FFFF0000")).width(8);
            double[] gcj02Line = CoordinateTransformUtil.wgs84togcj02(mStartLongitude, mStartLatitude);
            options.add(new LatLng(gcj02Line[1], gcj02Line[0]));
            options.add(aDroneMarker.getPosition());
            if (aFlyPolyline == null) {
                aFlyPolyline = aMap.addPolyline(options);
            } else {
                aFlyPolyline.setOptions(options);
            }
        }

    }

    @Override
    public void setMapType(int type) {
        switch (type) {
            case 0:
                if (aMap != null) {
                    aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                }
                break;
            case 1:
                if (aMap != null) {
                    aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                }
                break;
            case 2:
                if (aMap != null) {
                    aMap.setMapType(AMap.MAP_TYPE_NIGHT);
                }
                break;
        }
    }

    @Override
    public void addPointMarker(LngLat lngLat) {
        if (isOnlyLook) {
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
            Marker marker = aMap.addMarker(markerOptions);
            aMarkerList.add(marker);

            PolylineOptions options = new PolylineOptions().color(Color.parseColor("#FF959595")).width(8);
            for (Marker marker1 : aMarkerList) {
                options.add(marker1.getPosition());
            }

            if (aPolyline == null) {
                aPolyline = aMap.addPolyline(options);
            } else {
                aPolyline.setOptions(options);

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
        aPolyline = aMap.addPolyline(options);
    }

    @Override
    public void deleteFlyPolyline() {
        if (aFlyPolyline != null) {
            aFlyPolyline.remove();
            aFlyPolyline = null;
        }
    }

    @Override
    public void onRotate(float orientation) {
        CameraPosition cameraPosition = aMap.getCameraPosition();
        //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(cameraPosition.target, cameraPosition.zoom, cameraPosition.tilt, orientation));
        aMap.moveCamera(mCameraUpdate);
    }

    @Override
    public void uiSettings(MapUiSettings settings) {
        if (aMap == null) {
            return;
        }
        UiSettings uiSettings = aMap.getUiSettings();
        //设置地图是否可以倾斜
        uiSettings.setTiltGesturesEnabled(settings.isTiltGesturesEnabled());
        //设置地图是否可以旋转
        uiSettings.setRotateGesturesEnabled(settings.isRotateGesturesEnabled());
        //放大缩小按钮
        uiSettings.setZoomControlsEnabled(settings.isZoomControlsEnabled());
        //显示指南针
        uiSettings.setCompassEnabled(settings.isCompassEnabled());
        //移动到我的位置
        uiSettings.setMyLocationButtonEnabled(settings.isMyLocationButtonEnabled());
        if (settings.isMyLocationButtonEnabled()) {
            aMap.setLocationSource(new LocationSource() {
                @Override
                public void activate(OnLocationChangedListener onLocationChangedListener) {
                    moveMyLocation();
                }

                @Override
                public void deactivate() {
                }
            });
            aMap.setMyLocationEnabled(settings.isMyLocationButtonEnabled());
        }
    }


    @Override
    public void onResume() {
        if (aMapView == null) {
            return;
        }
        aMapView.onResume();
    }

    @Override
    public void onPause() {
        if (aMapView == null) {
            return;
        }
        aMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (aMapView == null) {
            return;
        }
        aMapView.onDestroy();
    }
}
