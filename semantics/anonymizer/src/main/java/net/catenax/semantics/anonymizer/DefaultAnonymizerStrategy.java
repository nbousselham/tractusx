/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.anonymizer;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.semantics.anonymizer.client.ApiClient;
import net.catenax.semantics.anonymizer.client.api.AnonymizeApi;
import net.catenax.semantics.anonymizer.client.model.Identifier;
import net.catenax.semantics.anonymizer.client.model.PseudonymGroup;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Default anonymization strategy and repository that are implemented
 * in-memory (or configured using spring)
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Data
public class DefaultAnonymizerStrategy implements AnonymizerStrategy, FederationRepository {

    @Value("${anonymizer.defaultDomain}")
    protected String pseudoDomain="urn:net.catenax.semantics.anonymizer#";
    private final Map<String,String> pseudos=new HashMap<>();
    private final Map<URL,Pattern> repo=new HashMap<>();
    private final Map<URL,AnonymizeApi> helpers=new HashMap<>();
    private boolean disableSsl=true;

    @Override
    public List<Map.Entry<String,String>> anonymize(String domain, String original) {
        String key=domain+original;
        List<Map.Entry<String,String>> result=new ArrayList<>();
        for(Map.Entry<URL,Pattern> federateService : repo.entrySet()) {
            if(federateService.getValue().matcher(key).matches()) {
                try {
                    Identifier id=new Identifier();
                    id.setDomain(domain);
                    id.setId(original);
                    for(PseudonymGroup group : helpers.get(federateService.getKey()).anonymize(List.of(id))) {
                        if (group.getIdentifier().getDomain().equals(domain) && group.getIdentifier().getId().equals(original)) {
                            for (Identifier pseudonym : group.getPseudonyms()) {
                                result.add(new AbstractMap.SimpleImmutableEntry<>(pseudonym.getDomain(), pseudonym.getId()));
                            }
                        }
                    }
                } catch (Exception e) {
                    log.warn("Could not access federated service. Trying to recover.",e);
                }
            }
        }
        if(result.isEmpty()) {
            String pseudo=pseudos.get(key);
            if(pseudo==null) {
                UUID uuid=UUID.randomUUID();
                long lsb = uuid.getLeastSignificantBits();
                long msb=uuid.getMostSignificantBits();
                byte[] uuidbytes = new byte[] {
                        (byte) (msb >>> 40),
                        (byte) (msb >>> 32),
                        (byte) (msb >>> 24),
                        (byte) (msb >>> 16),
                        (byte) (msb >>> 8),
                        (byte) (msb >>> 0),
                        (byte) (lsb >>> 40),
                        (byte) (lsb >>> 32),
                        (byte) (lsb >>> 24),
                        (byte) (lsb >>> 16),
                        (byte) (lsb >>> 8),
                        (byte) (lsb >>> 0),
                };
                pseudo=Base64.getEncoder().encodeToString(uuidbytes);
                pseudos.put(key,pseudo);
            }
            result.add(new AbstractMap.SimpleImmutableEntry<>(pseudoDomain,pseudo));
        }
        return result;
    }

    @Override
    public int forget(String domain, String original) {
       int result=0;
       for(Map.Entry<URL,Pattern> federateService : repo.entrySet()) {
           if (federateService.getValue().matcher(original).matches()) {
               Identifier id = new Identifier();
               id.setDomain(domain);
               id.setId(original);
               result += helpers.get(federateService.getKey()).forget(List.of(id)).getTotalPseudonyms();
           }
       }
       String key=domain+original;
       if(pseudos.containsKey(key)) {
          pseudos.remove(key);
          result+=1;
       }
       return result;
    }

    @Override
    public boolean register(String regex, URL endpoint) {
        repo.put(endpoint,Pattern.compile(regex));
        var client= new ApiClient().setBasePath(endpoint.toString());
        helpers.put(endpoint,client.buildClient(AnonymizeApi.class));
        return true;
    }

    @Override
    public String deregister(URL endpoint) {
        Pattern former=repo.remove(endpoint);
        if(former!=null) {
            helpers.remove(endpoint);
            return former.toString();
        }
        return null;
    }
}
