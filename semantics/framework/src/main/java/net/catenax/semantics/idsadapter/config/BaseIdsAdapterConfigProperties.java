package net.catenax.semantics.idsadapter.config;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import net.catenax.semantics.idsadapter.restapi.dto.Catalog;
import net.catenax.semantics.idsadapter.restapi.dto.Contract;

@Data
public class BaseIdsAdapterConfigProperties {
    private String connectorUrl;
    private String serviceUrl;
    private String adapterUrl;
    private String portalUrl;
    private String connectorUser;
    private String connectorPassword;
    private String publisher;
    private String proxyUrl;
    private int proxyPort;
    private String serviceName = "adpater";

    private Map<String, Catalog> catalogs = new HashMap<>();
    private Map<String, Contract> contracts = new HashMap<>();
}
