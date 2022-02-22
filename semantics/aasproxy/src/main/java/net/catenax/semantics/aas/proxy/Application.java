/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.aas.proxy;

import feign.Client;
import feign.Feign;
import lombok.Data;
import net.catenax.semantics.aas.api.RegistryAndDiscoveryInterfaceApi;
import net.catenax.semantics.framework.dsc.client.invoker.ApiClient;
import net.catenax.semantics.framework.edc.EdcService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Main AAS Proxy Application
 * TODO make sure openapi description is correct, referrer-header should give us a hint.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"net.catenax.semantics.aas", "org.openapitools.configuration"})
//@ImportResource({"classpath*:saynomore.lmx"})
@Data
public class Application {

    /**
     * enable ubiquitous CORS when accessed locally
     * @return a webmvc configurer which enables CORS
     */
    @Bean
    public WebMvcConfigurer configurer(ApplicationContext context) {
        BearerTokenIncomingInterceptor interceptor=context.getBean(BearerTokenIncomingInterceptor.class);

        return new WebMvcConfigurer(){
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(interceptor);
            }
        };
    }

    @Bean
    public BearerTokenIncomingInterceptor bearerTokenIncomingInterceptor(ApplicationContext context) {
        return new BearerTokenIncomingInterceptor(context.getBean(BearerTokenWrapper.class));
    }

    @Bean
    public BearerTokenOutgoingInterceptor bearerTokenOutgoingInterceptor(ApplicationContext context) {
        return new BearerTokenOutgoingInterceptor(context.getBean(BearerTokenWrapper.class));
    }

    @Bean
    @ConfigurationProperties(prefix="aasproxy")
    public ConfigurationData getConfiguration() {
        return new ConfigurationData();
    }

    @Bean
    public RegistryAndDiscoveryInterfaceApi remoteRegistry(ApplicationContext context)  {
        Feign.Builder builder=context.getBean(Feign.Builder.class);
        ConfigurationData config=context.getBean(ConfigurationData.class);
        return builder.target(RegistryAndDiscoveryInterfaceApi.class,config.getTargetUrl());
    }

    @Bean
    public Feign.Builder feignBuilder(ApplicationContext context) throws NoSuchAlgorithmException, KeyManagementException {
        ConfigurationData config=context.getBean(ConfigurationData.class);
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(config.getTargetUrl());
        NaiveSSLSocketFactory naiveSSLSocketFactory = new NaiveSSLSocketFactory("localhost");
        Client client = null;
        if(config.getProxyHost()!=null) {
            boolean isNoProxy=false;
            for(String host : config.getNoProxyHosts().split("; ")) {
               if(config.getTargetUrl().contains(host)) {
                   isNoProxy=true;
               }
            }
            if(!isNoProxy) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config.getProxyHost(), config.getProxyPort()));
                if (config.getProxyUser() != null) {
                    client = new Client.Proxied(naiveSSLSocketFactory, null, proxy, config.getProxyUser(), config.getProxyPass());
                } else {
                    client = new Client.Proxied(naiveSSLSocketFactory, null, proxy);
                }
            }
        }
        if(client==null) {
            client = new Client.Default(naiveSSLSocketFactory, null);
        }
        Feign.Builder feignBuilder = apiClient.getFeignBuilder();
        BearerTokenOutgoingInterceptor interceptor=context.getBean(BearerTokenOutgoingInterceptor.class);
        feignBuilder.client(client)
                .requestInterceptor(interceptor);
        return feignBuilder;
    }

    /**
     * entry point if started as an app
     * @param args command line
     */
    public static void main(String[] args) {
        // bootstrap EDC
        EdcService.bootstrap();
        new SpringApplication(Application.class).run(args);
    }

}