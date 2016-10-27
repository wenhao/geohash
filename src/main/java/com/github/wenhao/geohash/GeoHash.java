package com.github.wenhao.geohash;

import com.github.wenhao.geohash.domain.Coordinate;
import com.github.wenhao.geohash.math.GeoDistance;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class GeoHash implements Serializable {

    public static final int MAX_PRECISION = 52;
    private static final long FIRST_BIT_FLAGGED = 0x8000000000000L;
    private long bits = 0;
    private byte significantBits = 0;
    private Coordinate coordinate;

    private GeoHash() {
    }

    public static GeoHash fromCoordinate(double latitude, double longitude) {
        return fromCoordinate(latitude, longitude, MAX_PRECISION);
    }

    public static GeoHash fromCoordinate(double latitude, double longitude, int precision) {
        GeoHash geoHash = new GeoHash();
        geoHash.coordinate = new Coordinate(latitude, longitude);
        boolean isEvenBit = true;
        double[] latitudeRange = {-90, 90};
        double[] longitudeRange = {-180, 180};

        while (geoHash.significantBits < precision) {
            if (isEvenBit) {
                divideRangeEncode(geoHash, longitude, longitudeRange);
            } else {
                divideRangeEncode(geoHash, latitude, latitudeRange);
            }
            isEvenBit = !isEvenBit;
        }
        geoHash.bits <<= (MAX_PRECISION - precision);
        return geoHash;
    }

    public static GeoHash fromLong(long longValue) {
        return fromLong(longValue, MAX_PRECISION);
    }

    public static GeoHash fromLong(long longValue, int significantBits) {
        double[] latitudeRange = {-90.0, 90.0};
        double[] longitudeRange = {-180.0, 180.0};
        boolean isEvenBit = true;
        GeoHash geoHash = new GeoHash();
        String binaryString = Long.toBinaryString(longValue);
        while (binaryString.length() < MAX_PRECISION) {
            binaryString = "0" + binaryString;
        }

        for (int j = 0; j < significantBits; j++) {
            if (isEvenBit) {
                divideRangeDecode(geoHash, longitudeRange, binaryString.charAt(j) != '0');
            } else {
                divideRangeDecode(geoHash, latitudeRange, binaryString.charAt(j) != '0');
            }
            isEvenBit = !isEvenBit;
        }
        double latitude = (latitudeRange[0] + latitudeRange[1]) / 2;
        double longitude = (longitudeRange[0] + longitudeRange[1]) / 2;
        geoHash.coordinate = new Coordinate(latitude, longitude);
        geoHash.bits <<= (MAX_PRECISION - geoHash.significantBits);
        return geoHash;
    }

    public double distance(double latitude, double longitude) {
        return GeoDistance.distance(this.coordinate.getLatitude(), this.coordinate.getLongitude(), latitude, longitude);
    }

    public List<GeoHash> getAdjacent() {
        GeoHash northern = getNorthernNeighbour();
        GeoHash eastern = getEasternNeighbour();
        GeoHash southern = getSouthernNeighbour();
        GeoHash western = getWesternNeighbour();
        return Arrays.asList(northern, northern.getEasternNeighbour(), eastern, southern.getEasternNeighbour(),
            southern, southern.getWesternNeighbour(), western, northern.getWesternNeighbour(), this);
    }

    private GeoHash getNorthernNeighbour() {
        long[] latitudeBits = getRightAlignedLatitudeBits();
        long[] longitudeBits = getRightAlignedLongitudeBits();
        latitudeBits[0] += 1;
        latitudeBits[0] = maskLastNBits(latitudeBits[0], latitudeBits[1]);
        return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    private GeoHash getSouthernNeighbour() {
        long[] latitudeBits = getRightAlignedLatitudeBits();
        long[] longitudeBits = getRightAlignedLongitudeBits();
        latitudeBits[0] -= 1;
        latitudeBits[0] = maskLastNBits(latitudeBits[0], latitudeBits[1]);
        return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    private GeoHash getEasternNeighbour() {
        long[] latitudeBits = getRightAlignedLatitudeBits();
        long[] longitudeBits = getRightAlignedLongitudeBits();
        longitudeBits[0] += 1;
        longitudeBits[0] = maskLastNBits(longitudeBits[0], longitudeBits[1]);
        return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    private GeoHash getWesternNeighbour() {
        long[] latitudeBits = getRightAlignedLatitudeBits();
        long[] longitudeBits = getRightAlignedLongitudeBits();
        longitudeBits[0] -= 1;
        longitudeBits[0] = maskLastNBits(longitudeBits[0], longitudeBits[1]);
        return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    private GeoHash recombineLatLonBitsToHash(long[] latBits, long[] lonBits) {
        GeoHash geoHash = new GeoHash();
        boolean isEvenBit = false;
        latBits[0] <<= (MAX_PRECISION - latBits[1]);
        lonBits[0] <<= (MAX_PRECISION - lonBits[1]);
        double[] latitudeRange = {-90.0, 90.0};
        double[] longitudeRange = {-180.0, 180.0};

        for (int i = 0; i < latBits[1] + lonBits[1]; i++) {
            if (isEvenBit) {
                divideRangeDecode(geoHash, latitudeRange, (latBits[0] & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED);
                latBits[0] <<= 1;
            } else {
                divideRangeDecode(geoHash, longitudeRange, (lonBits[0] & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED);
                lonBits[0] <<= 1;
            }
            isEvenBit = !isEvenBit;
        }
        geoHash.bits <<= (MAX_PRECISION - geoHash.significantBits);
        geoHash.coordinate = getCenterCoordinate(latitudeRange, longitudeRange);
        return geoHash;
    }

    private long[] getRightAlignedLatitudeBits() {
        long copyOfBits = bits << 1;
        long value = extractEverySecondBit(copyOfBits, getNumberOfLatLonBits()[0]);
        return new long[]{value, getNumberOfLatLonBits()[0]};
    }

    private long[] getRightAlignedLongitudeBits() {
        long copyOfBits = bits;
        long value = extractEverySecondBit(copyOfBits, getNumberOfLatLonBits()[1]);
        return new long[]{value, getNumberOfLatLonBits()[1]};
    }

    private long extractEverySecondBit(long copyOfBits, int numberOfBits) {
        long value = 0;
        for (int i = 0; i < numberOfBits; i++) {
            if ((copyOfBits & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED) {
                value |= 0x1;
            }
            value <<= 1;
            copyOfBits <<= 2;
        }
        value >>>= 1;
        return value;
    }

    private Coordinate getCenterCoordinate(double[] latitudeRange, double[] longitudeRange) {
        double minLon = Math.min(longitudeRange[0], longitudeRange[1]);
        double maxLon = Math.max(longitudeRange[0], longitudeRange[1]);
        double minLat = Math.min(latitudeRange[0], latitudeRange[1]);
        double maxLat = Math.max(latitudeRange[0], latitudeRange[1]);
        double centerLatitude = (minLat + maxLat) / 2;
        double centerLongitude = (minLon + maxLon) / 2;
        return new Coordinate(centerLatitude, centerLongitude);
    }

    private int[] getNumberOfLatLonBits() {
        if (significantBits % 2 == 0) {
            return new int[]{significantBits / 2, significantBits / 2};
        } else {
            return new int[]{significantBits / 2, significantBits / 2 + 1};
        }
    }

    private long maskLastNBits(long value, long number) {
        long mask = 0xffffffffffffffffL;
        mask >>>= (MAX_PRECISION - number);
        return value & mask;
    }

    private static void divideRangeEncode(GeoHash geoHash, double value, double[] range) {
        double mid = (range[0] + range[1]) / 2;
        if (value >= mid) {
            geoHash.addOnBitToEnd();
            range[0] = mid;
        } else {
            geoHash.addOffBitToEnd();
            range[1] = mid;
        }
    }

    private static void divideRangeDecode(GeoHash geoHash, double[] range, boolean isOnBit) {
        double mid = (range[0] + range[1]) / 2;
        if (isOnBit) {
            geoHash.addOnBitToEnd();
            range[0] = mid;
        } else {
            geoHash.addOffBitToEnd();
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
        return Long.toBinaryString(bits);
    }
}
