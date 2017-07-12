package coffeeShopChallenge.shopservice;

import coffeeShopChallenge.model.CoffeeShop;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Michael on 7/10/2017.
 */
public class CsvImporterTest {
    @Test
    public void testImport() throws Exception {
        CsvImporter importer = new CsvImporter();
        InputStream instr = getClass().getResourceAsStream("/locations.csv");
        Reader reader = new InputStreamReader(instr, "utf-8");

        // Do the import
        ArrayList<CoffeeShop> shops = new ArrayList(importer.load(reader));

        assertEquals(48, shops.size());

        // Spotcheck the first and last.  For this test we know that the importer returned
        // shops in their order in the csv file.
        CoffeeShop firstShop = shops.get(0);
        assertEquals(1, firstShop.getId());
        assertEquals("Equator Coffees & Teas", firstShop.getName());
        assertEquals("986 Market St", firstShop.getAddress());
        assertEquals(37.782394430549445, firstShop.getGeoloc().getLatitude(), 0.0001);
        assertEquals(-122.40997343121123, firstShop.getGeoloc().getLongitude(), 0.0001);

        CoffeeShop lastShop = shops.get(shops.size()-1);
        assertEquals(56, lastShop.getId());
        assertEquals("Chapel Hill Coffee Co.", lastShop.getName());
        assertEquals("670 Commercial St", lastShop.getAddress());
        assertEquals(37.794096040757196, lastShop.getGeoloc().getLatitude(), 0.0001);
        assertEquals(-122.40423200906335, lastShop.getGeoloc().getLongitude(), 0.0001);

        // Check a shop that has a non-ASCII character in the name
        CoffeeShop shop15 = shops.get(14);
        assertEquals(15, shop15.getId());
        assertEquals("Réveille Coffee Co.", shop15.getName());

        // Check a shop that has extra punctuation in the name
        CoffeeShop shop27 = shops.get(26);
        assertEquals(27, shop27.getId());
        assertEquals("Bob's Donut & Pastry Shop", shop27.getName());

    }
}
