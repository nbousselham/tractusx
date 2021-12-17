package net.catenax.semantics.idsadapter.service;

import net.catenax.semantics.idsadapter.restapi.dto.Offer;

public interface IdsService {
    Offer getOrCreateOffer(String title, Offer offer);
    Object getSelfDescription();
}
