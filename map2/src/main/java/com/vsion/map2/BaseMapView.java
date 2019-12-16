package com.vsion.map2;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.vison.base_map.BaseMap;
import com.vison.base_map.widget.RCRelativeLayout;
import com.vison.gmap.GoogleMapLayout;
import com.vison.tmap.TencentMapLayout;


/**
 * @Author: Sanerly
 * @CreateDate: 2019/8/12 16:08
 * @Description: 类描述
 */
public class BaseMapView extends RCRelativeLayout {
    private BaseMap mBaseMap;

    public BaseMapView(@NonNull Context context) {
        this(context, null);
    }

    public BaseMapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseMapView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void init(Location location, boolean isChina) {
        if (isChina) {
            mBaseMap = new TencentMapLayout(getContext(), location);
        } else {
            mBaseMap = new GoogleMapLayout(getContext(), location);
        }
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
