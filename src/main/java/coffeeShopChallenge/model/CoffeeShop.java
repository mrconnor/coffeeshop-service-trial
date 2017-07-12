package coffeeShopChallenge.model;

/** A coffee shop.
 *
 * Created by Michael on 7/10/2017.
 */
public class CoffeeShop {

    private int id;
    private String name;
    private String address;
    private GeoLocation geoloc;


    public CoffeeShop(int id, String name, String address, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.geoloc = new GeoLocation(latitude, longitude);
    }

    @Override
    public String toString() {
        return "[" + id + "] \"" + name + "\" @ " + address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public GeoLocation getGeoloc() {
        return geoloc;
    }

    public void setGeoloc(GeoLocation geoloc) { this.geoloc = geoloc; }

}
