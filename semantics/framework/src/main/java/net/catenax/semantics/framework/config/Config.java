/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.framework.config;

import java.util.List;
import java.util.Map;

/**
 * represents a (part) of a configuration file, such as a spring boot yml.
 */
public interface Config<Cmd extends Command, O extends Offer, Ct extends Catalog, Co extends Contract, T extends Transformation> {
    public String getConnectorType();
    public String getConnectorUrl();
    public String getConnectorId();
    public String getServiceUrl();
    public String getAdapterUrl();
    public String getPortalUrl();
    public String getConnectorUser();
    public String getConnectorPassword();
    public String getPublisher();
    public String getProxyUrl();
    public int getProxyPort();
    public List<String> getNoProxyHosts();
    /**
     * determines the callback method to access artifacts
     */
    public String getCallbackPattern();

    public String getServiceName();
    public boolean isOfferOnStart();
    public boolean isRegisterOnStart();

    public Map<String, Ct> getCatalogs();
    public Map<String, Co> getContracts();
    public Map<String, O> getOffers();
    public Map<String, Cmd> getCommands();
    public Map<String,T> getTransformations();
    public Map<String,String> getTransformationParameters();
    public List<TwinSource> getTwinSources();
}
