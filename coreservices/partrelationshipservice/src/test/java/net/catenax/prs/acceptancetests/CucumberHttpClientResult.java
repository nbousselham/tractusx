package net.catenax.prs.acceptancetests;

import io.cucumber.spring.ScenarioScope;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@ScenarioScope
@Getter
public class CucumberHttpClientResult {

    private String body;
    private HttpStatus statusCode;

    public void setResponse(ClientHttpResponse response) throws IOException {
        this.body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
        this.statusCode = response.getStatusCode();
    }

}
