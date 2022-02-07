/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.framework.transformers;

import net.catenax.semantics.framework.IdsMessage;
import net.catenax.semantics.framework.IdsRequest;
import net.catenax.semantics.framework.StatusException;
import net.catenax.semantics.framework.Transformer;
import org.springframework.stereotype.Service;

/**
 * a default transformer that does not do anything
 * as long as the specifications fit.
 */
@Service
public class IdentityTransformer implements Transformer {

    @Override
    public boolean canHandle(IdsMessage incoming, IdsRequest request, String targetModel) {
        boolean accepted="*/*".equals(request.getAccepts()) || request.getAccepts().contains(incoming.getMediaType());
        boolean sameSemantics=targetModel.equals(incoming.getModel());
        return accepted && sameSemantics;
    }

    @Override
    public IdsMessage transform(IdsMessage incoming, IdsRequest request, String targetModel) throws StatusException {
        return incoming;
    }
}
