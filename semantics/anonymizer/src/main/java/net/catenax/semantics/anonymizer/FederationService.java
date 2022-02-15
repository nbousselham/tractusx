/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.anonymizer;

import lombok.RequiredArgsConstructor;
import net.catenax.semantics.anonymizer.api.DefederateApiDelegate;
import net.catenax.semantics.anonymizer.api.FederateApiDelegate;
import net.catenax.semantics.anonymizer.model.EndpointSpec;
import net.catenax.semantics.anonymizer.model.FederationSpec;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * Implements the federation business logic
 * with the help of an internal interface/configuration plane
 */
@Service
@RequiredArgsConstructor
public class FederationService implements FederateApiDelegate, DefederateApiDelegate {

    /**
     * links to a single repo
     */
    private final FederationRepository repository;

    /**
     * needed because of multiple code inheritance
     * @return native web request
     */
    @Override
    public Optional<NativeWebRequest> getRequest() {
        return FederateApiDelegate.super.getRequest();
    }

    /**
     * implements federation by delegating to the repo
     * @param federationSpec The request body contains a federation specification. (required)
     * @return response
     */
    @Override
    public ResponseEntity<FederationSpec> federate(FederationSpec federationSpec) {
        try {
            if (repository.register(federationSpec.getExpression(), new URL(federationSpec.getEndpoint()))) {
                return ResponseEntity.ok(federationSpec);
            }
            return (ResponseEntity<FederationSpec>) ResponseEntity.internalServerError();
        } catch(MalformedURLException e) {
            return (ResponseEntity<FederationSpec>) ResponseEntity.badRequest();
        }
    }

    /**
     * implements defederation by delegating to the repo
     * @param endpointSpec The request body contains the endpoint to deregister. (required)
     * @return response
     */

    @Override
    public ResponseEntity<FederationSpec> decouple(EndpointSpec endpointSpec) {
        FederationSpec fedSpec=new FederationSpec();
        fedSpec.setEndpoint(endpointSpec.getEndpoint());
        try {
            String regex=repository.deregister(new URL(endpointSpec.getEndpoint()));
            if(regex!=null) {
                fedSpec.setExpression(regex);
            }
            return ResponseEntity.ok(fedSpec);
        } catch(MalformedURLException e) {
            return (ResponseEntity<FederationSpec>) ResponseEntity.badRequest();
        }
    }

}
