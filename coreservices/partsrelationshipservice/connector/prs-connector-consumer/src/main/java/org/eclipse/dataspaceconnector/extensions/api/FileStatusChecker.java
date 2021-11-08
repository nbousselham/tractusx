package org.eclipse.dataspaceconnector.extensions.api;

import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.*;

import java.nio.file.Path;
import java.util.List;

public class FileStatusChecker implements StatusChecker {

    private final Monitor monitor;

    public FileStatusChecker(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public boolean isComplete(TransferProcess transferProcess, List<ProvisionedResource> list) {
        var destination = transferProcess.getDataRequest().getDataDestination();
        var destinationPath = Path.of(destination.getProperty("path"));

        if (destinationPath.toFile().isDirectory()) {
            var source = transferProcess.getDataRequest().getDataEntry().getCatalogEntry().getAddress();
            var sourceFileName = source.getProperty("filename");
            destinationPath = Path.of(destinationPath.toString(), sourceFileName);
        }

        boolean exists = destinationPath.toFile().exists();

        monitor.info("File status checker: " + exists);

        return exists;
    }
}
