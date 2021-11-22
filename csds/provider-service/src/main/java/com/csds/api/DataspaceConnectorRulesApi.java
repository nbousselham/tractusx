package com.csds.api;

import com.csds.model.RuleDescription;
import com.csds.model.RuleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.UUID;

@FeignClient(name = "DataspaceConnectorRulesApi", url = "placeholder", configuration = DataspaceConnectorConfiguration.class)
public interface DataspaceConnectorRulesApi {
    @PostMapping(path = "/rules", consumes = MediaType.APPLICATION_JSON_VALUE)
    RuleResponse registerRule(URI baseUrl, @RequestBody RuleDescription metadata);
    
    @DeleteMapping(path = "/rules/{id}")
    void deleteRule(URI baseUrl, @PathVariable("id") UUID id);
}
