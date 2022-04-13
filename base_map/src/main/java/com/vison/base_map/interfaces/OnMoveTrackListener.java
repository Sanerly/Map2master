package com.vison.base_map.interfaces;

/**
 * @Author: Sanerly
 * @CreateDate: 2022/4/13 11:47
 * @Description: 移动轨迹绘制监听
 */
public interface OnMoveTrackListener {
    /**
     * 绘制状态
     * @param isState true开始，false 结束
     */
    void onDrawState(boolean isState);

    /**
     * 正在绘制的点
     * @param position 位置
     * @param lon 经度
     * @param lat 纬度
     */
    void onDrawPosition(int position,double lon,double lat);
}
