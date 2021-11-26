/*
 *  Copyright (c) 2020, 2021 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - initial API and implementation
 *
 */

package org.eclipse.dataspaceconnector.transfer.core.transfer;

import lombok.Builder;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates.IN_PROGRESS;

public class TransferProcessWatchdog {
    private final Monitor monitor;
    private TransferProcessStore transferProcessStore;
    private final int batchSize;
    private final long delayInSeconds;

    private ScheduledExecutorService executor;

    @Builder
    private TransferProcessWatchdog(Monitor monitor) {
        this.monitor = monitor;
        // TODO: make props configurable
        this.batchSize = 5;
        this.delayInSeconds = 1;
    }

    public void start(TransferProcessStore processStore) {
        transferProcessStore = processStore;
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(this::run, 0, delayInSeconds, TimeUnit.SECONDS);
    }

    public void stop() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    private void run() {
        transferProcessStore.nextForState(IN_PROGRESS.code(), batchSize);
        monitor.info("Cleanup long running transfer process...");
    }
}
