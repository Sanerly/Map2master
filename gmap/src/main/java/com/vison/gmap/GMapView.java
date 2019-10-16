package com.vison.gmap;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.vison.base_map.BaseMap;


/**
 * @Author: Sanerly
 * @CreateDate: 2019/8/12 16:08
 * @Description: 类描述
 */
public class GMapView extends FrameLayout {
    private BaseMap mBaseMap;

    public GMapView(@NonNull Context context) {
        this(context, null);
    }

    public GMapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GMapView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void init(Location location) {
        mBaseMap = new GoogleMapLayout(getContext(), location);
        mBaseMap.init(this);
    }

    public BaseMap getBaseMap() {
        return mBaseMap;
    }

    /**
     * 重新绘制加载地图
     */
    public void onResume() {
        if (null != mBaseMap) {
            mBaseMap.onResume();
        }
    }

    /**
     * 暂停地图的绘制
     */
    public void onPause() {
        if (null != mBaseMap) {
            mBaseMap.onPause();
        }
    }

    /**
     * 销毁地图
     */
    public void onDestroy() {
        if (null != mBaseMap) {
            mBaseMap.onDestroy();
        }
    }
}
