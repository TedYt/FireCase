package com.hytera.fcls.bean;

/**
 * Created by Tim on 17/3/3.
 */

public class GPSBean extends BaseBean {

    /**
     * 维度
     */
    private double latitude;
    /**
     * 经度
     */
    private double longitude;

    /**
     * 上报GPS时间
     */
    private long gpsDateTime;

    public long getGpsDateTime() {
        return gpsDateTime;
    }

    public void setGpsDateTime(long gpsDateTime) {
        this.gpsDateTime = gpsDateTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
