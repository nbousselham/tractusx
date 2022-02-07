/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.framework.config;

import feign.Client;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import lombok.AllArgsConstructor;
import net.catenax.semantics.framework.dsc.client.api.*;
import net.catenax.semantics.framework.dsc.client.invoker.ApiClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import java.net.Proxy;
import java.net.InetSocketAddress;

/**
 * A spring configuration / bean factory for creating typed and proxiable http client objects
 * especially for talking to DSC.
 */
@Configuration
@AllArgsConstructor
public class HttpClientConfiguration {
    private final ConfigurationData configurationData;

    @Bean
    Feign.Builder feignBuilder() throws NoSuchAlgorithmException, KeyManagementException {
        ApiClient apiClient = new ApiClient();
        NaiveSSLSocketFactory naiveSSLSocketFactory = new NaiveSSLSocketFactory("localhost");

        Client client;
        
        if(configurationData.getProxyUrl()!=null) {
            client=new Client.Proxied(naiveSSLSocketFactory, null, new Proxy(Proxy.Type.HTTP, 
                new InetSocketAddress(configurationData.getProxyUrl(), configurationData.getProxyPort())));
        } else {
            client = new Client.Default(naiveSSLSocketFactory,null);
        }

        Feign.Builder feignBuilder = apiClient.getFeignBuilder();
        apiClient.setBasePath(configurationData.getConnectorUrl());
        feignBuilder.client(client)
                .requestInterceptor(new BasicAuthRequestInterceptor(configurationData.getConnectorUser(),
                    configurationData.getConnectorPassword()));
        return feignBuilder;
    }

    @Bean
    ConnectorApi connectorApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(ConnectorApi.class, configurationData.getConnectorUrl());
    }

    @Bean
    OfferedResourcesApi offersApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(OfferedResourcesApi.class, configurationData.getConnectorUrl());
    }

    @Bean
    CatalogsApi catalogApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(CatalogsApi.class, configurationData.getConnectorUrl());
    }

    @Bean
    ContractsApi contractApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(ContractsApi.class, configurationData.getConnectorUrl());
    }

    @Bean
    RulesApi rulesApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(RulesApi.class, configurationData.getConnectorUrl());
    }

    @Bean
    EndpointsApi endpointsApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(EndpointsApi.class, configurationData.getConnectorUrl());
    }

    @Bean
    RepresentationsApi representationsApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(RepresentationsApi.class, configurationData.getConnectorUrl());
    }

    @Bean
    RequestedResourcesApi requestedResourcesApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(RequestedResourcesApi.class, configurationData.getConnectorUrl());
    }

    @Bean
    ArtifactsApi artifactsApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(ArtifactsApi.class, configurationData.getConnectorUrl());
    }

    @Bean
    MessagesApi messagesApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(MessagesApi.class, configurationData.getConnectorUrl());
    }

    @Bean
    AgreementsApi agreementsApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(AgreementsApi.class, configurationData.getConnectorUrl());
    }
}
