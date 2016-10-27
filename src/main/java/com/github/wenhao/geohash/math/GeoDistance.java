package com.github.wenhao.geohash.math;

import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

public class GeoDistance {

    private static final double EARTH_RADIUS = 6372797.560856;

    public static double distance(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        double diffLongitudes = toRadians(abs(endLongitude - startLongitude));
        double diffLatitudes = toRadians(abs(endLatitude - startLatitude));
        double slat = toRadians(startLatitude);
        double flat = toRadians(endLatitude);
        double factor = sin(diffLatitudes / 2) * sin(diffLatitudes / 2) + cos(slat) * cos(flat) * sin(diffLongitudes / 2) * sin(diffLongitudes / 2);
        double angularDistance = 2 * atan2(sqrt(factor), sqrt(1 - factor));
        return EARTH_RADIUS * angularDistance;
    }
}
