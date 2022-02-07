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
public class FrameworkConfiguration extends MockConfiguration<Command,Offer,Catalog,Contract,Transformation,ConfigurationData<Command, Offer, Catalog, Contract, Transformation>> {

    /**
     * @return default configuration
     */
    @Bean
    @Override
    public ConfigurationData<Command, Offer, Catalog, Contract, Transformation> getConfigurationData() {
        return super.getConfigurationData();
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
