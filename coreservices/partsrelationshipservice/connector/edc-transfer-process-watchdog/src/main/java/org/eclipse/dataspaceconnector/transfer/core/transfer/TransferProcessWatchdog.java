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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TransferProcessWatchdog {
    private final Monitor monitor;
    private final int batchSize;
    private final long delayInMs;
    private final long stateTimeoutInMs;

    private ScheduledExecutorService executor;

    @Builder
    private TransferProcessWatchdog(Monitor monitor, int batchSize, long delayInMs, long stateTimeoutInMs) {
        this.monitor = monitor;
        this.batchSize = batchSize;
        this.delayInMs = delayInMs;
        this.stateTimeoutInMs = stateTimeoutInMs;
    }

    public void start(TransferProcessStore processStore, TransferProcessManager transferProcessManager) {
        var action = CancelLongRunningProcesses.builder()
                .monitor(monitor)
                .stateTimeoutInMs(stateTimeoutInMs)
                .batchSize(batchSize)
                .transferProcessStore(processStore)
                .transferProcessManager(transferProcessManager)
                .build();
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(action, 0, delayInMs, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }
}
