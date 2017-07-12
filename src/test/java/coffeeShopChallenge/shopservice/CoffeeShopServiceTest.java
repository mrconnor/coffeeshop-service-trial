package coffeeShopChallenge.shopservice;

import coffeeShopChallenge.model.CoffeeShop;
import coffeeShopChallenge.model.GeoLocation;
import coffeeShopChallenge.util.GMapsGeocoding;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.Assert.assertEquals;

/**
 * Created by Michael on 7/12/2017.
 */
public class CoffeeShopServiceTest {
    static Logger logger = LoggerFactory.getLogger(CoffeeShopServiceTest.class);

    private CoffeeShopService makeService() throws IOException {
        InputStream instr = getClass().getResourceAsStream("/locations.csv");
        Reader reader = new InputStreamReader(instr, "utf-8");
        CoffeeShopService coffeeShopService = new CoffeeShopService(reader);
        return coffeeShopService;
    }

    @Test
    public void testNearestShop() throws Exception {
        CoffeeShopService service = makeService();

        // coordinates should find an exact match
        CoffeeShop testShop1 = service.findNearest(new GeoLocation(37.760203782770596, -122.43491565857126));
        assertEquals(34, testShop1.getId());

        // coordinates are near shop #18 but not exact
        // Shop 21's exact coords: 37.74168720173771, -122.4225780158454
        CoffeeShop testShop2 = service.findNearest(new GeoLocation(37.742, -122.421));
        assertEquals(21, testShop2.getId());
    }

    @Test
    public void testNearestShopWithGeocoding() throws Exception {
        CoffeeShopService service = makeService();

        // put it together with a call to google maps for an addr around the corner
        GeoLocation geoloc = GMapsGeocoding.getLocForAddress("1310 Grant Ave, San Francisco, CA");
        CoffeeShop testShop3 = service.findNearest(geoloc);
        assertEquals(18, testShop3.getId());
    }

    @Test
    public void testUpdateShop() throws Exception {
        CoffeeShopService service = makeService();

        // update name only
        CoffeeShop shop = service.update(31, "Jane Doe", null, null);
        assertEquals(31, shop.getId());
        assertEquals("Jane Doe", shop.getName());
        assertEquals("925 Larkin St", shop.getAddress());
        assertEquals(37.786475, shop.getGeoloc().getLatitude(), 0.0001);
        assertEquals(-122.418342, shop.getGeoloc().getLongitude(), 0.0001);

    }

    @Test
    public void testPostShop() throws Exception {
        CoffeeShopService service = makeService();

        CoffeeShop shop = service.addShop("Our New Test Shoppe", "1 Main St.", 37.1234, -121.9876);
        // The new shop's ID will be one more than the current max ID, which is 56 in the test list
        assertEquals(57, shop.getId());
        assertEquals("Our New Test Shoppe", shop.getName());
        assertEquals("1 Main St.", shop.getAddress());
        assertEquals(37.1234, shop.getGeoloc().getLatitude(), 0.0001);
        assertEquals(-121.9876, shop.getGeoloc().getLongitude(), 0.0001);
    }

}
