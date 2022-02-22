/*
 * Copyright (c) 2022 Robert Bosch Manufacturing Solutions GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.catenax.semantics.registry.model;


import lombok.Data;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.Set;
import java.util.UUID;

@Data
public class Submodel {
    @Id
    UUID id;

    String idExternal;
    String idShort;
    String semanticId;

    @MappedCollection(idColumn = "FK_SUBMODEL_ID")
    Set<SubmodelDescription> descriptions;

    @MappedCollection(idColumn = "FK_SUBMODEL_ID")
    Set<SubmodelEndpoint> endpoints;

    @Column( "FK_SHELL_ID")
    UUID shellId;

    /**
     * create a new submodel
     * @param id
     * @param idExternal
     * @param idShort
     * @param semanticId
     * @param descriptions
     * @param endpoints
     * @param shellId
     */
    public Submodel(UUID id, String idExternal, String idShort, String semanticId, Set<SubmodelDescription> descriptions, Set<SubmodelEndpoint> endpoints, UUID shellId) {
        this.id=id;
        this.idExternal=idExternal;
        this.idShort=idShort;
        this.semanticId=semanticId;
        this.descriptions=descriptions;
        this.endpoints=endpoints;
        this.shellId=shellId;
    }
}
