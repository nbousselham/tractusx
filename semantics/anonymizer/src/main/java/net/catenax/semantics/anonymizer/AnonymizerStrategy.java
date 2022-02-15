/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.anonymizer;

import java.util.List;
import java.util.Map;

/**
 * An anonymization strategy may assign several pseudonyms to a single original
 * id and is able to forget what is has done
 */
public interface AnonymizerStrategy {
    /**
     * anonymize
     * @param original
     * @return a set of pseudonyms as mapped according the strategy
     */
    List<Map.Entry<String,String>> anonymize(String domain, String original);

    /**
     * forget
     * @param domain
     * @param original
     * @return number of deleted pseudonyms
     */
    int forget(String domain, String original);
}
