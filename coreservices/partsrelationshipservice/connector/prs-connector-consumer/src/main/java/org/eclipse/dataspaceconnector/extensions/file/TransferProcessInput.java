package org.eclipse.dataspaceconnector.extensions.file;

public class TransferProcessInput {
    private String file;
    private String connectorUrl;

    public TransferProcessInput() {
    }

    public TransferProcessInput(String file, String connectorUrl) {
        this.file = file;
        this.connectorUrl = connectorUrl;
    }

    public String getFile() {
        return file;
    }

    public String getConnectorUrl() {
        return connectorUrl;
    }
}
