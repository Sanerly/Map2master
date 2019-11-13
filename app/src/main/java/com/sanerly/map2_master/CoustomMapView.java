package com.sanerly.map2_master;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.vsion.map2.BaseMapView;

/**
 * @Author: Sanerly
 * @CreateDate: 2019/8/12 16:49
 * @Description: 类描述
 */
public class CoustomMapView extends BaseMapView {
    public CoustomMapView(@NonNull Context context) {
        this(context, null);
    }

    public CoustomMapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoustomMapView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init(Location location, boolean isChina) {
        super.init(location, isChina);
    }
}
