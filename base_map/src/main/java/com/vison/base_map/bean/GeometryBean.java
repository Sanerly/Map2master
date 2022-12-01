package com.vison.base_map.bean;


import com.vison.base_map.LngLat;

import java.util.List;

/**
 * @author XiaoShu
 * @date 1/22/21.
 */
public class GeometryBean {
    private String type;
    private Object coordinates;
    private LngLat single; // 单个坐标数据
    private List<LngLat> multiple; // 多个坐标数据
    private List<List<LngLat>> mMultiple; // 二维list坐标数据

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Object coordinates) {
        this.coordinates = coordinates;
    }

    public LngLat getSingle() {
        return single;
    }

    public void setSingle(LngLat single) {
        this.single = single;
    }

    public List<LngLat> getMultiple() {
        return multiple;
    }

    public void setMultiple(List<LngLat> multiple) {
        this.multiple = multiple;
    }

    public List<List<LngLat>> getMMultiple() {
        return mMultiple;
    }

    public void setMMultiple(List<List<LngLat>> mMultiple) {
        this.mMultiple = mMultiple;
    }
}
