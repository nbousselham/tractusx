/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.anonymizer;

import java.net.URL;

/**
 * interface to a federation repository that keeps
 * links to remote anonymizers for particular
 * identifiers.
 */
public interface FederationRepository {

    /**
     * registers a new endpoint
     * @param regex regular expression that the identifiers should match
     * @param endpoint unique resource location
     * @return whether the endpoint could be registered
     */
    public boolean register(String regex, URL endpoint);

    /**
     * deregisters a given endpoint
     * @param endpoint
     * @return the regular expression that the endpoint was responsible for
     */
    public String deregister(URL endpoint);
}
