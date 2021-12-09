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

import java.time.Clock;
import java.time.Duration;

import static java.time.Instant.now;
import static java.time.Instant.ofEpochMilli;
import static org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates.IN_PROGRESS;

@Builder
class CancelLongRunningProcesses implements Runnable {
    private final Monitor monitor;
    private final int batchSize;
    private final Duration stateTimeout;

    @Builder.Default
    private Clock clock = Clock.systemUTC();

    private final TransferProcessStore transferProcessStore;

    public void run() {
        var transferProcesses = transferProcessStore.nextForState(IN_PROGRESS.code(), batchSize);

        transferProcesses.stream()
            .filter(p -> ofEpochMilli(p.getStateTimestamp()).isBefore(now(clock).minus(stateTimeout)))
            .forEach(p -> {
                p.transitionError("Timed out waiting for process to complete after > " + stateTimeout.toMillis() + "ms");
                /*
                 * IMPORTANT NOTE: Updating the process here might cause a race condition with the updates performed from the main loop in TransferProcessManagerImpl.
                 * At the moment EDC offers no capabilities of preventing this in any way. Issue #330 (https://github.com/eclipse-dataspaceconnector/DataSpaceConnector/issues/330)
                 * will rework TransferProcessManagerImpl to prevent such situations.
                 *
                 * The consequences of this race condition are not too critical in this case though. If a process happens to finish at the same time as its timeout
                 * the resulting state of the process (COMPLETED VS ERROR) will be determined by the thread that manages to update the process the last.
                 */
                transferProcessStore.update(p);
                monitor.info("Timeout for process " + p);
            });
    }
}
