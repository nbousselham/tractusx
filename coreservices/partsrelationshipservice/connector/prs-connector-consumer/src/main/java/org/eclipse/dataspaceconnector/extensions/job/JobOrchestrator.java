package org.eclipse.dataspaceconnector.extensions.job;

import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;
import org.eclipse.dataspaceconnector.spi.transfer.response.ResponseStatus;
import org.eclipse.dataspaceconnector.spi.types.domain.metadata.DataEntry;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public class JobOrchestrator {

    private TransferProcessManager processManager;
    private InMemoryJobStore jobStore;

    public JobOrchestrator(TransferProcessManager processManager, InMemoryJobStore jobStore) {
        this.processManager = processManager;
        this.jobStore = jobStore;
    }

    public JobInitiateResponse startJob(String filename, String connectorAddress, String destinationPath) {

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
                        .property("path", destinationPath) //where we want the file to be stored
                        .build())
                .managedResources(false) //we do not need any provisioning
                .build();

        var response = processManager.initiateConsumerRequest(dataRequest);

        Job job = Job.Builder.newInstance()
                .id(randomUUID().toString())
                .transferProcessIds(response.getId())
                .state(JobState.UNSAVED)
                .build();
        jobStore.create(job);

        return JobInitiateResponse.Builder.newInstance().id(job.getId()).status(ResponseStatus.OK).build();
    }

    public Job findJob(String jobId) {
        return jobStore.find(jobId);
    }
}
