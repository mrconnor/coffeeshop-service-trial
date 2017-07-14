# Kinsa Take-home Challenge -- Coffee Shop Service

## Build and Execute

#### Requirements:

* Java 8 SDK
* mvn

#### Build:
* Checkout this git repo to a folder \<repoDir\>.
* In <repoDir>, execute this at a command prompt:
~~~~
> mvn package
~~~~
This generates the JAR file:  \<repoDir\>/target/CoffeeShopService.jar

#### Execute:
Really, run like this:
~~~~
mvn jetty:run
~~~~



This jar contains all dependencies.  The service can be executed like this:
~~~~
> java -jar CoffeeShopService.jar
~~~~
This runs an HTTP server that listens to "localhost:8080".

At time of launch, the shops data is initialized to ...

## Service REST Queries

Summary:
* GET "/coffeeshop/\<id\>"
* PUT "/coffeeshop/\<id\>" ? ( name, address, lat, and/or lng )
* DELETE "/coffeeshop/\<id\>"
* POST "/coffeeshop" ?  ( name, address, lat, lng )
* GET "/coffeeshop/nearest" ? ( address or lat, lng )


If you know the integer ID of a specific shop, the shop's data may be
read, updated or deleted by querying the URI "/coffeeshop/<id>" with the appropriate HTTP method.

#### Read data for a shop
* URI: "/coffeeshop/<id>"
* Method: GET
* Response: JSON representation for shop data (see next section)

#### Update data for a shop
* URI: "/coffeeshop/<id>"
    * Query parameters: name, address, lat, lng
* Method: PUT
* Response: the plain text value "SUCCESS"

The query parameters "name", "address", "lat", "lng" are all optional.
If a value is not given, it is not changed.  The only restriction is that "lat"
and "lng" must both be specified if either is specified.

#### Delete a shop
* URI: "/coffeeshop/<id>"
* Method: DELETE
* Response: the plain text value "DELETED"

#### Create a new shop
* URI: "/coffeeshop"
    * Query parameters: name, address, lat, lng
* Method: POST
* Response: JSON representation for new shop id (see next section)

All query parameters - "name", "address", "lat", "lng" - are required.
The ID of the newly created shop is returned.

#### Find nearest shop
* URI: "/coffeeshop/nearest"
    * Query parameter: address
    * Query parameters: lat, lng
* Method: GET
* Response: JSON representation for shop data (see next section)

The query must contain either the "address" parameter or the two "lat" and "lng" parameters.
If "address" is given, the lat and lng of that address is determined using Google Maps.

The data for the nearest shop is returned.


### Result Formats

#### Shop Data
The data for an individual shop is returned as a JSON object having these elements.

| Name    | Type   | Description
|---------|--------|--------------
| id      | number | unique shop ID
| name    | text   | name of shop
| address | text   | address of shop
| geoloc  | object | store's geolocation.  Sub-object elements are "lat" and "lng"


Example:
~~~~
{
    "id": 1,
    "name": "Equator Coffees & Teas",
    "address": "986 Market St",
    "geoloc": {
        "lat": 37.782394430549445,
        "lng": -122.40997343121123
    }
}
~~~~

#### New Shop ID
| Name    | Type   | Description
|---------|--------|--------------
| id      | number | ID of newly added shop

~~~~
{
    "id": 57
}
~~~~


#### Error
| Name    | Type   | Description
|---------|---------|--------------
| error   | text    | Error message

~~~~
{
    "error": "No coffee shop with id: 300"
}
~~~~
