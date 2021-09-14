package net.catenax.prs.acceptancetests;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CucumberContextConfiguration
@SpringBootTest
public class AcceptanceTestsBase {

    protected static ResponseResults latestResponse = null;
    protected RestTemplate restTemplate = new RestTemplate();

    protected void executeGet(String url) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");

        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate.execute(url, HttpMethod.GET, requestCallback, response -> {
            if (errorHandler.hadError) {
                return (errorHandler.getResults());
            }
            return new ResponseResults(response);
        });

    }

    protected void executePost(String url) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");

        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate.execute(url, HttpMethod.POST, requestCallback, response -> {
            if (errorHandler.hadError) {
                return (errorHandler.getResults());
            } else {
                return (new ResponseResults(response));
            }
        });

    }

    private static class ResponseResultErrorHandler implements ResponseErrorHandler {
        private ResponseResults results = null;
        private Boolean hadError = false;

        private ResponseResults getResults() {
            return results;
        }

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            hadError = response.getRawStatusCode() >= 400;
            return hadError;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            results = new ResponseResults(response);
        }
    }

    private static class HeaderSettingRequestCallback implements RequestCallback {

        private final Map<String, String> requestHeaders;
        private String body;

        public HeaderSettingRequestCallback(final Map<String, String> headers) {
            this.requestHeaders = headers;
        }

        public void setBody(final String postBody) {
            this.body = postBody;
        }

        @Override
        public void doWithRequest(ClientHttpRequest request) throws IOException {
            final HttpHeaders clientHeaders = request.getHeaders();
            for (final Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                clientHeaders.add(entry.getKey(), entry.getValue());
            }
            if (null != body) {
                request.getBody().write(body.getBytes());
            }
        }
    }
}
