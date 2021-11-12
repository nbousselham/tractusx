package org.eclipse.dataspaceconnector.extensions.file;

import java.util.Collection;

public class TransferProcessFile {

    private String value;
    private int order;
    private Collection<TransferProcessInput> transferProcesses;

    public TransferProcessFile() {
    }

    public TransferProcessFile(String value, int order, Collection<TransferProcessInput> transferProcesses) {
        this.value = value;
        this.order = order;
        this.transferProcesses = transferProcesses;
    }

    public String getValue() {
        return value;
    }

    public int getOrder() {
        return order;
    }

    public Collection<TransferProcessInput> getTransferProcesses() {
        return transferProcesses;
    }
}
