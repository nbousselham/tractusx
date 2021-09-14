package net.catenax.prs.acceptancetests;

import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ResponseResults {

    private final ClientHttpResponse response;
    private final String body;

    protected ResponseResults(final ClientHttpResponse response) throws IOException {
        this.response = response;
        this.body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    protected ClientHttpResponse getResponse() {
        return response;
    }

    protected String getBody() {
        return body;
    }
}
