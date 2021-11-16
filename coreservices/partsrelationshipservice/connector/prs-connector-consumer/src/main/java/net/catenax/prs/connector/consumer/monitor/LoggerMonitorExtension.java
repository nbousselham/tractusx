package net.catenax.prs.connector.consumer.monitor;

import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.system.MonitorExtension;

public class LoggerMonitorExtension implements MonitorExtension {
    @Override
    public Monitor getMonitor() {
        return new LoggerMonitor();
    }
}
