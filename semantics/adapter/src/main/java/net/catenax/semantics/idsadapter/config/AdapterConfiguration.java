package net.catenax.semantics.idsadapter.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ FhIdsConfiguration.class, SimpleAdapterConfiguration.class})
@AllArgsConstructor
public class AdapterConfiguration {

}
