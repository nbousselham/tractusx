package org.eclipse.dataspaceconnector.extensions.api;

import org.eclipse.dataspaceconnector.extensions.job.InMemoryJobStore;
import org.eclipse.dataspaceconnector.extensions.job.JobOrchestrator;
import org.eclipse.dataspaceconnector.extensions.job.JobStore;
import org.eclipse.dataspaceconnector.spi.protocol.web.WebService;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessObservable;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;

import java.util.Set;

public class ApiEndpointExtension implements ServiceExtension {

    private ServiceExtensionContext context;

    @Override
    public Set<String> requires() {
        return Set.of(
                "edc:webservice",
                "dataspaceconnector:transferprocessstore"
        );
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        this.context = context;
        var monitor = context.getMonitor();

        var processManager = context.getService(TransferProcessManager.class);
        var processStore = context.getService(TransferProcessStore.class);

        // in memory job store extension
        var jobStore = new InMemoryJobStore();
        context.registerService(JobStore.class, jobStore);

        // job extension
        var jobOrchestrator = new JobOrchestrator(processManager, jobStore, monitor);
        TransferProcessObservable transferProcessObservable = context.getService(TransferProcessObservable.class);
        transferProcessObservable.registerListener(jobOrchestrator);

        // web service extension
        var webService = context.getService(WebService.class);
        webService.registerController(new ConsumerApiController(context.getMonitor(), processManager, processStore, jobOrchestrator));
    }

}
