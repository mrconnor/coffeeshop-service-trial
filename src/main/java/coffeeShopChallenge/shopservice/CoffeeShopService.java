package coffeeShopChallenge.shopservice;

import coffeeShopChallenge.model.CoffeeShop;
import coffeeShopChallenge.model.GeoLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/** The Coffee Shop Service.  This maintains the set of known shops and provides some operations on that set.
 *
 * This service is thread-safe, albeit in the least sophisticated way by simply synchronizing all the
 * public methods.
 *
 * Created by Michael on 7/10/2017.
 */
public class CoffeeShopService {
    static Logger logger = LoggerFactory.getLogger(CoffeeShopService.class);

    // For allocating IDs for new shops
    private int topShopId = 0;

    // Map of shops indexed by ID
    private HashMap<Integer, CoffeeShop> shopsById = new HashMap();

    // Map of shops indexed by geohash
    private TreeMap<String, CoffeeShop> shopsByGeohash = new TreeMap();


    /** Construction of a coffee shop service requires a reader containing the initial shop data.
     */
    public CoffeeShopService(Reader cvsReader) throws IOException {
        loadCvs(cvsReader);
    }

    /** Read the reader, assumed to contain CSV data.  Add the defined shops to the service.
     */
    public void loadCvs(Reader cvsReader) throws IOException {
        CsvImporter importer = new CsvImporter();
        Collection<CoffeeShop> shops = importer.load(cvsReader);

        for (CoffeeShop shop : shops) {
            int shopId = shop.getId();
            shopsById.put(shopId, shop);
            topShopId = Math.max(topShopId, shopId);

            shopsByGeohash.put(shop.getGeoloc().getGeohash(), shop);
        }
    }

    /** Add a new shop.
     *
     * @param name
     * @param address
     * @param latitude
     * @param longitude
     * @return the newly created shop
     */
    public synchronized CoffeeShop addShop(String name, String address, double latitude, double longitude) {

        int shopId = ++topShopId;
        CoffeeShop shop = new CoffeeShop(shopId, name, address, latitude, longitude);

        shopsById.put(shopId, shop);

        return shop;
    }

    /** Get a shop by id.
     *
     * @param id  A shop ID
     * @return  The associated shop
     * @throws NoCoffeeShopException if there is no shop for that id
     */
    public synchronized CoffeeShop get(int id) throws NoCoffeeShopException {
        CoffeeShop shop = shopsById.get(id);
        if (shop == null)
            throw new NoCoffeeShopException(id);

        return shop;
    }

    /** Update an existing shop.
     *
     * @param id  ID of shop to update
     * @param name  New value of name.  If null, name is not modified.
     * @param address  New value of address.  If null, address is not modified.
     * @param geoloc  New value of geo location.  If null, location is not modified.
     * @return the updated shop
     * @throws NoCoffeeShopException if there is no shop for that id
     */
    public synchronized CoffeeShop update(int id, String name, String address, GeoLocation geoloc) throws NoCoffeeShopException {
        CoffeeShop shop = shopsById.get(id);
        if (shop == null)
            throw new NoCoffeeShopException(id);

        if (name != null)
            shop.setName(name);

        if (address != null)
            shop.setAddress(address);

        if (geoloc != null) {
            shopsByGeohash.remove(shop.getGeoloc().getGeohash());
            shop.setGeoloc(geoloc);
            shopsByGeohash.put(shop.getGeoloc().getGeohash(), shop);
        }

        return shop;
    }

    /** Delete an existing shop.
     *
     * @param id  ID of shop to delete
     * @throws NoCoffeeShopException if there is no shop for that id
     */
    public synchronized void delete(int id) throws NoCoffeeShopException {
        if (!shopsById.containsKey(id))
            throw new NoCoffeeShopException(id);

        shopsById.remove(id);
    }

    /** Find shop that is nearest to a given location.
     *
     * @param geoloc  A location
     * @return  the nearest shop
     */
    public synchronized CoffeeShop findNearest(GeoLocation geoloc) {

        String geoHash = geoloc.getGeohash();  // the geohash of the desired location

        // Get the entries closest to that geohash, looking both higher (ceiling) and lower (floor)
        Map.Entry<String, CoffeeShop> floor = shopsByGeohash.floorEntry(geoHash);
        Map.Entry<String, CoffeeShop> ceiling = shopsByGeohash.ceilingEntry(geoHash);

        // If there is no floor, the ceiling must be the best match
        if (floor == null)
            return (ceiling == null) ? null : ceiling.getValue();

        // If there is no ceiling, the floor must be the best match
        if (ceiling == null)
            return floor.getValue();

        // We have a shop that exactly matches the desired location if both floor and ceiling
        // returned the same value
        if (floor.getValue().equals(ceiling.getValue())) {
            logger.debug("Exact match for " + geoloc + " is: " + floor.getValue());
            return floor.getValue();
        }

        // We have to break a tie, so it's time to get a little picky.
        // Directly calculate the distance from the desired location to both shops.
        // Return the shop with the shortest distance.
        double floorDistance = floor.getValue().getGeoloc().distinceFrom(geoloc);
        double ceilingDistance = ceiling.getValue().getGeoloc().distinceFrom(geoloc);

        CoffeeShop closestShop = (floorDistance < ceilingDistance) ? floor.getValue() : ceiling.getValue();
        logger.debug("Closest to " + geoloc + " is: " + closestShop + " @ " + closestShop.getGeoloc());

        return closestShop;
    }
}
