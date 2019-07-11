package com.xq.live.common;

/**
 * 位置
 */
public class LocationUtils {

    /**
     * 定义赤道的半径（千米）
     */
    public static double EARTH_RADIUS = 6378.138;

    /**
     * 根据两个点的经纬度计算两个点之间的距离（单位：米）
     * @param locationX1 经度
     * @param locationY1 纬度
     * @param locationX2
     * @param locationY2
     * @return
     */
    public static double getDistance(double locationX1, double locationY1, double locationX2, double locationY2) {
        double ly1 = Math.toRadians(locationY1);
        double ly2 = Math.toRadians(locationY2);
        double a = ly1 - ly2;
        double b = Math.toRadians(locationX1) - Math.toRadians(locationX2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(ly1) * Math.cos(ly2) * Math.pow(Math.sin(b / 2), 2)));
        double distance = s * EARTH_RADIUS;
        return Math.round(distance * 1000);
    }

}
