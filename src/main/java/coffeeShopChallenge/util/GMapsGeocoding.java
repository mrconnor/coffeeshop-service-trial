package coffeeShopChallenge.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import coffeeShopChallenge.model.GeoLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/** Utility for querying Google Maps for geocoding info.
 *
 * Created by Michael on 7/11/2017.
 */
public class GMapsGeocoding {
    static Logger logger = LoggerFactory.getLogger(GMapsGeocoding.class);

    // MConnor's app key
    static String apiKey = "AIzaSyDQ8uhYhdCl7_F1DgujNLNbgI3q_tQdh68";


    // ==== Classes correspond to JSON result from Google Maps geocode query ====
    static class GMapResults {
        List<GMapResult> results;
    }

    static class GMapResult {
        String formatted_address;
        GMapGeometry geometry;
    }

    static class GMapGeometry {
        GMapGeoloc location;
    }

    static class GMapGeoloc {
        double lat;
        double lng;
    }
    // ==== <end geocode query result structures> ====


    /** Use the Google Maps geocoding API to get the geo-location for a given address string.
     */
    public static GeoLocation getLocForAddress(String address) throws GeocodingException {
        try {
            String query = "https://maps.googleapis.com/maps/api/geocode/json?address="
                    + URLEncoder.encode(address, "utf-8")
                    + "&key=" + apiKey;

            URL url = new URL(query);
            InputStream instr = url.openStream();
            InputStreamReader reader = new InputStreamReader(instr);

            try {
                Gson gson = new GsonBuilder().create();
                GMapResults results = gson.fromJson(reader, GMapResults.class);
                GMapGeoloc gloc = results.results.get(0).geometry.location;

                logger.debug("\"" + address + "\" -> \""
                        + results.results.get(0).formatted_address + "\" @ ["
                        + gloc.lat + "," + gloc.lng + "]");

                return new GeoLocation(gloc.lat, gloc.lng);

            } finally {
                reader.close();
                instr.close();
            }

        } catch (Exception ex) {
            throw new GeocodingException(ex);
        }
    }

    public static class GeocodingException extends Exception {
        GeocodingException(Exception cause) {
            super(cause);
        }
    }

}
