package com.vison.gmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.vison.base_map.BaseMap;
import com.vison.base_map.CoordinateTransformUtil;
import com.vison.base_map.LngLat;
import com.vison.base_map.LocationUtils;
import com.vison.base_map.MapUiSettings;
import com.vison.base_map.bean.FeaturesBean;
import com.vison.base_map.bean.GeometryBean;
import com.vison.base_map.bean.PropertiesBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Sanerly
 * @CreateDate: 2019/7/15 17:33
 * @Description: 类描述
 */
public class GoogleMapLayout extends BaseMap implements OnMapReadyCallback {

    protected MapView gMapView;
    protected GoogleMap gMap;
    protected Marker aMyMarker;             // 手机定位点
    protected Marker aDroneMarker;          // 飞机
    protected Polyline aPolyline;           // 指点飞行路径
    protected Polyline aHomePolyline;       // 起飞点到当前位置连线
    protected Polyline aPhonePolyline;      // 手机定位点到当前位置连线
    protected Polyline aDroneTrackline;     //飞机轨迹路径
    protected List<Marker> aMarkerList = new ArrayList<>(); // 航点飞行标记
    protected Circle mCircle;
    private Marker startMarker;
    private Marker endMarker;
    protected double mHomeLon = 0;
    protected double mHomeLat = 0;
    private Marker homeMarker;
    private final List<Polygon> mNoFlyZonePolygons = new ArrayList<>();   //危险-多边形
    private final List<Circle> mNoFlyZonePoints = new ArrayList<>();   //危险-点
    private final List<Polyline> mNoFlyZoneLines = new ArrayList<>();   //危险-线

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
    public boolean moveMyLocation() {
        if (aMyMarker != null) {
            CameraPosition cameraPosition = gMap.getCameraPosition();
            //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(aMyMarker.getPosition(),
                    mZoomLevel, cameraPosition.tilt, cameraPosition.bearing));
            gMap.moveCamera(mCameraUpdate);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setMyLocation(Location location) {
        if (!isMapReady || location == null) {
            return;
        }
        mLocation = location;
        // 坐标转换
        LatLng latLng;
        if (isLocationConvert) {
            double[] gcj02 = CoordinateTransformUtil.wgs84togcj02(location.getLongitude(), location.getLatitude());
            latLng = new LatLng(gcj02[1], gcj02[0]);
        } else {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
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
                        .fillColor(mFillColor).
                        strokeColor(mStrokeColor).
                        strokeWidth(mStrokeWidth));
            } else {
                mCircle.setCenter(latLng);
            }
        }
    }

    @Override
    public void setResetMaxDistance(int distance) {
        if (!isMapReady || mLocation == null) {
            return;
        }
        mMaxDistance = distance;
        // 坐标转换
        LatLng latLng;
        if (isLocationConvert) {
            double[] gcj02 = CoordinateTransformUtil.wgs84togcj02(mLocation.getLongitude(), mLocation.getLatitude());
            latLng = new LatLng(gcj02[1], gcj02[0]);
        } else {
            latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        }

        if (isHasArea) {
            if (mCircle == null) {
                mCircle = gMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(distance)
                        .fillColor(mFillColor).
                        strokeColor(mStrokeColor).
                        strokeWidth(mStrokeWidth));
            } else {
                mCircle.setCenter(latLng);
                mCircle.setRadius(distance);
            }
        }
    }

    @Override
    public boolean moveDroneLocation() {
        if (aDroneMarker != null) {
            CameraPosition cameraPosition = gMap.getCameraPosition();
            //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(aDroneMarker.getPosition(),
                    mZoomLevel, cameraPosition.tilt, cameraPosition.bearing));
            gMap.moveCamera(mCameraUpdate);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setDroneLocation(double longitude, double latitude, float angle) {
        if (!isMapReady) {
            return;
        }
        // 坐标转换
        LatLng latLng;
        if (isLocationConvert) {
            double[] gcj02 = CoordinateTransformUtil.wgs84togcj02(longitude, latitude);
            latLng = new LatLng(gcj02[1], gcj02[0]);
        } else {
            latLng = new LatLng(latitude, longitude);
        }

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
        aDroneMarker.setRotation(angle);

        if (mHomeLon != 0 && mHomeLat != 0) {
            PolylineOptions options = new PolylineOptions().color(Color.parseColor("#FFFF0000")).width(8);
            LatLng latLngLine;
            if (isLocationConvert) {
                double[] gcj02Line = CoordinateTransformUtil.wgs84togcj02(mHomeLon, mHomeLat);
                latLngLine = new LatLng(gcj02Line[1], gcj02Line[0]);
            } else {
                latLngLine = new LatLng(mHomeLat, mHomeLon);
            }
            options.add(latLngLine);
            options.add(aDroneMarker.getPosition());
            if (aHomePolyline == null) {
                aHomePolyline = gMap.addPolyline(options);
                aHomePolyline.setVisible(isShowHomeLine);
            } else {
                aHomePolyline.setPoints(options.getPoints());
                aHomePolyline.setVisible(isShowHomeLine);
            }
        }

        if (mLocation != null) {
            PolylineOptions options = new PolylineOptions().color(Color.parseColor("#FFFF0000")).width(8);
            double[] gcj02p = CoordinateTransformUtil.wgs84togcj02(mLocation.getLongitude(), mLocation.getLatitude());
            options.add(new LatLng(gcj02p[1], gcj02p[0]));
            options.add(aDroneMarker.getPosition());
            if (aPhonePolyline == null) {
                aPhonePolyline = gMap.addPolyline(options);
                aPhonePolyline.setVisible(isShowPhoneLine);
            } else {
                aPhonePolyline.setPoints(options.getPoints());
                aPhonePolyline.setVisible(isShowPhoneLine);
            }
        }

        if (isShowInfoWindow && aMyMarker != null) {
//            GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
//                // marker 对象被点击时回调的接口
//                // 返回 true 则表示接口已响应事件，否则返回false
//                @Override
//                public boolean onMarkerClick(Marker marker) {
//                    if (marker.getId().equals(aDroneMarker.getId())) {
//
//                    }
//                    return true;
//                }
//            };

            gMap.setInfoWindowAdapter(new InfoWindowAdapter(getContext()));
            double distance = CoordinateTransformUtil.getDistance(aMyMarker.getPosition().longitude, aMyMarker.getPosition().latitude,
                    aDroneMarker.getPosition().longitude, aDroneMarker.getPosition().latitude);
            aDroneMarker.setTitle("Last flight position of uav");
            String snippet = "longitude:" + aDroneMarker.getPosition().longitude + "\nlatitude:" + aDroneMarker.getPosition().latitude + "\nfrom your current position " + distance + "m";
            aDroneMarker.setSnippet(snippet);
            aDroneMarker.showInfoWindow();
//            gMap.setOnMarkerClickListener(onMarkerClickListener);
        }

    }

    @Override
    public void setHomePoint(double longitude, double latitude, int homeRes) {
        this.mHomeLon = longitude;
        this.mHomeLat = latitude;
        if (homeRes != 0) {
            LatLng latLng;
            if (isLocationConvert) {
                double[] gcj02Line = CoordinateTransformUtil.wgs84togcj02(mHomeLon, mHomeLat);
                latLng = new LatLng(gcj02Line[1], gcj02Line[0]);
            } else {
                latLng = new LatLng(latitude, longitude);
            }

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(homeRes));
            if (homeMarker != null) {
                homeMarker.remove();
            }
            homeMarker = gMap.addMarker(markerOptions);
        }
    }

    @Override
    public void setShowHomeLine(boolean visible) {
        super.setShowHomeLine(visible);
        if (aHomePolyline != null) {
            aHomePolyline.setVisible(visible);
        }
    }

    @Override
    public void setShowPhoneLine(boolean visible) {
        super.setShowPhoneLine(visible);
        if (aPhonePolyline != null) {
            aPhonePolyline.setVisible(visible);
        }
    }

    @Override
    public boolean moveCurrentLocation(double longitude, double latitude, float tilt) {
        // 坐标转换
        LatLng latLng;
        if (isLocationConvert) {
            double[] gcj02 = CoordinateTransformUtil.wgs84togcj02(longitude, latitude);
            latLng = new LatLng(gcj02[1], gcj02[0]);
        } else {
            latLng = new LatLng(latitude, longitude);
        }
        if (gMap != null) {
            CameraPosition cameraPosition = gMap.getCameraPosition();
            //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,
                    mZoomLevel, tilt, cameraPosition.bearing));
            gMap.moveCamera(mCameraUpdate);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setMapType(int type) {
        switch (type) {
            case 0:
                if (gMap != null) {
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
            // 坐标转换
            LatLng latLng;
            if (isLocationConvert) {
                double[] gcj02 = CoordinateTransformUtil.wgs84togcj02(lngLat.getLongitude(), lngLat.getLatitude());
                latLng = new LatLng(gcj02[1], gcj02[0]);
            } else {
                latLng = new LatLng(lngLat.getLatitude(), lngLat.getLongitude());
            }
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
    public void setPointMarker(LngLat lngLat) {
        if (isOnlyLook) {
            return;
        }

        if (mLngLats.size() < mMaxPoint) {
            // 坐标转换
            LatLng latLng;
            if (isLocationConvert) {
                double[] gcj02 = CoordinateTransformUtil.wgs84togcj02(lngLat.getLongitude(), lngLat.getLatitude());
                latLng = new LatLng(gcj02[1], gcj02[0]);
            } else {
                latLng = new LatLng(lngLat.getLatitude(), lngLat.getLongitude());
            }
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
    public void deleteHomePolyline() {
        if (aHomePolyline != null) {
            aHomePolyline.remove();
            aHomePolyline = null;
        }
    }

    @Override
    public void deletePhonePolyline() {
        if (aPhonePolyline != null) {
            aPhonePolyline.remove();
            aPhonePolyline = null;
        }
    }

    @Override
    public void deleteHomeMarker() {
        if (homeMarker != null) {
            homeMarker.remove();
        }
    }

    @Override
    public void drawMoveTrack(List<LngLat> lngLats, int texture, int color, int start, int end) {
        if (onMoveTrackListener != null) {
            onMoveTrackListener.onDrawState(true);
        }
        PolylineOptions options = new PolylineOptions().color(color).width(8);
        for (int i = 0; i < lngLats.size(); i++) {
            LatLng latLng;
            if (isLocationConvert) {
                double[] gcj02 = CoordinateTransformUtil.wgs84togcj02(lngLats.get(i).getLongitude(), lngLats.get(i).getLatitude());
                latLng = new LatLng(gcj02[1], gcj02[0]);
            } else {
                latLng = new LatLng(lngLats.get(i).getLatitude(), lngLats.get(i).getLongitude());
            }

            options.add(latLng);
            if (onMoveTrackListener != null) {
                onMoveTrackListener.onDrawPosition(i, lngLats.get(i).getLongitude(), lngLats.get(i).getLatitude());
            }
        }
        aDroneTrackline = gMap.addPolyline(options);

        if (lngLats.size() > 1 && start != 0 && end != -0) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(aDroneTrackline.getPoints().get(0));
            markerOptions.icon(BitmapDescriptorFactory.fromResource(start));
            startMarker = gMap.addMarker(markerOptions);

            MarkerOptions endMarkerOptions = new MarkerOptions();
            endMarkerOptions.position(aDroneTrackline.getPoints().get(aDroneTrackline.getPoints().size() - 1));
            endMarkerOptions.icon(BitmapDescriptorFactory.fromResource(end));
            endMarker = gMap.addMarker(endMarkerOptions);
        }
        if (onMoveTrackListener != null) {
            onMoveTrackListener.onDrawState(false);
        }
    }

    @Override
    public void deleteMoveTrack() {
        if (aDroneTrackline != null) {
            aDroneTrackline.remove();
            aDroneTrackline = null;
        }

        if (startMarker != null) {
            startMarker.remove();
        }
        if (endMarker != null) {
            endMarker.remove();
        }
    }


    @Override
    public void onRotate(float orientation) {
        if (gMap == null) {
            return;
        }
        CameraPosition cameraPosition = gMap.getCameraPosition();
        //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(cameraPosition.target, cameraPosition.zoom, cameraPosition.tilt, orientation));
        gMap.moveCamera(mCameraUpdate);
    }

    @Override
    public float bearing() {
        if (gMap == null) {
            return 0;
        }
        return gMap.getCameraPosition().bearing;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void uiSettings(MapUiSettings settings) {
        if (gMap == null) {
            return;
        }
        UiSettings uiSettings = gMap.getUiSettings();
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
            gMap.setLocationSource(new LocationSource() {
                @Override
                public void activate(OnLocationChangedListener onLocationChangedListener) {
                    moveMyLocation();
                }

                @Override
                public void deactivate() {

                }
            });
            gMap.setMyLocationEnabled(settings.isMyLocationButtonEnabled());
        }


    }

    @Override
    public void addMarker(int icon, double longitude, double latitude) {
        if (gMap == null) {
            return;
        }
        // 坐标转换
        LatLng latLng;
        if (isLocationConvert) {
            double[] gcj02 = CoordinateTransformUtil.wgs84togcj02(longitude, latitude);
            latLng = new LatLng(gcj02[1], gcj02[0]);
        } else {
            latLng = new LatLng(latitude, longitude);
        }

        // 设置 飞机 图标
        Marker marker = gMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(icon)));
        //设置旋转角度
        marker.setAnchor(0.5f, 0.5f);
        marker.setRotation(0);

    }

    @Override
    public void changeMaxDistance(int distance) {
        if (mCircle != null) {
            mMaxDistance = distance;
            mCircle.setRadius(distance);
        }
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
        super.onDestroy();
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
        uiSettings.setCompassEnabled(false);

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
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(mZoomLevel);
        gMap.animateCamera(zoom);
        // 设置最小缩放级别为2km
        gMap.setMinZoomPreference(mMinZoomLevel);

        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // 转为wgs84
                // 坐标转换
                LatLng latLngConvert;
                if (isLocationConvert) {
                    double[] wgs84 = CoordinateTransformUtil.gcj02towgs84(latLng.longitude, latLng.latitude);
                    latLngConvert = new LatLng(wgs84[1], wgs84[0]);
                } else {
                    latLngConvert = new LatLng(latLng.latitude, latLng.longitude);
                }
                if (onMapClickListener != null) {
                    onMapClickListener.onMapClick(new LngLat(latLngConvert.longitude, latLngConvert.latitude));
                }
                addPointMarker(new LngLat(latLngConvert.longitude, latLngConvert.latitude));
            }
        });


        if (mLocation != null) {
            CameraPosition cameraPosition = gMap.getCameraPosition();
            // 坐标转换
            LatLng latLng;
            if (isLocationConvert) {
                double[] gcj02 = CoordinateTransformUtil.wgs84togcj02(mLocation.getLongitude(), mLocation.getLatitude());
                latLng = new LatLng(gcj02[1], gcj02[0]);
            } else {
                latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            }
            //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, mZoomLevel, cameraPosition.tilt, cameraPosition.bearing));
            gMap.moveCamera(mCameraUpdate);

        }

        gMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                isMapReady = true;
                setMyLocation(mLocation);
                moveMyLocation();
            }
        });
        isMapReady = true;
    }

    @Override
    public void addDangerPolygon(FeaturesBean featuresBean) {
        GeometryBean geometryBean = featuresBean.getGeometry();
        if (geometryBean == null) {
            return;
        }
        if (geometryBean.getMMultiple() == null) {
            return;
        }

        for (List<LngLat> lngLats : geometryBean.getMMultiple()) {
            PolygonOptions options = new PolygonOptions()
                    .strokeColor(Color.TRANSPARENT)
                    .strokeWidth(8)
                    .fillColor(Color.parseColor("#60ff0000"));
            for (LngLat lngLat : lngLats) {
                options.add(new LatLng(lngLat.getLatitude(), lngLat.getLongitude()));
            }
            Polygon polygon = gMap.addPolygon(options);
            mNoFlyZonePolygons.add(polygon);
        }
    }

    /**
     * 添加危险区域
     * (点)
     */
    @Override
    public void addDangerPoint(FeaturesBean featuresBean) {
        LngLat lngLat = featuresBean.getGeometry().getSingle();
        if (lngLat == null) {
            return;
        }
        Circle circle = gMap.addCircle(new CircleOptions().
                center(new LatLng(lngLat.getLatitude(), lngLat.getLongitude())).
                radius(featuresBean.getProperties().getRadius()).
                fillColor(Color.parseColor("#60ff0000")).
                strokeColor(Color.parseColor("#ff0000")).
                strokeWidth(6));
        mNoFlyZonePoints.add(circle);
    }

    @Override
    public void addDangerLine(FeaturesBean featuresBean) {
        PropertiesBean propertiesBean = featuresBean.getProperties();
        PolylineOptions options = new PolylineOptions()
                .width(propertiesBean.getStrokeWidth() * 8)
                .color(Color.parseColor(propertiesBean.getStrokeColor()));
        for (LngLat lngLat : featuresBean.getGeometry().getMultiple()) {
            // 坐标转换
            double[] gcj02 = CoordinateTransformUtil.wgs84togcj02(lngLat.getLongitude(), lngLat.getLatitude());
            LatLng latLng = new LatLng(gcj02[1], gcj02[0]);
            options.add(latLng);
        }
        Polyline polyline = gMap.addPolyline(options);
        mNoFlyZoneLines.add(polyline);
    }

    /**
     * 清除 danger view
     */
    @Override
    public void cleanNoFlyZone() {
        for (Polygon gDangerPolygon : mNoFlyZonePolygons) {
            gDangerPolygon.remove();
        }
        mNoFlyZonePolygons.clear();

        for (Circle gDangerPoint : mNoFlyZonePoints) {
            gDangerPoint.remove();
        }
        mNoFlyZonePoints.clear();

        for (Polyline gDangerLine : mNoFlyZoneLines) {
            gDangerLine.remove();
        }
        mNoFlyZoneLines.clear();
    }

    /**
     * 检查是否在禁飞区内
     */
    @Override
    public boolean checkInNoFlyZone(double longitude, double latitude) {
        boolean isInNoFlyZone = false;
        // 检查是否在多边形内
        for (Polygon gNoflyzonePolygon : mNoFlyZonePolygons) {
            if (PolyUtil.containsLocation(new LatLng(latitude, longitude), gNoflyzonePolygon.getPoints(), false)) {
                isInNoFlyZone = true;
            }
        }

        // 检查是否在圆形内
        for (Circle circle : mNoFlyZonePoints) {
            LatLng latLng = circle.getCenter();
            if (LocationUtils.getDistance(longitude, latitude, latLng.longitude, latLng.latitude) <= circle.getRadius()) {
                isInNoFlyZone = true;
            }
        }

        return isInNoFlyZone;
    }
}
