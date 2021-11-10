package org.eclipse.dataspaceconnector.extensions.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;

import static java.util.Collections.emptyList;

public class TransferProcessResultParser {

    private final Monitor monitor;

    public TransferProcessResultParser(Monitor monitor) {
        this.monitor = monitor;
    }

    public TransferProcessResult parse(TransferProcess process) {
        Path destinationPath = getDestinationPath(process);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(destinationPath.toFile(), TransferProcessResult.class);
        } catch (IOException e) {
            monitor.severe("Could not retrieve results for process " + process.getId(), e);
        }

        return new TransferProcessResult("error", emptyList());
    }

    private Path getDestinationPath(TransferProcess process) {
        var destination = process.getDataRequest().getDataDestination();
        var destinationPath = Path.of(destination.getProperty("path"));

        File file = destinationPath.toFile();
        if (file.exists() && file.isDirectory()) {
            destinationPath = Path.of(destinationPath.toString(), process.getDataRequest().getDataEntry().getId());
        }

        return destinationPath;
    }
}
