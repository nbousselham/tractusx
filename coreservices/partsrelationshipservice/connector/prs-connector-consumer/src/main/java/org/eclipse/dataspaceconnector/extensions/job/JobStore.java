package org.eclipse.dataspaceconnector.extensions.job;

import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;

public interface JobStore {
    Job find(String id);

    void create(Job job);
}
