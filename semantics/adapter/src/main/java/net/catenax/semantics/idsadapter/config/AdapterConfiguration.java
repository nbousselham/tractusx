/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.idsadapter.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ FhIdsConfiguration.class, SimpleAdapterConfiguration.class})
@AllArgsConstructor
public class AdapterConfiguration {

}
