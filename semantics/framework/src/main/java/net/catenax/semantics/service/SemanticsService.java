package net.catenax.semantics.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.semantics.exceptions.AdapterException;
import net.catenax.semantics.idsadapter.config.BaseIdsAdapterConfigProperties;
import net.catenax.semantics.idsadapter.restapi.dto.Source;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

@RequiredArgsConstructor
@Slf4j
public class SemanticsService {
    private final BaseIdsAdapterConfigProperties baseIdsAdapterConfigProperties;

    /**
     * registers new twin definitions
     * @param twinType the type of twins
     * @param twinSource the twin definitions as a payload
     * @return the registration response
     */
    public String registerTwinDefinitions(String twinType, String twinSource) {
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(baseIdsAdapterConfigProperties.getServiceUrl() + "/twins");
            httppost.addHeader("accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            httppost.setEntity(new StringEntity(twinSource));
            log.info("Accessing Twin Registry via " + httppost.getRequestLine());
            HttpResponse response = httpclient.execute(httppost);
            log.info("Received Twin Registry response " + response.getStatusLine());
            String finalResult = IOUtils.toString(response.getEntity().getContent());
            if (response.getStatusLine().getStatusCode() != 200) {

                throw new AdapterException(finalResult);
            }
            return finalResult;
        } catch (IOException e) {
            throw new AdapterException("can not register twin",e);
        }
    }
}
