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

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * TransferProcess watchdog thread that cancels long running transfer processes after a timeout
 */
public final class TransferProcessWatchdog {
    private final Monitor monitor;
    private final int batchSize;
    private final Duration interval;
    private final Duration stateTimeout;

    private ScheduledExecutorService executor;

    /**
     * @param monitor The monitor
     * @param batchSize Transfer process batch size for each iteration
     * @param interval Watchdog polling interval in seconds
     * @param stateTimeout Process timeout in seconds
     */
    @Builder
    private TransferProcessWatchdog(Monitor monitor, int batchSize, double interval, double stateTimeout) {
        this.monitor = monitor;
        this.batchSize = batchSize;
        this.interval = Duration.of((long) secondsToMillis(interval), ChronoUnit.MILLIS);
        this.stateTimeout = Duration.of((long) secondsToMillis(stateTimeout), ChronoUnit.MILLIS);
    }

    /**
     * Starts the watchdog thread
     * @param processStore Process store
     */
    public void start(TransferProcessStore processStore) {
        var action = CancelLongRunningProcesses.builder()
                .monitor(monitor)
                .stateTimeout(stateTimeout)
                .batchSize(batchSize)
                .transferProcessStore(processStore)
                .build();
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(action, 0, interval.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Stop the watchdog thread
     */
    public void stop() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private double secondsToMillis(double seconds) {
        return seconds * 1000;
    }
}
