package org.eclipse.dataspaceconnector.extensions.monitor;

import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.system.MonitorExtension;

public class LoggerMonitorExtension implements MonitorExtension {
    @Override
    public Monitor getMonitor() {
        return new LoggerMonitor();
    }
}
