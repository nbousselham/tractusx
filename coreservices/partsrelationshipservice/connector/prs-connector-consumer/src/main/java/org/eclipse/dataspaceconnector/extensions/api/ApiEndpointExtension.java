package org.eclipse.dataspaceconnector.extensions.api;

import org.eclipse.dataspaceconnector.extensions.job.*;
import org.eclipse.dataspaceconnector.spi.protocol.web.WebService;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.StatusCheckerRegistry;
import org.eclipse.dataspaceconnector.transfer.store.memory.InMemoryTransferProcessStore;

import java.util.Set;

public class ApiEndpointExtension implements ServiceExtension {

    private ServiceExtensionContext context;
    private JobManager jobManager;

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

        var webService = context.getService(WebService.class);
        var processManager = context.getService(TransferProcessManager.class);
        var processStore = context.getService(TransferProcessStore.class);

        var jobStore = new InMemoryJobStore();
        context.registerService(JobStore.class, jobStore);
        var jobOrchestrator = new JobOrchestrator(processManager, jobStore);

        webService.registerController(new ConsumerApiController(context.getMonitor(), processManager, processStore, jobOrchestrator));

        var statusCheckerReg = context.getService(StatusCheckerRegistry.class);
        statusCheckerReg.register("File", new FileStatusChecker(monitor));

        // job extension
        jobManager = JobManagerImpl.Builder.newInstance().monitor(monitor).build();
        context.registerService(JobManager.class, jobManager);
    }

    @Override
    public void start() {
        var monitor = context.getMonitor();

        var jobStore = context.getService(JobStore.class);
        var processStore = context.getService(TransferProcessStore.class);
        jobManager.start(jobStore, processStore);

        monitor.info("Started Job Manager");
    }
}
