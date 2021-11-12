package org.eclipse.dataspaceconnector.extensions.job;

import org.eclipse.dataspaceconnector.extensions.file.TransferProcessInput;
import org.eclipse.dataspaceconnector.extensions.file.TransferProcessFileHandler;
import org.eclipse.dataspaceconnector.spi.transfer.TransferInitiateResponse;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessListener;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;
import org.eclipse.dataspaceconnector.spi.transfer.response.ResponseStatus;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.spi.types.domain.metadata.DataEntry;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

public class JobOrchestrator implements TransferProcessListener {

    private final TransferProcessManager processManager;
    private final JobStore jobStore;
    private final TransferProcessStore processStore;
    private final TransferProcessFileHandler transferProcessFileHandler;

    public JobOrchestrator(TransferProcessManager processManager, JobStore jobStore, TransferProcessStore processStore, TransferProcessFileHandler transferProcessFileHandler) {
        this.processManager = processManager;
        this.jobStore = jobStore;
        this.processStore = processStore;
        this.transferProcessFileHandler = transferProcessFileHandler;
    }

    public JobInitiateResponse startJob(String filename, String connectorAddress, String destinationPath) {
        Job job = Job.Builder.newInstance()
                .id(randomUUID().toString())
                .destinationPath(destinationPath)
                .state(JobState.UNSAVED)
                .build();

        jobStore.create(job);

        startTransferProcess(job, filename, connectorAddress);

        return JobInitiateResponse.Builder.newInstance().id(job.getId()).status(ResponseStatus.OK).build();
    }

    public void startTransferProcess(Job job, String filename, String connectorAddress) {
        String processDestinationPath = Path.of(job.getDestinationPath()).getParent().toString();
        var dataRequest = DataRequest.Builder.newInstance()
                .id(UUID.randomUUID().toString()) //this is not relevant, thus can be random
                .connectorAddress(connectorAddress) //the address of the provider connector
                .protocol("ids-rest") //must be ids-rest
                .connectorId("consumer")
                .dataEntry(DataEntry.Builder.newInstance() //the data entry is the source asset
                        .id(filename)
                        .policyId("use-eu")
                        .build())
                .dataDestination(DataAddress.Builder.newInstance()
                        .type("File") //the provider uses this to select the correct DataFlowController
                        .property("jobId", job.getId()) // store jobId for further retrieval
                        .property("path", processDestinationPath) //where we want the file to be stored
                        .build())
                .managedResources(false) //we do not need any provisioning
                .build();

        TransferInitiateResponse response = processManager.initiateConsumerRequest(dataRequest);

        jobStore.addTransferProcess(job.getId(), response.getId());
    }

    @Override
    public void completed(TransferProcess process) {
        var jobId = process.getDataRequest().getDataDestination().getProperty("jobId");
        var job = jobStore.find(jobId);
        var result = transferProcessFileHandler.parse(process);

        for (TransferProcessInput nextTransferProcess : result.getTransferProcesses()) {
            startTransferProcess(job, nextTransferProcess.getFile(), nextTransferProcess.getConnectorUrl());
        }

        jobStore.completeTransferProcess(job.getId(), process.getId());

        if (job.getState() == JobState.TRANSFERS_FINISHED) {
            List<TransferProcess> processes = job.getCompletedTransferProcessIds().stream().map(processStore::find).collect(Collectors.toList());
            transferProcessFileHandler.aggregate(job, processes);
            jobStore.completeJob(job.getId());
        }
    }

    public Job findJob(String jobId) {
        return jobStore.find(jobId);
    }
}
