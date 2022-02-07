package net.catenax.semantics.adapter.test;

import net.catenax.semantics.adapter.ConfigurationData;
import net.catenax.semantics.framework.config.*;
import net.catenax.semantics.framework.test.MockConfiguration;
import net.catenax.semantics.framework.test.MockConfiguration;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = {"net.catenax.semantics.framework"})
public class AdapterConfiguration extends MockConfiguration<Command, Offer, Catalog, Contract, Transformation, ConfigurationData> {

    /**
     * no default data source
     */
    @Bean
    @Override
    public javax.sql.DataSource getDataSource() {
        return super.getDataSource();
    }

    /**
     * @return default configuration
     */
    @Override
    protected ConfigurationData createConfigurationData() {
        return new ConfigurationData();
    }

    /**
     * @return default configuration
     */
    @Bean
    public net.catenax.semantics.framework.config.ConfigurationData<?,?,?,?,?> getBaseConfigurationData() {
        return super.getConfigurationData();
    }

}
