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

package org.eclipse.dataspaceconnector.transfer.core;

import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;
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
                        .build();

        monitor.info("Initialized Core Transfer extension");
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
