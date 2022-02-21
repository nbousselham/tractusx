/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.registry.repository;

import net.catenax.semantics.registry.model.Submodel;
import org.springframework.data.relational.core.conversion.MutableAggregateChange;
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Event Handler that is invoked before save
 * to try to map the given identification to the
 * ID field (and vice versa in case the identification is empty).
 */
@Component
public class SubmodelRepositoryEventHandler implements BeforeSaveCallback<Submodel> {
    @Override
    public Submodel onBeforeSave(Submodel aggregate, MutableAggregateChange<Submodel> aggregateChange) {
        if(aggregate.getId()==null) {
            try {
                aggregate.setId(UUID.fromString(aggregate.getIdExternal()));
            } catch(IllegalArgumentException e) {
                aggregate.setId(UUID.randomUUID());
            }
        }
        if(aggregate.getIdExternal().replace(" ","").isEmpty()) {
            aggregate.setIdExternal(aggregate.getId().toString());
        }
        return aggregate;
    }
}
