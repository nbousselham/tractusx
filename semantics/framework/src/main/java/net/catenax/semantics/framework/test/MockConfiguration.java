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
public class MockConfiguration {

    /**
     * no default data source
     */
    @Bean
    public javax.sql.DataSource getDataSource() {
        return new MockDataSource();
    }
}
