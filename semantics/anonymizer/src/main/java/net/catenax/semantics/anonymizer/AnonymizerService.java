/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.anonymizer;

import lombok.RequiredArgsConstructor;
import net.catenax.semantics.anonymizer.api.AnonymizeApiDelegate;
import net.catenax.semantics.anonymizer.api.ForgetApiDelegate;
import net.catenax.semantics.anonymizer.model.Identifier;
import net.catenax.semantics.anonymizer.model.PseudonymGroup;
import net.catenax.semantics.anonymizer.model.Statistics;
import net.catenax.semantics.framework.StatusException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implements the anonymization business logic
 * with the help of an internal interface/configuration plane
 */
@Service
@RequiredArgsConstructor
public class AnonymizerService implements AnonymizeApiDelegate, ForgetApiDelegate {

    /** has a set of strategies to try */
    private final AnonymizerStrategy[] defaultStrategies;

    /**
     * needed because of multiple code inheritence
     * @return native web request by delegating to one super interface
     */
    @Override
    public Optional<NativeWebRequest> getRequest() {
        return AnonymizeApiDelegate.super.getRequest();
    }

    /**
     * implements the anonymization logic
     * @param requestBody a list of identifiers to anonymize
     * @return a list of pseudonym groups for the distinct identifiers given
     */
    @Override
    public ResponseEntity<List<PseudonymGroup>> anonymize(List<Identifier> requestBody) {
        try {
            Map<String,PseudonymGroup> result = new HashMap<>();
            for (Identifier original : requestBody) {
                String key=original.getDomain()+original.getId();
                if(!result.containsKey(key)) {
                    PseudonymGroup group = new PseudonymGroup();
                    group.setIdentifier(original);
                    group.setPseudonyms(anonymizeSingle(original));
                    result.put(key, group);
                }
            }
            return ResponseEntity.ok(result.values().stream().collect(Collectors.toList()));
        } catch(StatusException se) {
            return (ResponseEntity<List<PseudonymGroup>>) ResponseEntity.status(se.getStatus());
        }
    }

    /**
     * helper to anonymize a single identifier
     * uses the first strategy that it can find
     * @param original the identifier to anonymize
     * @return the first pseudonym generated
     */
    protected List<Identifier> anonymizeSingle(Identifier original) throws StatusException {
        for(AnonymizerStrategy strategy:defaultStrategies) {
            List<Map.Entry<String,String>> pseudos=strategy.anonymize(original.getDomain(),original.getId());
            if(pseudos!=null && pseudos.size()>0) {
                return pseudos.stream().map( entry -> {
                    Identifier result=new Identifier();
                    result.setDomain(entry.getKey());
                    result.setId(entry.getValue());
                    return result;
                }).collect(Collectors.toList());
            }
        }
        throw new StatusException("Could not generate a pseudonym",500);
    }

    /**
     * implements the forget logic
     * @param requestBody a list of identifiers to forget
     * @return number of pseudonyms deleted
     */
    @Override
    public ResponseEntity<Statistics> forget(List<Identifier> requestBody) {
        var responseEntity=new Statistics();
        int totalNumber=0;
        for(AnonymizerStrategy strategy:defaultStrategies) {
            for (Identifier id : requestBody) {
                totalNumber += strategy.forget(id.getDomain(), id.getId());
            }
        }
        responseEntity.setTotalPseudonyms(totalNumber);
        return ResponseEntity.ok(responseEntity);
    }

}
