package org.eclipse.dataspaceconnector.extensions.file;

import java.util.Collection;

public class TransferProcessFile {

    private String value;
    private Collection<TransferProcessInput> transferProcesses;

    public TransferProcessFile() {
    }

    public TransferProcessFile(String value, Collection<TransferProcessInput> transferProcesses) {
        this.value = value;
        this.transferProcesses = transferProcesses;
    }

    public String getValue() {
        return value;
    }

    public Collection<TransferProcessInput> getTransferProcesses() {
        return transferProcesses;
    }
}
