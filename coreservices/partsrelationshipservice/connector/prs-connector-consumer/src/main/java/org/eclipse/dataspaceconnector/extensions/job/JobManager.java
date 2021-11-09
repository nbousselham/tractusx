package org.eclipse.dataspaceconnector.extensions.job;

import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;

public interface JobManager {
    void start(JobStore jobStore, TransferProcessStore processStore);

    void stop();
}
