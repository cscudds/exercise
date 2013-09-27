package com.example.web.facade;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONMode;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONUtility;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JSONBuildHelper {

    public static String buildJSON(Vertex rootVertex, List<Vertex> vertices, String rootJSONKey, String arrayJSONKey) {
        String jsonString = "";
        JSONObject jsonEnvelope = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            JSONObject jsonRootObject = getJSONFromElement(rootVertex);
            for (Vertex vertex : vertices) {
                jsonArray.put(getJSONFromElement(vertex));
            }
            jsonRootObject.accumulate(arrayJSONKey, jsonArray);
            jsonEnvelope.accumulate(rootJSONKey, jsonRootObject);
            jsonString = jsonEnvelope.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            // throw something more meaningful - gather data from this scope into custom exception type
        }
        return jsonString;
    }

    public static JSONObject getJSONFromElement(Element element) throws JSONException {
        Set<String> graphSONPropertyStrings = getGraphSONPropertyStrings();
        return GraphSONUtility.jsonFromElement(element, graphSONPropertyStrings, GraphSONMode.COMPACT);
    }

    private static Set<String> getGraphSONPropertyStrings() {
        Set<String> props = new HashSet<String>();
        props.add("name");
        props.add("image");
        props.add("price");
        props.add("qty");
        props.add("email");
        props.add("orderID");
        return props;
    }
}
