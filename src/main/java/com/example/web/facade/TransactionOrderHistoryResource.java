package com.example.web.facade;

import com.example.core.repository.UserGraphRepository;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
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
@Path("/transaction/{username}")
public class TransactionOrderHistoryResource {

    public static final String JSON_KEY_ORDERS = "orders";
    public static final String JSON_KEY_CUSTOMER = "customer";
    @Autowired
    UserGraphRepository userGraphRepository;

    @GET
    public Response transactionOrderHistory(@PathParam("username") String userName) {

        Vertex customer = userGraphRepository.getForUser(userName);

        Iterable<Vertex> allCustomerVisits = customer.getVertices(Direction.OUT, UserGraphRepository.VISIT_EDGE_LABEL);

        List<Vertex> orders = new ArrayList<Vertex>();

        for(Vertex visit : allCustomerVisits) {
            Iterable<Vertex> checkouts = visit.getVertices(Direction.OUT, UserGraphRepository.CHECK_OUT_EDGE_LABEL);
            for (Vertex checkout : checkouts) {
                orders.add(checkout);
            }
        }

        String jsonString = JSONBuildHelper.buildJSON(customer, orders, JSON_KEY_CUSTOMER, JSON_KEY_ORDERS);

        return Response.ok(jsonString).build();
    }

}
