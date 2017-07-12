package coffeeShopChallenge.shopservice;

/**
 * Created by Michael on 7/10/2017.
 */
public class NoCoffeeShopException extends Exception {

    int shopId;

    public NoCoffeeShopException(int id) {
        super("No coffee shop with id: " + id);
        this.shopId = id;
    }
}
