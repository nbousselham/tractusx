/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.framework.adapters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import net.catenax.semantics.framework.*;
import net.catenax.semantics.framework.auth.BearerTokenOutgoingInterceptor;
import net.catenax.semantics.framework.auth.BearerTokenWrapper;
import net.catenax.semantics.framework.config.*;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * adapts a remote twin registry to the IDS connector.
 */
@Service
public class TwinRegistryAdapter<Cmd extends Command, O extends Offer, Ct extends Catalog, Co extends Contract, T extends Transformation> extends BaseAdapter<Cmd,O,Ct,Co,T> {

    private final BearerTokenOutgoingInterceptor interceptor;

    /**
     * delegate to super
     * @param configurationData
     * @param connector
     */
    public TwinRegistryAdapter(Config<Cmd,O,Ct,Co,T> configurationData, IdsConnector connector, BearerTokenOutgoingInterceptor interceptor) {
        super(configurationData);
        setIdsConnector(connector);
        this.interceptor=interceptor;
    }

    /**
     * when the service start it may read its config
     * and automatically publish stuff
     */
    @PostConstruct
    public void setup() {
        if(configurationData.isRegisterOnStart()) {
            for(TwinSource twin : configurationData.getTwinSources()) {
                try {
                    System.out.println(registerTwins(twin.getProtocol(),twin.getCommand(),twin.getParameters()));
                } catch(Exception e) {
                    log.error("Could not register twins from command "+twin.getCommand()+" and protocol "+twin.getProtocol()+" in connector. Maybe it is not active?",e);
                }
            }
        }
    }

    /**
     * registers new twins
     * @param protocol use the backend protocol
     * @param command use the backend command
     * @param parameters a map of parameters
     * @return the registration response from the registry copied
     */
    public String registerTwins(String protocol, String command, Map<String,String> parameters) throws StatusException {
        IdsRequest request=new IdsRequest();
        request.setProtocol(protocol);
        request.setCommand(command);
        request.setParameters(parameters);
        request.setModel("urn:com.catenaX.semantics:1.2.0-SNAPSHOT#DigitialTwins");
        request.setAccepts("application/json");
        IdsResponse response = idsConnector.perform(request);
        try {
            IdsMessage responseMessage = response.getMessage().get();
            HttpClient httpclient=null;
            if(configurationData.getProxyUrl()!=null) {
                boolean noProxy = false;
                for (String noProxyHost : configurationData.getNoProxyHosts()) {
                    noProxy = noProxy || configurationData.getServiceUrl().contains(noProxyHost);
                }
                if (!noProxy) {
                    HttpHost proxyHost = new HttpHost(configurationData.getProxyUrl(), configurationData.getProxyPort());
                    DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxyHost);
                    HttpClientBuilder clientBuilder = HttpClients.custom();
                    clientBuilder = clientBuilder.setRoutePlanner(routePlanner);
                    clientBuilder = clientBuilder.addInterceptorFirst(interceptor);
                    httpclient = clientBuilder.build();
                }
            }

            if(httpclient==null) {
                HttpClientBuilder clientBuilder = HttpClients.custom();
                clientBuilder.addInterceptorFirst(interceptor);
                httpclient = clientBuilder.build();
            }
            HttpPost httppost = new HttpPost(configurationData.getServiceUrl() + "/registry/shell-descriptors");
            httppost.addHeader("accept", responseMessage.getMediaType());
            httppost.setHeader("Content-type", responseMessage.getMediaType());
            httppost.setEntity(new StringEntity(responseMessage.getPayload()));
            log.info("Accessing Twin Registry via " + httppost.getRequestLine());
            HttpResponse twinResponse = httpclient.execute(httppost);
            log.info("Received Twin Registry response " + twinResponse.getStatusLine());
            String finalResult = IOUtils.toString(twinResponse.getEntity().getContent());
            if (twinResponse.getStatusLine().getStatusCode() != 200) {
                if(finalResult==null || finalResult.isEmpty()) {
                    finalResult=twinResponse.getStatusLine().getReasonPhrase();
                }
                throw new StatusException(finalResult,twinResponse.getStatusLine().getStatusCode());
            }
            return finalResult;
        } catch(InterruptedException | ExecutionException e) {
            throw new StatusException("Could not synchronize on twin backend request",e,501);
        } catch(IOException e) {
            throw new StatusException("Could not perform twin registration request",e,501);
        }
    }

}
