package coffeeShopChallenge.server;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/** The generic handler for exceptions.  Returns a JSON value containing the single element "error".
 *
 * Created by Michael on 7/11/2017.
 */

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

    public Response toResponse(Exception ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorMsg(ex.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    /** Structure for returning error messages. */
    public static class ErrorMsg {
        String error;
        ErrorMsg(String msg) { this.error = msg; }
    }
}
