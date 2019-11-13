//package com.vison.base_map;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.location.Location;
//import android.widget.FrameLayout;
//
//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @Author: Sanerly
// * @CreateDate: 2019/10/19 10:36
// * @Description: 类描述
// */
//public class Map {
//    protected Builder mBuilder;
//    protected boolean isMapReady = false;
//    protected Location mLocation;
//    public List<LngLat> mLngLats;
//
//    public Map(Builder builder) {
//        this.mBuilder = builder;
//        this.mLocation = builder.mLocation;
//        this.mLngLats = new ArrayList<>();
//        if (mBuilder.isCompass) {
//            CompassUtils mCompassUtils = new CompassUtils(mBuilder.context);
//            mCompassUtils.setCompassLister(mCompassLister);
//        }
//    }
//
//
//    public Context getContext() {
//        return mBuilder.context;
//    }
//
//    /**
//     * @return 地图准备就绪
//     */
//    public boolean isMapReady() {
//        return isMapReady;
//    }
//
//    /**
//     * @param multipoint 多点或者单点
//     * @return
//     */
//    public void isMultipoint(boolean multipoint) {
//        mBuilder.isMultipoint = multipoint;
//    }
//
//    private CompassUtils.CompassLister mCompassLister = new CompassUtils.CompassLister() {
//        @Override
//        public void onOrientationChange(float orientation) {
//            // 横屏数值
//            orientation = 360 - (orientation + 90);
//            onRotate(orientation);
//        }
//    };
//
//    /**
//     * 设置手机的位置
//     */
//    public void setMyLocation(Location location) {
//
//    }
//
//    /**
//     * 移动到手机为中心位置
//     */
//    public void moveMyLocation() {
//
//    }
//
//
//    /**
//     * 设置飞机位置
//     */
//    public void setDroneLocation(double longitude, double latitude, float angle) {
//
//    }
//
//    /**
//     * 移动到飞机为中心位置
//     */
//    public void moveDroneLocation() {
//
//    }
//
//
//    /**
//     * 设置地图类型
//     */
//    public void setMapType(int type) {
//
//    }
//
//    /**
//     * 添加航点
//     */
//    public void addPointMarker(LngLat lngLat) {
//
//    }
//
//    /**
//     * 删除全部航点
//     */
//    public void deleteAllMarker() {
//
//    }
//
//    /**
//     * 删除单个航点
//     */
//    public void deleteSingleMarker() {
//
//    }
//
//    /**
//     * 旋转
//     */
//    public void onRotate(float orientation) {
//
//    }
//
//
//    /**
//     * 重新绘制加载地图
//     */
//    public void onResume() {
//
//    }
//
//    /**
//     * 暂停地图的绘制
//     */
//    public void onPause() {
//
//    }
//
//    /**
//     * 销毁地图
//     */
//    public void onDestroy() {
//
//    }
//
//    /**
//     * 地图点击监听
//     */
//    public interface OnMapClickListener {
//        void onMapClick(LngLat lngLat);
//    }
//
//    public static class Builder {
//        public Context context;
//        public Location mLocation;
//        public FrameLayout parentLayout;
//        public int pointMarker;
//        public int[] pointMarkerArray;
//        public boolean isMultipoint = true;
//        public int pointLength = 15;
//        public int areaRadius = 50;
//        public boolean isHasArea = true;
//        public String outAreaText;
//        public int myIcon;
//        public int planeIcon;
//        public boolean isOnlyLook = false;
//        public MapUiSettings uiSettings;
//        public boolean isCompass = true;
//        public OnMapClickListener onMapClickListener;
//
//
//        /**
//         * @param context      上下文
//         * @param mLocation    地理位置的数据类。
//         * @param parentLayout 包裹地图的父类(必须是FrameLayout)
//         */
//        public Builder(Context context, Location mLocation, FrameLayout parentLayout) {
//            this.context = context;
//            this.mLocation = mLocation;
//            this.parentLayout = parentLayout;
//            uiSettings=new MapUiSettings();
//        }
//
//        /**
//         * @param array  设置航点图标的数组
//         * @param length 设置航点的最大数量
//         */
//        public Builder setPointMarkerArray(int[] array, int length) {
//            this.pointMarkerArray = array;
//            this.pointLength = length;
//            return this;
//        }
//
//        /**
//         * @param array 设置航点图标的数组
//         */
//        public Builder setPointMarkerArray(int[] array) {
//            this.pointMarkerArray = array;
//            this.pointLength = array.length;
//            return this;
//        }
//
//        /**
//         * @param multipoint 多点或者单点
//         * @return
//         */
//        public Builder setMultipoint(boolean multipoint) {
//            isMultipoint = multipoint;
//            return this;
//        }
//
//        /**
//         * @param length 设置航点的最大数量
//         */
//        public Builder setPointLength(int length) {
//            this.pointLength = length;
//            return this;
//        }
//
//        /**
//         * @param isHasArea 创建航点范围
//         */
//        public Builder setHasArea(boolean isHasArea) {
//            this.isHasArea = isHasArea;
//            return this;
//        }
//
//
//        /**
//         * @param radius 设置航点范围的半径
//         */
//        public Builder setAreaRadis(int radius) {
//            this.areaRadius = radius;
//            return this;
//        }
//
//        /**
//         * @param text 触摸航点范围外的文字提示
//         */
//        public Builder setOutAreaText(String text) {
//            this.outAreaText = text;
//            return this;
//        }
//
//        /**
//         * @param onlyLook 只看地图,不能触摸
//         */
//        public Builder setOnlyLook(boolean onlyLook) {
//            this.isOnlyLook = onlyLook;
//            return this;
//        }
//
//        /**
//         * @param icon 设置自己位置ICON
//         */
//        public Builder setMyIcon(int icon) {
//            this.myIcon = icon;
//            return this;
//        }
//
//        /**
//         * @param icon 设置飞机位置ICON
//         */
//        public Builder setPlaneIcon(int icon) {
//            this.planeIcon = icon;
//            return this;
//        }
//
//        /**
//         * @param uiSettings 设置地图的默认Settings
//         */
//        public Builder setUiSettings(MapUiSettings uiSettings) {
//            this.uiSettings = uiSettings;
//            return this;
//        }
//
//        /**
//         * @param compass 设置地图旋转
//         */
//        public Builder setCompass(boolean compass) {
//            this.isCompass = compass;
//            return this;
//        }
//
//        /**
//         * 设置地图点击监听
//         */
//        public Builder setOnMapClickListener(OnMapClickListener listener) {
//            this.onMapClickListener = listener;
//            return this;
//        }
//
//        Class<? extends Map> cls;
//
//        public Builder setChildClass(Class<? extends Map> cls) {
//            this.cls = cls;
//            return this;
//        }
//
//        public Map biuld() {
//            Map aClass = null;
//            Constructor<?> cons = null;
//            try {
//                cons = cls.getDeclaredConstructor(Builder.class);
//                aClass = (Map) cons.newInstance(this);
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//
//            return aClass;
//        }
//    }
//}
