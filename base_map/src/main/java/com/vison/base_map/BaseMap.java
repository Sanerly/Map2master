package com.vison.base_map;

import android.content.Context;
import android.location.Location;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Sanerly
 * @CreateDate: 2019/7/15 17:29
 * @Description: 类描述
 */
public abstract class BaseMap {
    private Context context;
    protected List<LngLat> mLngLats;
    protected Location mLocation;
    protected int mPointRes;
    protected int[] mPointResArray;
    protected int mMyIcon;
    protected int mPlaneIcon;
    protected int mInvalidPointRes;
    protected int mMaxPoint = 15;
    protected int mMaxDistance = 50;
    protected boolean isHasArea = true;
    protected boolean isMapReady;        //地图准备就绪
    protected boolean isOnlyLook = false;
    protected OnMapClickListener onMapClickListener;
    private CompassUtils mCompassUtils;

    public BaseMap(Context context, Location location) {
        this.context = context;
        this.mLocation = location;
        mLngLats = new ArrayList<>();
    }

    public Context getContext() {
        return context;
    }

    /**
     * 获取已添加的所有航点
     */
    public List<LngLat> getLngLats() {
        return mLngLats;
    }


    /**
     * 设置地图是否旋转
     */
    public BaseMap setCompass(boolean isCompass) {
        if (isCompass && mCompassUtils == null) {
            mCompassUtils = new CompassUtils(context);
            mCompassUtils.setCompassLister(mCompassLister);
        }
        return this;
    }

    private CompassUtils.CompassLister mCompassLister = new CompassUtils.CompassLister() {
        @Override
        public void onOrientationChange(float orientation) {
            onRotate(orientation);
        }
    };

    /**
     * 设置航点图标的数组
     */
    public BaseMap setPointRes(int[] array, int max) {
        this.mPointResArray = array;
        mMaxPoint = max;
        return this;
    }

    /**
     * 设置无数字标记，单个航点图标
     */
    public BaseMap setPointRes(int res, int max) {
        this.mPointRes = res;
        this.mMaxPoint = max;
        return this;
    }

    /**
     * 设置航点数量
     */
    public BaseMap setMaxPoint(int maxPoint) {
        mMaxPoint = maxPoint;
        return this;
    }

    /**
     * 设置航点范围的半径
     */
    public BaseMap setMaxDistance(int mMaxDistance) {
        this.mMaxDistance = mMaxDistance;
        return this;
    }

    /**
     * 是否显示区域
     */
    public BaseMap setHasArea(boolean isHasArea, int res) {
        this.isHasArea = isHasArea;
        this.mInvalidPointRes = res;
        return this;
    }

    /**
     * 只看地图
     */
    public BaseMap setOnlyLook(boolean onlyLook) {
        isOnlyLook = onlyLook;
        return this;
    }

    /**
     * 设置自己位置ICON
     */
    public BaseMap setMyIcon(int icon) {
        this.mMyIcon = icon;
        return this;
    }

    /**
     * 设置飞机位置ICON
     */
    public BaseMap setPlaneIcon(int icon) {
        this.mPlaneIcon = icon;
        return this;
    }

    /**
     * 设置点击范围无效点的提示
     */
    public BaseMap setInvalidPointRes(int res) {
        this.mInvalidPointRes = res;
        return this;
    }


    /**
     * 设置地图的默认UI
     */
    public BaseMap setUiSettings(MapUiSettings uiSettings) {
        this.uiSettings(uiSettings);
        return this;
    }

    /**
     * 设置地图点击监听
     */
    public BaseMap setOnMapClickListener(OnMapClickListener listener) {
        this.onMapClickListener = listener;
        return this;
    }


    /**
     * 地图点击监听
     */
    public interface OnMapClickListener {
        void onMapClick(LngLat lngLat);
    }


    /**
     * 开始构建
     */
    public void build() {
        setMyLocation(mLocation);
    }

    /**
     * 初始化
     */
    public abstract void init(FrameLayout layout);

    /**
     * 移动到手机为中心位置
     */
    public abstract void moveMyLocation();

    /**
     * 设置手机的位置
     */
    public abstract void setMyLocation(Location location);


    /**
     * 移动到飞机为中心位置
     */
    public abstract void moveDroneLocation();

    /**
     * 设置飞机位置
     */
    public abstract void setDroneLocation(double longitude, double latitude, float angle);

    /**
     * 设置地图类型
     */
    public abstract void setMapType(int type);

    /**
     * 添加航点
     */
    public abstract void addPointMarker(LngLat lngLat);

    /**
     * 删除全部航点
     */
    public abstract void deleteAllMarker();

    /**
     * 删除单个航点
     */
    public abstract void deleteSingleMarker();

    /**
     * 旋转
     */
    public abstract void onRotate(float orientation);


    public abstract void uiSettings(MapUiSettings settings);


    /**
     * 重新绘制加载地图
     */
    public abstract void onResume();

    /**
     * 暂停地图的绘制
     */
    public abstract void onPause();

    /**
     * 销毁地图
     */
    public void onDestroy() {
        if (mCompassUtils != null) {
            mCompassUtils.unbind();
        }
    }

}
