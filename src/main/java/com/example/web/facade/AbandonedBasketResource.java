package com.example.web.facade;

import com.example.core.repository.UserGraphRepository;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Component
@Produces("application/json")
@Path("/abandoned-basket/{username}")
public class AbandonedBasketResource {

    public static final String JSON_KEY_BASKET = "basket";
    public static final String JSON_KEY_CUSTOMER = "customer";
    @Autowired
    UserGraphRepository userGraphRepository;

    @GET
    public Response abandonedBasket(@PathParam("username") String userName) {

        Vertex customer = userGraphRepository.getForUser(userName);

        Iterable<Vertex> allCustomerVisits = customer.getVertices(Direction.OUT, UserGraphRepository.VISIT_EDGE_LABEL);

        DateTime lastCheckoutDate = getLatestCheckoutDateTime(allCustomerVisits);
        List<Vertex> abandonedProducts = getProductsAddedAfterDate(allCustomerVisits, lastCheckoutDate);

        String jsonString = JSONBuildHelper.buildJSON(customer, abandonedProducts, JSON_KEY_CUSTOMER, JSON_KEY_BASKET);

        return Response.ok(jsonString).build();
    }

    private DateTime getLatestCheckoutDateTime(Iterable<Vertex> visits) {
        DateTime lastCheckoutDate = null;
        for (Vertex visit : visits) {
            // Caters for multiple check-outs per visit
            Iterable<Vertex> checkouts = visit.getVertices(Direction.OUT, UserGraphRepository.CHECK_OUT_EDGE_LABEL);
            for (Vertex checkout : checkouts) {
                Iterable<Edge> checkOutEdges = checkout.getEdges(Direction.IN);
                lastCheckoutDate = updateLatestCheckoutDateFromEdges(lastCheckoutDate, checkOutEdges);
            }
        }
        return lastCheckoutDate;
    }

    private DateTime updateLatestCheckoutDateFromEdges(DateTime lastCheckoutDate, Iterable<Edge> checkOutEdges) {
        for (Edge checkOutEdge : checkOutEdges) {
            DateTime checkoutDate = checkOutEdge.getProperty(UserGraphRepository.CHECKOUT_CREATED_AT_KEY);
            if (checkoutDate != null) {
                if (lastCheckoutDate == null) {
                    lastCheckoutDate = checkoutDate;
                } else {
                    if (checkoutDate.compareTo(lastCheckoutDate) > 0) {
                        lastCheckoutDate = checkoutDate;
                    }
                }
            }
        }
        return lastCheckoutDate;
    }

    private List<Vertex> getProductsAddedAfterDate(Iterable<Vertex> visits, DateTime latestCheckoutDate) {
        List<Vertex> products = new ArrayList<Vertex>();
        for (Vertex visit : visits) {
            Iterable<Edge> addedToBasket = visit.getEdges(Direction.OUT, UserGraphRepository.ADD_TO_BASKET_EDGE_LABEL);
            for (Edge basket : addedToBasket) {
                DateTime basketAddedDate = basket.getProperty(UserGraphRepository.BASKET_CREATED_AT_KEY);
                // Possibly no check-outs for customer so check for null
                if (latestCheckoutDate == null || basketAddedDate.compareTo(latestCheckoutDate) > 0) {
                    Vertex product = basket.getVertex(Direction.IN);
                    product.setProperty(UserGraphRepository.BASKET_QUANTITY_KEY, basket.getProperty(UserGraphRepository.BASKET_QUANTITY_KEY));  // assuming updating the property won't get persisted
                    products.add(product);
                }
            }
        }
        return products;
    }
}
