package com.vison.base_map;

/**
 * @Author: Sanerly
 * @CreateDate: 2019/9/18 10:25
 * @Description: 地图设置
 */
@Deprecated
public class MapUiSettings {
    //设置地图是否可以倾斜
    private boolean tiltGesturesEnabled;
    //设置地图是否可以旋转
    private boolean rotateGesturesEnabled;
    //放大缩小按钮
    private boolean zoomControlsEnabled;
    //显示指南针
    private boolean compassEnabled;
    //移动到我的位置
    private boolean myLocationButtonEnabled;

    public boolean isTiltGesturesEnabled() {
        return tiltGesturesEnabled;
    }

    public void setTiltGesturesEnabled(boolean tiltGesturesEnabled) {
        this.tiltGesturesEnabled = tiltGesturesEnabled;
    }

    public boolean isRotateGesturesEnabled() {
        return rotateGesturesEnabled;
    }

    public void setRotateGesturesEnabled(boolean rotateGesturesEnabled) {
        this.rotateGesturesEnabled = rotateGesturesEnabled;
    }

    public boolean isZoomControlsEnabled() {
        return zoomControlsEnabled;
    }

    public void setZoomControlsEnabled(boolean zoomControlsEnabled) {
        this.zoomControlsEnabled = zoomControlsEnabled;
    }


    public boolean isCompassEnabled() {
        return compassEnabled;
    }

    public void setCompassEnabled(boolean compassEnabled) {
        this.compassEnabled = compassEnabled;
    }

    public boolean isMyLocationButtonEnabled() {
        return myLocationButtonEnabled;
    }

    public void setMyLocationButtonEnabled(boolean myLocationButtonEnabled) {
        this.myLocationButtonEnabled = myLocationButtonEnabled;
    }
}
