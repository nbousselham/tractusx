/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.registry.repository;

import net.catenax.semantics.registry.model.Shell;
import org.springframework.data.relational.core.conversion.MutableAggregateChange;
import org.springframework.data.relational.core.mapping.event.AfterSaveCallback;
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Event Handler that is invoked before save
 * to try to map the given identification to the
 * ID field (and vice versa in case the identification is empty).
 * Does that recursively also for the given submodels that are already known
 */
@Component
public class ShellRepositoryEventHandler implements BeforeSaveCallback<Shell> {
    @Override
    public Shell onBeforeSave(Shell aggregate, MutableAggregateChange<Shell> aggregateChange) {
        if(aggregate.getId()==null) {
            try {
                String template=aggregate.getIdExternal();
                int lastIndex=template.lastIndexOf("#")+1;
                if(lastIndex>0) {
                    template=template.substring(lastIndex);
                }
                aggregate.setId(UUID.fromString(template));
            } catch(IllegalArgumentException e) {
                aggregate.setId(UUID.randomUUID());
            }
        }
        if(aggregate.getIdExternal().isEmpty()) {
            aggregate.setIdExternal(aggregate.getId().toString());
        }
        if(aggregate.getSubmodels()!=null) {
            aggregate.getSubmodels().forEach( submodel -> {
                if(submodel.getId()==null) {
                    try {
                        String template=submodel.getIdExternal();
                        int lastIndex=template.lastIndexOf("#")+1;
                        if(lastIndex>0) {
                            template=template.substring(lastIndex);
                        }
                        submodel.setId(UUID.fromString(template));
                    } catch(IllegalArgumentException e) {
                        submodel.setId(UUID.randomUUID());
                    }
                }
                if(submodel.getIdExternal().isEmpty()) {
                    submodel.setIdExternal(submodel.getId().toString());
                }
            });
        }
        return aggregate;
    }

}
