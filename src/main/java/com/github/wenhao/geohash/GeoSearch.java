package com.github.wenhao.geohash;

import com.github.wenhao.geohash.domain.GeoRange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.wenhao.geohash.GeoHash.MAX_PRECISION;
import static java.math.BigDecimal.ROUND_HALF_UP;

public class GeoSearch {
    private static final BigDecimal EARTH_RADIUS = new BigDecimal(6372797.560856);
    private static Map<BigDecimal, Integer> PRECISION_MAP;

    static {
        PRECISION_MAP = new HashMap<BigDecimal, Integer>();
        for (int angle = 1; angle <= MAX_PRECISION / 2; angle++) {
            BigDecimal bigDecimal = new BigDecimal(2)
                    .multiply(new BigDecimal(Math.PI))
                    .multiply(EARTH_RADIUS)
                    .divide(new BigDecimal(2).pow(angle), ROUND_HALF_UP);
            PRECISION_MAP.put(bigDecimal, 2 * angle);
        }
    }

    public static List<GeoRange> range(double latitude, double longitude, double range) {
        int desiredLength = getDesiredLength(range);
        List<GeoHash> nineAroundCoordinates = getNineAroundCoordinate(latitude, longitude, desiredLength);
        List<GeoRange> geoRanges = new ArrayList<GeoRange>();
        for (GeoHash geoHash : nineAroundCoordinates) {
            long longValue = geoHash.toLong();
            long min = longValue << (MAX_PRECISION - desiredLength);
            long max = (longValue + 1) << (MAX_PRECISION - desiredLength);
            geoRanges.add(new GeoRange(min, max));
        }
        return geoRanges;
    }

    private static List<GeoHash> getNineAroundCoordinate(double latitude, double longitude, int desiredLength) {
        long longValue = GeoHash.fromCoordinate(latitude, longitude).toLong();
        long centralPoint = longValue >>> (MAX_PRECISION - desiredLength);
        return GeoHash.fromLong(centralPoint).getAdjacent();
    }

    private static int getDesiredLength(double range) {
        int desiredLength = 0;
        BigDecimal rangeKey = null;
        List<BigDecimal> ranges = new ArrayList<BigDecimal>(PRECISION_MAP.keySet());
        ranges.sort(null);
        for (BigDecimal bigDecimal : ranges) {
            if (bigDecimal.compareTo(BigDecimal.valueOf(range)) == 1) {
                rangeKey = bigDecimal;
                break;
            }
        }
        if (rangeKey != null) {
            desiredLength = PRECISION_MAP.get(rangeKey);
        }
        return desiredLength;
    }
}
