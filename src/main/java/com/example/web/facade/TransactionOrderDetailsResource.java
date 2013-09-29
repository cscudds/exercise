package com.example.web.facade;

import com.example.core.repository.UserGraphRepository;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.lang.Integer;
import java.util.*;

@Component
@Produces("application/json")
@Path("/transaction/{username}/{orderid}")
public class TransactionOrderDetailsResource {

    public static final String JSON_KEY_ORDER = "order";
    public static final String JSON_KEY_ORDERID = "orderID";
    public static final String JSON_KEY_ORDERS = "lines";
    public static final String JSON_KEY_CUSTOMER = "customer";

    @Autowired
    UserGraphRepository userGraphRepository;

    @GET
    public Response transactionOrderDetails(@PathParam("username") String userName, @PathParam("orderid") String orderID) {

        Vertex customer = userGraphRepository.getForUser(userName);

        Iterable<Vertex> allCustomerVisits = customer.getVertices(Direction.OUT, UserGraphRepository.VISIT_EDGE_LABEL);

        Map<DateTime, Vertex> orders = getDateTimeVertexMap(allCustomerVisits);

        Interval orderDateTimeInterval = getOrderDateTimeInterval(orders, orderID);
        List<Vertex> products = getProductsAddedWithinInterval(allCustomerVisits, orderDateTimeInterval);
        String jsonString = buildJSON(customer, products, orderID);

        return Response.ok(jsonString).build();
    }

    private Map<DateTime, Vertex> getDateTimeVertexMap(Iterable<Vertex> allCustomerVisits) {
        Map<DateTime, Vertex> map = new HashMap<DateTime, Vertex>();

        for (Vertex visit : allCustomerVisits) {
            Iterable<Vertex> checkouts = visit.getVertices(Direction.OUT, UserGraphRepository.CHECK_OUT_EDGE_LABEL);
            for (Vertex checkout : checkouts) {
                Iterable<Edge> edges = visit.getEdges(Direction.OUT, UserGraphRepository.CHECK_OUT_EDGE_LABEL);
                for (Edge checkoutEdge : edges) {
                    DateTime checkoutDateTime = checkoutEdge.getProperty(UserGraphRepository.CHECKOUT_CREATED_AT_KEY);
                    map.put(checkoutDateTime, checkout);
                }
            }
        }
        return map;
    }

    // TODO: Refactor into JSONBuildHelper
    private String buildJSON(Vertex rootVertex, List<Vertex> vertices, String orderID) {
        String jsonString = "";
        JSONObject jsonEnvelope = new JSONObject();
        JSONObject jsonOrderEnvelope = new JSONObject();
        JSONArray lines = new JSONArray();

        try {
            JSONObject jsonRootObject = JSONBuildHelper.getJSONFromElement(rootVertex);
            for (Vertex vertex : vertices) {
                lines.put(JSONBuildHelper.getJSONFromElement(vertex));
            }
            jsonOrderEnvelope.accumulate(JSON_KEY_ORDERID, orderID);
            jsonOrderEnvelope.accumulate(JSON_KEY_ORDERS, lines);
            jsonRootObject.accumulate(JSON_KEY_ORDER, jsonOrderEnvelope);
            jsonEnvelope.accumulate(JSON_KEY_CUSTOMER, jsonRootObject);
            jsonString = jsonEnvelope.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonString;
    }


    private List<Vertex> getProductsAddedWithinInterval(Iterable<Vertex> allCustomerVisits, Interval orderDateTimeInterval) {

        List<Vertex> list = new ArrayList<Vertex>();

        for (Vertex visit : allCustomerVisits) {
            Iterable<Edge> addedToBasket = visit.getEdges(Direction.OUT, UserGraphRepository.ADD_TO_BASKET_EDGE_LABEL);
            for (Edge basket : addedToBasket) {
                DateTime basketAddedDate = basket.getProperty(UserGraphRepository.BASKET_CREATED_AT_KEY);
                if (orderDateTimeInterval.contains(basketAddedDate)) {
                    Vertex product = basket.getVertex(Direction.IN);
                    int basketQuantity = Integer.valueOf(basket.getProperty(UserGraphRepository.BASKET_QUANTITY_KEY).toString());
                    if (list.contains(product)) {
                        int productQuantity = Integer.valueOf(product.getProperty(UserGraphRepository.BASKET_QUANTITY_KEY).toString());
                        product.setProperty(UserGraphRepository.BASKET_QUANTITY_KEY, basketQuantity + productQuantity);  // assuming updating the property won't get persisted
                    } else {
                        product.setProperty(UserGraphRepository.BASKET_QUANTITY_KEY, basketQuantity);
                        list.add(product);
                    }
                }
            }
        }

        return list;
    }

    private Interval getOrderDateTimeInterval(Map<DateTime, Vertex> orders, String orderID) {
        DateTime lastPriorCheckoutDateTime = null;
        DateTime orderCheckoutDateTime = null;

        List<DateTime> checkoutDates = new ArrayList<DateTime>(orders.keySet());
        Collections.sort(checkoutDates);

        for (DateTime checkoutDate : checkoutDates) {
            if (lastPriorCheckoutDateTime == null) {
                lastPriorCheckoutDateTime = checkoutDate;
            }
            if (orders.get(checkoutDate).getProperty(UserGraphRepository.CHECKOUT_ORDER_ID_KEY).equals(orderID)) {
                orderCheckoutDateTime = checkoutDate;
                break;
            }
            lastPriorCheckoutDateTime = checkoutDate;
        }

        return new Interval(lastPriorCheckoutDateTime, orderCheckoutDateTime);
    }
}
