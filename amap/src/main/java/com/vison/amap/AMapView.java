package com.vison.amap;

import android.content.Context;
import android.location.Location;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vison.base_map.BaseMap;
import com.vison.base_map.widget.RCRelativeLayout;


/**
 * @Author: Sanerly
 * @CreateDate: 2019/8/12 16:08
 * @Description: 类描述
 */
public class AMapView extends RCRelativeLayout {
    private BaseMap mBaseMap;

    public AMapView(@NonNull Context context) {
        this(context, null);
    }

    public AMapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AMapView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void init(Location location) {
        mBaseMap = new GaodeMapLayout(getContext(), location);
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
