package com.github.wenhao.geohash;

import com.github.wenhao.geohash.domain.Coordinate;

public class GeoHash {

    private static final int MAX_PRECISION = 52;
    private long bits = 0;
    private byte significantBits = 0;
    private Coordinate coordinate;

    public GeoHash(double latitude, double longitude) {
        this(latitude, longitude, MAX_PRECISION);
    }

    public GeoHash(double latitude, double longitude, int precision) {
        this.coordinate = new Coordinate(latitude, longitude);
        boolean isEvenBit = true;
        double[] latitudeRange = {-90, 90};
        double[] longitudeRange = {-180, 180};

        while (significantBits < precision) {
            if (isEvenBit) {
                divideRangeEncode(longitude, longitudeRange);
            } else {
                divideRangeEncode(latitude, latitudeRange);
            }
            isEvenBit = !isEvenBit;
        }
        bits <<= (MAX_PRECISION - precision);
    }

    public GeoHash(long longValue) {
        this(longValue, MAX_PRECISION);
    }

    public GeoHash(long longValue, int precision) {
        double[] latitudeRange = {-90.0, 90.0};
        double[] longitudeRange = {-180.0, 180.0};

        boolean isEvenBit = true;

        String binaryString = Long.toBinaryString(longValue);
        while (binaryString.length() < MAX_PRECISION) {
            binaryString = "0" + binaryString;
        }
        for (int j = 0; j < precision; j++) {
            if (isEvenBit) {
                divideRangeDecode(longitudeRange, binaryString.charAt(j) != '0');
            } else {
                divideRangeDecode(latitudeRange, binaryString.charAt(j) != '0');
            }
            isEvenBit = !isEvenBit;
        }

        double latitude = (latitudeRange[0] + latitudeRange[1]) / 2;
        double longitude = (longitudeRange[0] + longitudeRange[1]) / 2;
        this.coordinate = new Coordinate(latitude, longitude);
        bits <<= (MAX_PRECISION - precision);
    }

    private void divideRangeEncode(double value, double[] range) {
        double mid = (range[0] + range[1]) / 2;
        if (value >= mid) {
            addOnBitToEnd();
            range[0] = mid;
        } else {
            addOffBitToEnd();
            range[1] = mid;
        }
    }

    private void divideRangeDecode(double[] range, boolean b) {
        double mid = (range[0] + range[1]) / 2;
        if (b) {
            addOnBitToEnd();
            range[0] = mid;
        } else {
            addOffBitToEnd();
            range[1] = mid;
        }
    }

    private void addOnBitToEnd() {
        significantBits++;
        bits <<= 1;
        bits = bits | 0x1;
    }

    private void addOffBitToEnd() {
        significantBits++;
        bits <<= 1;
    }

    public long toLong() {
        return bits;
    }

    public Coordinate coordinate() {
        return coordinate;
    }

    @Override
    public String toString() {
        if (significantBits % 5 == 0) {
            return String.format("bits: %s", Long.toBinaryString(bits));
        } else {
            return String.format("bits: %s", Long.toBinaryString(bits));
        }
    }
}
