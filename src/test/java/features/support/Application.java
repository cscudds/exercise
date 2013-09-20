package features.support;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class Application {
    private final String baseUrl = "http://localhost:8080";

    public GetMethod getEndpoint(String path) {
        return new GetMethod(baseUrl + path);
    }

    public HttpMethod process() {
        return new PostMethod(baseUrl + "/process");
    }
}
