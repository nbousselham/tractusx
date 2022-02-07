/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.framework.test;

import net.catenax.semantics.framework.config.*;
import org.springframework.context.annotation.Bean;

/**
 * helper to build mock configurations
 */
public class MockConfiguration<Cm extends Command, O extends Offer, Ct extends Catalog, Co extends Contract, T extends Transformation, Cd extends ConfigurationData<Cm,O,Ct,Co,T>> {

    /**
     * internal creation
     * @return a configuration
     */
    protected Cd createConfigurationData() {
        return (Cd) new ConfigurationData<Cm,O,Ct,Co,T>();
    }

    /**
     * @return default configuration
     */
    public Cd getConfigurationData() {
        Cd configurationData= createConfigurationData();
        configurationData.setConnectorUser("user");
        configurationData.setConnectorPassword("password");
        configurationData.setConnectorUrl("http://localhost:4242");
        return configurationData;
    }

    /**
     * no default data source
     */
    public javax.sql.DataSource getDataSource() {
        return new MockDataSource();
    }
}
