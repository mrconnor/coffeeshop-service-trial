package coffeeShopChallenge.model;

import coffeeShopChallenge.util.Geohash;

/** A specific point in geographic space as measured by latitude and longitude.
 *
 * Created by Michael on 7/11/2017.
 */
public class GeoLocation {
    static int geoHashPrecision = 16;

    private double lat;
    private double lng;
    private transient String geohash;

    public GeoLocation(double latitude, double longitude) {
        this.lat = latitude;
        this.lng = longitude;
        this.geohash = Geohash.encodeGeohash(lat, lng, geoHashPrecision);
    }

    @Override
    public String toString() {
        return "[" + lat + "," + lng + "]";
    }

    public double getLatitude() { return lat; }

    public double getLongitude() { return lng; }

    public String getGeohash() { return geohash; }


    /** A measure of the direct distance between two points. */
    public double distinceFrom(double latitude, double longitude) {
        return Math.sqrt( Math.pow(latitude - this.lat, 2) + Math.pow(longitude - this.lng, 2)  );
    }

    public double distinceFrom(GeoLocation other) {
        return distinceFrom(other.getLatitude(), other.getLongitude());
    }
}
