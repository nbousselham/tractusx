//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//

package org.eclipse.dataspaceconnector.transfer.core;

import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.transfer.core.transfer.TransferProcessWatchdog;

import java.util.Set;

/**
 * TransferProcessWatchdog extension
 */
public class TransferProcessWatchdogExtension implements ServiceExtension {

    private static final int BATCH_SIZE = 5;

    private Monitor monitor;
    private ServiceExtensionContext context;
    private TransferProcessWatchdog watchdog;

    @Override
    public Set<String> requires() {
        return Set.of("dataspaceconnector:transfer-process-manager");
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        monitor = context.getMonitor();
        this.context = context;

        watchdog = TransferProcessWatchdog.builder()
                        .monitor(monitor)
                        .period(Double.parseDouble(context.getSetting("edc.watchdog.delay", "1")))
                        .stateTimeout(Double.parseDouble(context.getSetting("edc.watchdog.timeout", "60")))
                        .batchSize(BATCH_SIZE)
                        .build();

        monitor.info("Initialized Transfer Process Watchdog extension");
    }

    @Override
    public void start() {
        var transferProcessStore = context.getService(TransferProcessStore.class);
        watchdog.start(transferProcessStore);
        monitor.info("Started Transfer Process Watchdog extension");
    }

    @Override
    public void shutdown() {
        if (watchdog != null) {
            watchdog.stop();
        }
        monitor.info("Shutdown Transfer Process Watchdog extension");
    }

}
