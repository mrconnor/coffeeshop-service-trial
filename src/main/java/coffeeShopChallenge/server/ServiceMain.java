package coffeeShopChallenge.server;

import coffeeShopChallenge.shopservice.CoffeeShopService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by Michael on 7/11/2017.
 */
public class ServiceMain {
    static Logger logger = LoggerFactory.getLogger(ServiceMain.class);

    public static void main(String[] args) throws Exception {
        InputStream instr = ServiceMain.class.getResourceAsStream("/locations.csv");
        Reader reader = new InputStreamReader(instr, "utf-8");
        CoffeeShopService coffeeShopService = new CoffeeShopService(reader);

        Server server = new Server(8080);

        ServletContextHandler ctx =
                new ServletContextHandler(ServletContextHandler.NO_SESSIONS);

        ctx.setContextPath("/coffeeshop");
        ctx.setAttribute("coffeeShopService", coffeeShopService);
        server.setHandler(ctx);

        ServletHolder serHol = ctx.addServlet(ServletContainer.class, "/*");
        serHol.setInitOrder(1);
        serHol.setInitParameter("jersey.config.server.provider.packages",
                "kinsaCoffee.server");

        try {
            logger.info("Starting server");
            server.start();
            server.join();
        } catch (Exception ex) {
            logger.error("SERVER ERROR: " + ex);
        } finally {
            server.destroy();
        }
    }
}
