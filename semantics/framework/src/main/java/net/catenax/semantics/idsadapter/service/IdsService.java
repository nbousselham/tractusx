/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.idsadapter.service;

import net.catenax.semantics.idsadapter.restapi.dto.Offer;

/**
 * Interface to any IDS service that this adapter
 * is attached to
 */
public interface IdsService {
    /**
     * Creates a particular offer
     * @param title
     * @param offer
     * @return
     */
    Offer getOrCreateOffer(String title, Offer offer);

    /**
     * presents a self-description
     * @return
     */
    Object getSelfDescription();
}
