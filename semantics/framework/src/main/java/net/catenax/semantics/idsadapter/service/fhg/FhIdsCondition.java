/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.idsadapter.service.fhg;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * A spring configuration condition that
 * evaluates the usage of the fraunhofer ids connector and
 * provides a framework for further connector types
 */
@Configuration
public class FhIdsCondition implements Condition {

    public static String CONNECTOR_TYPE_PROPERTY = "idsadapter.connectortype";
    public static String FH_IDS_CONNECTOR_TYPE = "FHG";

    public static String getConnectorType(ConditionContext context) {
        return context.getEnvironment().getProperty(CONNECTOR_TYPE_PROPERTY,FH_IDS_CONNECTOR_TYPE);
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return FH_IDS_CONNECTOR_TYPE.equals(FhIdsCondition.getConnectorType(context));
    }
}