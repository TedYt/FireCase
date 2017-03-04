package com.hytera.fcls.bean;

/**
 * Created by Tim on 17/3/3.
 */

public class GPSBean extends BaseBean {

    /**
     * 维度
     */
    private double lat;
    /**
     * 经度
     */
    private double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
