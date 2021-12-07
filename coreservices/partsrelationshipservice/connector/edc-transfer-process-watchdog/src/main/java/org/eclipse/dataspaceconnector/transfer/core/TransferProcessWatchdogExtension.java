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
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.transfer.core.transfer.TransferProcessWatchdog;

import java.util.Set;

public class TransferProcessWatchdogExtension implements ServiceExtension {

    private Monitor monitor;
    private ServiceExtensionContext context;
    private TransferProcessWatchdog watchdog;

    @Override
    public Set<String> requires() {
        return Set.of("dataspaceconnector:transfer-process-manager");
    }

    @Override
    public LoadPhase phase() {
        return LoadPhase.PRIMORDIAL;
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        monitor = context.getMonitor();
        this.context = context;

        watchdog = TransferProcessWatchdog.builder()
                        .monitor(monitor)
                        .delayInSeconds(Long.parseLong(context.getSetting("edc.watchdog.delay", "1")))
                        .stateTimeout(Long.parseLong(context.getSetting("edc.watchdog.timeout", "60")))
                        .batchSize(5)
                        .build();

        monitor.info("Initialized Core Transfer extension");
    }

    @Override
    public void start() {
        var transferProcessStore = context.getService(TransferProcessStore.class);
        var transferProcessManager = context.getService(TransferProcessManager.class);
        watchdog.start(transferProcessStore, transferProcessManager);
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
