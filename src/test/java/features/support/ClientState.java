package features.support;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

import java.io.IOException;

public class ClientState {
    private HttpClient httpClient;
    private HttpMethod lastRequest;

    public ClientState() {
        HttpClientParams params = new HttpClientParams();
        params.setConnectionManagerTimeout(1000);
        httpClient = new HttpClient(params);
    }

    public void executeMethod(HttpMethod request) {
        try {
            httpClient.executeMethod(request);
            lastRequest = request;
        } catch (IOException e) {
            throw new RuntimeException("Can't execute http request", e);
        }
    }

    public String getLastResponse() {
        try {
            return lastRequest.getResponseBodyAsString();
        } catch (IOException e) {
            return null;
        }
    }

    public int getLastResponseStatus() {
        return lastRequest.getStatusCode();
    }
}
