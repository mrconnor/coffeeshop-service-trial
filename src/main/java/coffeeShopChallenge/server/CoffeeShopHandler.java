package coffeeShopChallenge.server;

import coffeeShopChallenge.model.CoffeeShop;
import coffeeShopChallenge.model.GeoLocation;
import coffeeShopChallenge.shopservice.CoffeeShopService;
import coffeeShopChallenge.shopservice.NoCoffeeShopException;
import coffeeShopChallenge.util.GMapsGeocoding;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/** Define the endpoint handlers for the "coffeeshop" service.
 *
 * Created by Michael on 7/11/2017.
 */

@Path("/")
public class CoffeeShopHandler {
    @Context ServletContext context;

    public static String ServiceAttrName = "coffeeShopService";
    private CoffeeShopService getService() {
        return (CoffeeShopService)context.getAttribute(ServiceAttrName);
    }


    /** The CREATE endpoint.
     * Do a POST query to "/coffeeshop".
     * Query parameters "name", "address", "latitude" and "longitude" give the data for the new shop.
     * Returns the id of the newly registered shop.
     */
    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public CoffeeShopId postShop(
            @QueryParam("name") String name,
            @QueryParam("address") String address,
            @QueryParam("lat") Double lat,
            @QueryParam("lng") Double lng
    ) {
        if (name == null)
            throw new MissingParamException("name");
        if (lat == null)
            throw new MissingParamException("lat");
        if (lng == null)
            throw new MissingParamException("lng");

        CoffeeShop shop = getService().addShop(name, address, lat, lng);
        return new CoffeeShopId(shop.getId());
    }


    /** The READ endpoint.
     * Do a GET query to "/coffeeshop/{id}".
     * Returns the data for the shop, in JSON form, or an error message of no shop exists for that id.
     */
    @GET
    @Path("/{shopid}")
    @Produces(MediaType.APPLICATION_JSON)
    public CoffeeShop getShop(@PathParam("shopid") int shopid) throws NoCoffeeShopException {
        CoffeeShop shop = getService().get(shopid);
        return shop;
    }


    /** The UPDATE endpoint.
     * Do a PUT query to "/coffeeshop/{id}".
     * Any or all of query parameters "name", "address", "lat" and "lng" may be given.  If so, their
     * value will overwrite the value in the existing shop entry.
     * "lat" and "lng" must be specified together.  If either is specified, the other must be as well.
     * Returns the text "SUCCESS" or an error message.
     */
    @PUT
    @Path("/{shopid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String putShop(
            @PathParam("shopid") int shopid,
            @QueryParam("name") String name,
            @QueryParam("address") String address,
            @QueryParam("lat") Double lat,
            @QueryParam("lng") Double lng
    ) throws NoCoffeeShopException, IllegalArgumentException {

        GeoLocation geoloc = null;
        if (lat != null || lng != null) {
            if (lat == null || lng == null)
                throw new IllegalArgumentException("lat and lng query params must be specified together");
            geoloc = new GeoLocation(lat, lng);
        }

        getService().update(shopid, name, address, geoloc);
        return "SUCCESS";
    }


    /** The DELETE endpoint.
     * Do a DELETE query to "/coffeeshop/{id}".
     * Returns the text "DELETED" or an error message.
     */
    @DELETE
    @Path("/{shopid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteShop(@PathParam("shopid") int shopid) throws NoCoffeeShopException {
        getService().delete(shopid);
        return "DELETED";
    }


    /** The FIND NEAREST endpoint.
     * Do a GET query to "/coffeeshop/nearest".
     * The query parameters may be either "address", by itself, or "latitude" and "longitude" together.
     * The data for the nearest coffee shop is returned.
     * Note: There is no promise that it's close, just the closest :-)
     */
    @GET
    @Path("/nearest")
    @Produces(MediaType.APPLICATION_JSON)
    public CoffeeShop getNearestShop(
            @QueryParam("address") String address,
            @QueryParam("lat") Double lat,
            @QueryParam("lng") Double lng
    ) throws GMapsGeocoding.GeocodingException {

        GeoLocation geoloc = null;

        if (address != null) {
            geoloc= GMapsGeocoding.getLocForAddress(address);
        }
        else if (lat != null || lng != null){
            if (lat == null || lng == null)
                throw new IllegalArgumentException("lat and lng query params must be specified together");
            geoloc = new GeoLocation(lat, lng);
        }
        else
            throw new MissingParamException("address");

        CoffeeShop shop = getService().findNearest(geoloc);
        return shop;
    }


    /** Structure for returning just the id of a shop. */
    public static class CoffeeShopId {
        int id;
        CoffeeShopId(int id) { this.id = id; }
    }

    public static class MissingParamException extends IllegalArgumentException {
        String pname;
        MissingParamException(String pname) {
            super("Missing '" + pname + "' query parameter");
            this.pname = pname;
        }
    }
}
