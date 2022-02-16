/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.aas.proxy;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

/**
 * A feign request to insert a saved bearer token into the outgoing communication
 */
@RequiredArgsConstructor
public class BearerTokenOutgoingInterceptor implements RequestInterceptor {

    private final BearerTokenWrapper tokenWrapper;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Authorization", "Bearer "+tokenWrapper.getToken());
    }
}
