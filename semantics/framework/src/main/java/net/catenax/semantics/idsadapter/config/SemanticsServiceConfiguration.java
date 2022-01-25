/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.idsadapter.config;


import lombok.AllArgsConstructor;
import net.catenax.semantics.service.SemanticsService;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class SemanticsServiceConfiguration {
    final private AutowireCapableBeanFactory beanFactory;

    @Bean
    public SemanticsService getSemanticService() {
        return beanFactory.createBean(SemanticsService.class);
    }
}
