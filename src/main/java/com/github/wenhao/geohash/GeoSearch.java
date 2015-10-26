package com.github.wenhao.geohash;

import static java.math.BigDecimal.ROUND_HALF_UP;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.github.wenhao.geohash.GeoHash.MAX_PRECISION;

public class GeoSearch {
    private static final BigDecimal EARTH_RADIUS = new BigDecimal(6372797.560856);
    private static Map<BigDecimal, Integer> PRECISION_MAP;

    static {
        PRECISION_MAP = new HashMap<>();
        for (int angle = 1; angle <= MAX_PRECISION / 2; angle++) {
            BigDecimal bigDecimal = new BigDecimal(2)
                    .multiply(new BigDecimal(Math.PI))
                    .multiply(EARTH_RADIUS)
                    .divide(new BigDecimal(2).pow(angle), ROUND_HALF_UP);
            PRECISION_MAP.put(bigDecimal, 2 * angle);
        }
    }

    public static long[] range(double latitude, double longitude, double startRage, double endRange) {
        GeoHash geoHash = GeoHash.fromCoordinate(latitude, longitude);
        long longValue = geoHash.toLong();
        return new long[]{getStartRange(longValue, startRage), getEndRange(longValue, endRange)};
    }

    private static long getStartRange(long longValue, double startRage) {
        int length = MAX_PRECISION;
        Optional<BigDecimal> smallerKey = PRECISION_MAP.keySet()
                .stream()
                .sorted(Collections.reverseOrder())
                .filter(bigDecimal -> bigDecimal.compareTo(BigDecimal.valueOf(startRage)) == -1)
                .findFirst();
        if (smallerKey.isPresent()) {
            length = PRECISION_MAP.get(smallerKey.get());
        }
        long desiredMinPrecision = longValue >>> (MAX_PRECISION - length);
        desiredMinPrecision <<= (MAX_PRECISION - length);
        return desiredMinPrecision;
    }

    private static long getEndRange(long longValue, double endRange) {
        int length = 0;
        Optional<BigDecimal> biggerKey = PRECISION_MAP.keySet()
                .stream()
                .sorted()
                .filter(bigDecimal -> bigDecimal.compareTo(BigDecimal.valueOf(endRange)) == 1)
                .findFirst();
        if (biggerKey.isPresent()) {
            length = PRECISION_MAP.get(biggerKey.get());
        }
        long desiredMaxPrecision = (longValue >>> (MAX_PRECISION - length)) + 1;
        desiredMaxPrecision <<= (MAX_PRECISION - length);
        return desiredMaxPrecision;
    }
}
