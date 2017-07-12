package coffeeShopChallenge.util;

import coffeeShopChallenge.model.GeoLocation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Michael on 7/12/2017.
 */
public class GMapsGeocodingTest {
    @Test
    public void testLocForAddr() throws Exception {

        GeoLocation geoloc = GMapsGeocoding.getLocForAddress("100 Market St., SF, CA");

        assertEquals(37.7942635, geoloc.getLatitude(), 0.0001);
        assertEquals(-122.3955861, geoloc.getLongitude(), 0.0001);

    }
}
