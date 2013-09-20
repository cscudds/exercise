package features.support;

import org.apache.commons.httpclient.methods.GetMethod;

public class Application {
    private final String baseUrl = "http://localhost:8080";

    public GetMethod getEndpoint(String path) {
        return new GetMethod(baseUrl + path);
    }
}
