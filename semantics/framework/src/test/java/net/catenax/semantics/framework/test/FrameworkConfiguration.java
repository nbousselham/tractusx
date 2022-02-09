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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * spring configuration of the framework tests
 */
@Configuration
@Import({HttpClientConfiguration.class})
@ComponentScan(basePackages = {"net.catenax.semantics.framework"})
public class FrameworkConfiguration extends MockConfiguration {

    /**
     * @return default configuration
     */
    @Bean
    public Config<Command, Offer, Catalog, Contract, Transformation> getConfigurationData() {
        ConfigurationData<Command, Offer, Catalog, Contract, Transformation> configurationData= new ConfigurationData<Command, Offer, Catalog, Contract, Transformation>();
        configurationData.setConnectorUser("user");
        configurationData.setConnectorPassword("password");
        configurationData.setConnectorUrl("http://localhost:4242");
        return configurationData;
    }

    /**
     * no default data source
     */
    @Bean
    @Override
    public javax.sql.DataSource getDataSource() {
        return super.getDataSource();
    }

}
