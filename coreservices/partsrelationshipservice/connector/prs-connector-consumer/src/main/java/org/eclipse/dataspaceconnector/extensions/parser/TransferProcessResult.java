package org.eclipse.dataspaceconnector.extensions.parser;

import java.util.Collection;

public class TransferProcessResult {

    private String value;
    private Collection<TransferProcessInput> transferProcesses;

    public TransferProcessResult() {
    }

    public TransferProcessResult(String value, Collection<TransferProcessInput> transferProcesses) {
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
