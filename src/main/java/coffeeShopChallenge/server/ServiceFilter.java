package coffeeShopChallenge.server;

import coffeeShopChallenge.shopservice.CoffeeShopService;

import javax.servlet.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by Michael on 7/13/2017.
 */
public class ServiceFilter implements Filter {
    CoffeeShopService coffeeShopService;

    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            InputStream instr = getClass().getResourceAsStream("/locations.csv");
            Reader reader = new InputStreamReader(instr, "utf-8");
            coffeeShopService = new CoffeeShopService(reader);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setAttribute(CoffeeShopHandler.ServiceAttrName, coffeeShopService);
    }

    public void destroy() {
    }
}
