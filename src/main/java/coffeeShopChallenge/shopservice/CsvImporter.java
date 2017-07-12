package coffeeShopChallenge.shopservice;

import coffeeShopChallenge.model.CoffeeShop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

/** A not very fancy, just good enough importer for the "locations.csv" file.
 * This doesn't handle any kind of variants, like quote chars or header line.
 *
 * Created by Michael on 7/10/2017.
 */
public class CsvImporter {

    public Collection<CoffeeShop> load(Reader reader) throws IOException {

        ArrayList<CoffeeShop> shops = new ArrayList();

        BufferedReader breader = new BufferedReader(reader);

        while (true) {
            String line = breader.readLine();
            if (line == null)
                break;

            String[] parts = line.split(",");

            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            String address = parts[2].trim();
            double latitude = Double.parseDouble(parts[3].trim());
            double longitude = Double.parseDouble(parts[4].trim());

            CoffeeShop shop = new CoffeeShop(id, name, address, latitude, longitude);
            shops.add(shop);
        }

        return shops;
    }
}
