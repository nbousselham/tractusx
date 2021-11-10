package org.eclipse.dataspaceconnector.extensions.parser;

public class TransferProcessInput {
    private String filename;
    private String connectorUrl;

    public TransferProcessInput(String filename, String connectorUrl) {
        this.filename = filename;
        this.connectorUrl = connectorUrl;
    }

    public String getFilename() {
        return filename;
    }

    public String getConnectorUrl() {
        return connectorUrl;
    }
}
