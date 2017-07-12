package coffeeShopChallenge.server;

import com.google.gson.Gson;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by Michael on 7/11/2017.
 */
@Provider @Produces("application/json")
public class JsonWriter implements MessageBodyWriter<Object> {
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    public long getSize(Object coffeeShop, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return 0;
    }

    public void writeTo(
            Object message,
            Class<?> aClass, Type type,
            Annotation[] annotations,
            MediaType mediaType,
            MultivaluedMap<String, Object> multivaluedMap,
            OutputStream outputStream
    ) throws IOException, WebApplicationException {

        Gson gson = new Gson();

        OutputStreamWriter ow = new OutputStreamWriter(outputStream);
        gson.toJson(message, ow);
        ow.close();
    }
}
