package org.eclipse.dataspaceconnector.extensions.job;

public interface JobStore {
    Job find(String id);

    void create(Job job);

    void delete(String processId);

    void addTransferProcess(String jobId, String processId);

    void completeTransferProcess(String jobId, String processId);
}
