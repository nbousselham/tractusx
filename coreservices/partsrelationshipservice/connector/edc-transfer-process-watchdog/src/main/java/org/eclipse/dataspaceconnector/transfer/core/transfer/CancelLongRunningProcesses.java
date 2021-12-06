//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//

package org.eclipse.dataspaceconnector.transfer.core.transfer;

import lombok.Builder;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;

import static java.lang.String.format;
import static org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates.IN_PROGRESS;

@Builder
class CancelLongRunningProcesses implements Runnable {
    private final Monitor monitor;
    private final int batchSize;
    private final long stateTimeout;

    private final TransferProcessStore transferProcessStore;

    public void run() {
        var transferProcesses = transferProcessStore.nextForState(IN_PROGRESS.code(), batchSize);
        transferProcesses.stream()
            .filter(p -> p.getStateTimestamp() > stateTimeout)
            .forEach(p -> {
                p.transitionError(format("Timeout (%ss)", stateTimeout));
                transferProcessStore.update(p);
                monitor.info("Timeout for process " + p);
            });
    }
}
