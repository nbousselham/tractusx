/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.idsadapter.service;

import net.catenax.semantics.idsadapter.restapi.dto.Offer;

public interface IdsService {
    Offer getOrCreateOffer(String title, Offer offer);
    Object getSelfDescription();
}
