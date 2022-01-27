/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.idsadapter.config;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import net.catenax.semantics.idsadapter.restapi.dto.Catalog;
import net.catenax.semantics.idsadapter.restapi.dto.Contract;

@Data
public class BaseIdsAdapterConfigProperties {
    private String connectorType;
    private String connectorUrl;
    private String connectorId;
    private String serviceUrl;
    private String adapterUrl;
    private String portalUrl;
    private String connectorUser;
    private String connectorPassword;
    private String publisher;
    private String proxyUrl;
    private int proxyPort;
    private String serviceName = "adapter";

    private Map<String, Catalog> catalogs = new HashMap<>();
    private Map<String, Contract> contracts = new HashMap<>();
}
