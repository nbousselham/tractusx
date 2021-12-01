/*
Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.idsadapter.restapi.dto;

import java.util.Map;
import java.util.UUID;

import lombok.Data;

/**
 * Represents a source = artifact for the adaption
 */
@Data
public class Source {
    /**
     * id of the source (once registered or to be registered)
     */
    private UUID id;
    /**
     * complete uri of the source (once registered)
     */
    private String uri;
    /**
     * description of the source
     */
    private String description;
    /**
     * a concrete source file
     */
    private String file;
    /**
     * a concrete transformation file
     */
    private String transformation;
    /**
     * the type of source, such as file or database
     */
    private String type;
    /**
     * some mapping of parameters to views
     */
    private Map<String,String> aliases;
    /**
     * a database connection associated to the source
     */
    private DataSource datasource;
    /**
     * determines the callback method of the adapter to access this artifact
     */
    private String callbackPattern="%1$s/adapter/download?offer=%2$s&representation=%3$s&source=%4$s";
}
