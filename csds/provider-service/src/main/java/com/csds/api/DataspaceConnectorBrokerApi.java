package com.csds.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@FeignClient(name = "DataspaceConnectorBrokerApi", url = "placeholder", configuration = DataspaceConnectorConfiguration.class)
public interface DataspaceConnectorBrokerApi {
    @PostMapping(path = "/ids/connector/update")
    void registerOrUpdateConnector(URI baseUrl, @RequestHeader("Authorization") String authHeader, @RequestParam("recipient") String brokerUrl);

    @PostMapping(path = "/ids/connector/unavailable")
    void unregisterConnector(URI baseUrl,  @RequestHeader("Authorization") String authHeader, @RequestParam("recipient") String brokerUrl);

    @PostMapping(path = "/ids/resource/update")
    void updateResource(URI baseUrl,  @RequestHeader("Authorization") String authHeader, @RequestParam("resourceId") String resourceId, @RequestParam("recipient") String brokerUrl);

    @PostMapping(path = "/ids/resource/unavailable")
    void removeResource(URI baseUrl,  @RequestHeader("Authorization") String authHeader, @RequestParam("resourceId") String resourceId, @RequestParam("recipient") String brokerUrl);
    
    @PostMapping(path = "/ids/search")
    String searchResource(URI baseUrl,  @RequestHeader("Authorization") String authHeader, @RequestParam("recipient") String resourceId, @RequestBody String body);

}
