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
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;

import java.time.Instant;

import static java.lang.String.format;
import static java.time.Instant.now;
import static java.time.Instant.ofEpochMilli;
import static org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates.IN_PROGRESS;

@Builder
class CancelLongRunningProcesses implements Runnable {
    private final Monitor monitor;
    private final int batchSize;
    private final long stateTimeout;

    private final TransferProcessStore transferProcessStore;
    private final TransferProcessManager transferProcessManager;

    public void run() {
        var transferProcesses = transferProcessStore.nextForState(IN_PROGRESS.code(), batchSize);

        transferProcesses.stream()
            .filter(p -> ofEpochMilli(p.getStateTimestamp()).isBefore(now().minusSeconds(stateTimeout)))
            .forEach(p -> {
                transferProcessManager.cancelTransferProcess(p.getId());
                monitor.info("Timeout for process " + p);
            });
    }
}
