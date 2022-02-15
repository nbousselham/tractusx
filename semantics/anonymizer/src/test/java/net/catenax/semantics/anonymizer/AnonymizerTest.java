/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.anonymizer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the anonymizer functions
 */
@SpringBootTest
public class AnonymizerTest {

    String sampleDomain="http://sample.domain.com/";
    String sampleId="mybusinessid";
    String sampleId2="myotherbusinessid";

    /**
     * the actual test code
     * @param context created spring context
     */
    @Test
    void testStrategy(ApplicationContext context) {
        assertThat(context).isNotNull();
        AnonymizerStrategy defaultStrategy=context.getBean(AnonymizerStrategy.class);
        assertThat(defaultStrategy).isNotNull();
        var pseudonyms=defaultStrategy.anonymize(sampleDomain,sampleId);
        assertThat(pseudonyms).isNotEmpty();
        var otherpseudonyms=defaultStrategy.anonymize(sampleDomain,sampleId2);
        assertThat(otherpseudonyms).isNotEmpty();
        assertThat(pseudonyms.get(0).getValue()).isNotEqualTo(otherpseudonyms.get(0).getValue());
        var repeatablePseudonyms = defaultStrategy.anonymize(sampleDomain,sampleId);
        assertThat(pseudonyms.get(0).getValue()).isEqualTo(repeatablePseudonyms.get(0).getValue());
        assertThat(defaultStrategy.forget(sampleDomain,sampleId)).isGreaterThan(0);
        var forgotPseudonyms = defaultStrategy.anonymize(sampleDomain,sampleId);
        assertThat(pseudonyms.get(0).getValue()).isNotEqualTo(forgotPseudonyms.get(0).getValue());
    }
}
