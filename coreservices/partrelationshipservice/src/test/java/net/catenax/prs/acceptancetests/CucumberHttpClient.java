package net.catenax.prs.acceptancetests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class CucumberHttpClient {

    private final static String SERVER_URL = "http://localhost";

    @LocalServerPort
    private int port;
    @Autowired
    private CucumberHttpClientResult responseResults;

    private final RestTemplate restTemplate = new RestTemplate();

    protected void executeGet(String path) {
        RequestCallback addHeaders = r -> r.getHeaders().add("Accept", "application/json");

        restTemplate.execute(getUrl(path),
                HttpMethod.GET,
                addHeaders,
                response -> {
                    responseResults.setResponse(response);
                    return response;
                });
    }

    private String getUrl(String path) {
        return SERVER_URL + ":" + port + path;
    }
}
